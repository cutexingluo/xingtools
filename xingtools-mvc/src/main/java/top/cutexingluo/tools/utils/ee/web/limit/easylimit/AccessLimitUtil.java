package top.cutexingluo.tools.utils.ee.web.limit.easylimit;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import top.cutexingluo.core.bridge.servlet.adapter.HttpServletRequestAdapter;
import top.cutexingluo.core.bridge.servlet.adapter.HttpServletResponseAdapter;
import top.cutexingluo.core.common.base.IResult;
import top.cutexingluo.core.common.data.Entry;
import top.cutexingluo.core.common.result.HttpStatus;
import top.cutexingluo.core.common.result.Result;
import top.cutexingluo.core.common.utils.GlobalResultFactory;
import top.cutexingluo.tools.utils.ee.redis.RYRedisCache;
import top.cutexingluo.tools.utils.ee.redis.RYRedisUtil;
import top.cutexingluo.tools.utils.ee.web.ip.util.IPUtil;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimit;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimitData;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.RequestLimitHandler;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;
import top.cutexingluo.tools.utils.spring.SpringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 限流工具类
 *
 * <p>1.0.4 及以后版本推荐使用 {@link RequestLimit} 注解 或 {@link RequestLimitHandler} 编程式工具</p>
 * <p>为防止过多依赖, 1.1.4 之后采用 IPUtil 代替原来的 IpUtils, 并且使用 LimitStrategy 策略接口 作为新的限制策略, 后续将移除大部分方法</p>
 * <p>需要导入 spring-data-redis 相关的包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/17 20:30
 */
@Slf4j
public class AccessLimitUtil {

    @Deprecated
    protected static ApplicationContext applicationContext;
    @Deprecated
    protected static RedisTemplate<String, Object> redisTemplate;
    @Deprecated
    protected static RYRedisCache redisCache;

    @Deprecated
    protected static boolean showLog = true;

    @Deprecated
    protected static GlobalResultFactory globalResultFactory = null;

    /**
     * 第一次首次检查 - false - 检查过了
     */
    protected static boolean firstTime = true;


    /**
     * 限制策略
     */
    protected static LimitStrategy limitStrategy;


    public static void setShowLog(boolean showLog) {
        AccessLimitUtil.showLog = showLog;
    }

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        AccessLimitUtil.redisTemplate = redisTemplate;
    }

    public static void setRedisCache(RYRedisCache redisCache) {
        AccessLimitUtil.redisCache = redisCache;
        firstTime = false;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        AccessLimitUtil.applicationContext = applicationContext;
    }

    public static void setGlobalResultFactory(GlobalResultFactory globalResultFactory) {
        AccessLimitUtil.globalResultFactory = globalResultFactory;
    }

    public static void setLimitStrategy(LimitStrategy limitStrategy) {
        AccessLimitUtil.limitStrategy = limitStrategy;
    }

    @Deprecated
    public static boolean checkRedis() {
        if (applicationContext == null) {
            applicationContext = SpringUtils.getApplicationContext();
        }
        redisCache = RYRedisUtil.checkRedisSource(redisCache, redisTemplate, applicationContext);
        firstTime = false;
        if (redisCache == null) {
            throw new NullPointerException("" +
                    "条件一： RYRedisCache未设置 或者" +
                    "RedisTemplate未设置。" +
                    "条件二： 如果两个均未设置，则需设置ApplicationContext，" +
                    "如果ApplicationContext已设置，" +
                    "但找不到相应的bean！");
        }
        return true;
    }

    public static boolean checkRedisStrategy() {
        if (!firstTime) { //第一次对配置进行检查
            return true;
        }
        if (limitStrategy == null) {
            throw new NullPointerException("limitStrategy 未设置！使用该方法必须设置！");
        }
        firstTime = false;
        return true;
    }

    /**
     * 限制过滤器，需要@AccessLimit 注解
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理程序
     * @return boolean
     * @throws Exception 异常
     */
    @Deprecated
    public static boolean limitFilter(HttpServletRequestAdapter request, HttpServletResponseAdapter response, Object handler) throws Exception {
        if (firstTime && !checkRedis()) { //第一次对配置进行检查
            return false;
        }
        boolean result = true;
        // Handler 是否为 HandlerMethod 实例
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            //方法上没有访问控制的注解，直接通过
            if (accessLimit != null) {
                result = limitAll(request, response,
                        accessLimit.seconds(), accessLimit.maxCount(), accessLimit.msg());
            }
        }
        return result;
    }

    /**
     * 限制所有, 默认redis key 为 "IP-" + ip + ":" + method + ":" + requestUri
     *
     * <p>1.0.4 更新为加上 "IP-" 前缀</p>
     *
     * @param request     请求
     * @param response    响应
     * @param interval    时间间隔
     * @param maxCount    最大计数
     * @param outLimitMsg 超出限制消息
     * @return boolean
     * @throws Exception 异常
     * @updateFrom 1.0.4
     */
    @Deprecated
    public static boolean limitAll(HttpServletRequestAdapter request, HttpServletResponseAdapter response,
                                   int interval, int maxCount, String outLimitMsg) throws Exception {
        String ip = IPUtil.getIpAddr(request);
        String method = request.getMethod();
        String requestUri = request.getRequestURI();
        String redisKey = "IP-" + ip + ":" + method + ":" + requestUri;
        return limit(response, redisKey, interval, maxCount, outLimitMsg);
    }


    /**
     * 限制策略
     *
     * @param request             请求
     * @param response            响应
     * @param interval            间隔
     * @param maxCount            最大计数
     * @param keyToResponseResult 自定义key 返回值为异常返回结果
     * @return boolean 是否通过
     * @throws Exception 例外
     * @since 1.0.4
     */
    @Deprecated
    public static <T> boolean limitStrategy(HttpServletRequestAdapter request, HttpServletResponseAdapter response,
                                            int interval, int maxCount, @NotNull Function<String, T> keyToResponseResult) throws Exception {
        String ip = IPUtil.getIpAddr(request);
        String method = request.getMethod();
        String requestUri = request.getRequestURI();
        String redisKey = "IP-" + ip + ":" + method + ":" + requestUri;
        return limit(response, redisKey, interval, maxCount, keyToResponseResult.apply(redisKey));
    }

    /**
     * 限制IP, 默认 redis key = "limitIp:"+ip
     *
     * @param request     请求
     * @param response    响应
     * @param interval    时间间隔
     * @param maxCount    最大计数
     * @param outLimitMsg 出限制味精
     * @return boolean
     * @throws Exception 异常
     */
    @Deprecated
    public static boolean limitIP(HttpServletRequestAdapter request, HttpServletResponseAdapter response,
                                  int interval, int maxCount, String outLimitMsg) throws Exception {
        String ip = IPUtil.getIpAddr(request);
        String redisKey = "limitIp:" + ip;
        return limit(response, redisKey, interval, maxCount, outLimitMsg);
    }

    /**
     * 限流基础方法
     *
     * @param response    响应
     * @param redisKey    复述,关键
     * @param interval    时间间隔
     * @param maxCount    最大计数
     * @param outLimitMsg 超出限制消息
     * @return boolean
     * @throws Exception 异常
     * @updateFrom 1.0.3
     */
    @Deprecated
    public static <C, T> boolean limit(HttpServletResponseAdapter response,
                                       String redisKey,
                                       int interval, int maxCount, String outLimitMsg) throws Exception {
        if (globalResultFactory == null && applicationContext != null) {
            globalResultFactory = SpringUtils.getBeanNoExc(applicationContext, GlobalResultFactory.class);
        }
        IResult<C, T> result = null;
        if (globalResultFactory != null) {
            result = (IResult<C, T>) GlobalResultFactory.selectResult(globalResultFactory, Result.error(outLimitMsg));
        } else {
            result = (IResult<C, T>) Result.error(outLimitMsg);
        }
        return limit(response, redisKey, interval, maxCount, result);
    }

    /**
     * 限制
     * 限流基础方法
     *
     * @param response       响应
     * @param redisKey       复述,关键
     * @param interval       时间间隔
     * @param maxCount       最大计数
     * @param responseResult 响应结果
     * @return boolean 是否通过
     * @throws Exception 异常
     * @deprecated 封装层度过高，并且没有考虑并发问题，故未来移除
     */
    @Deprecated
    public static <T> boolean limit(HttpServletResponseAdapter response,
                                    String redisKey,
                                    int interval, int maxCount, T responseResult) throws Exception {
        if (firstTime && !checkRedis()) { //第一次对配置进行检查
            return false;
        }
        boolean result = true;
//        try {
        Long count = redisCache.incrementCacheValue(redisKey, 1L);
        // 第一次访问
        if (Objects.nonNull(count) && count == 1) {
            redisCache.expire(redisKey, interval, TimeUnit.SECONDS);
        } else if (count > maxCount) {
            response.response(JSONUtil.toJsonStr(responseResult), HttpStatus.SUCCESS.getCode());
//                XTResponseUtil.success(response.getResponse(), JSONUtil.toJsonStr(responseResult));
            if (showLog) log.warn(redisKey + "请求次数超过每" + interval + "秒" + maxCount + "次");
            result = false;
        }
//        } catch (RedisConnectionFailureException e) {
//            throw e;
//        }
        return result;
    }

    /**
     * 限制
     * 限流基础方法
     *
     * @param redisKey         redis 键
     * @param requestLimitData 请求配置
     * @return boolean 是否通过
     * @throws Exception 异常
     * @since 1.1.4
     */
    public static boolean limit(String redisKey, @NotNull RequestLimitData requestLimitData) throws Exception {
        if (!checkRedisStrategy()) { //第一次对配置进行检查
            return false;
        }
        limitStrategy.checkRequestLimit(requestLimitData);
        return limitStrategy.interceptor(requestLimitData, redisKey);
    }

    /**
     * 限制
     * 限流基础方法
     *
     * @param requestLimitData 请求配置
     * @return boolean 是否通过
     * @throws Exception 异常
     * @since 1.1.4
     */
    public static boolean limit(@NotNull RequestLimitData requestLimitData) throws Exception {
        if (!checkRedisStrategy()) { //第一次对配置进行检查
            return false;
        }
        Objects.requireNonNull(requestLimitData.getLimitKey());
        limitStrategy.checkRequestLimit(requestLimitData);
        return limitStrategy.interceptor(requestLimitData);
    }


    /**
     * 限制
     * 限流基础方法
     *
     * @param redisKey redis 键
     * @param interval 时间间隔 (秒)
     * @param maxCount 最大计数
     * @return boolean 是否通过
     * @throws Exception 异常
     * @since 1.1.4
     */
    public static boolean limit(String redisKey, int interval, int maxCount) throws Exception {
        RequestLimitData limitData = new RequestLimitData();
        limitData.setTimeUnit(TimeUnit.SECONDS);
        limitData.setTimeout(interval);
        limitData.setMaxCount(maxCount);
        return limit(redisKey, limitData);
    }

    /**
     * 限制所有, 默认redis key 为 "IP-" + ip + ":" + method + ":" + requestUri
     *
     * <p>1.0.4 更新为加上 "IP-" 前缀</p>
     *
     * @param request  请求
     * @param interval 时间间隔
     * @param maxCount 最大计数
     * @return boolean
     * @throws Exception 异常
     * @since 1.1.4
     */
    public static boolean limitAll(HttpServletRequestAdapter request,
                                   int interval, int maxCount) throws Exception {
        String ip = IPUtil.getIpAddr(request);
        String method = request.getMethod();
        String requestUri = request.getRequestURI();
        String redisKey = "IP-" + ip + ":" + method + ":" + requestUri;
        return limit(redisKey, interval, maxCount);
    }

    /**
     * 限制过滤器，需要@AccessLimit 注解
     *
     * @param request 请求
     * @param handler 处理程序
     * @return boolean
     * @throws Exception 异常
     * @since 1.1.4
     */
    @NotNull
    public static Entry<Boolean, AccessLimit> limitFilterAnno(HttpServletRequestAdapter request, Object handler) throws Exception {
        if (!checkRedisStrategy()) { //第一次对配置进行检查
            return new Entry<>(false, null);
        }
        boolean result = true;
        AccessLimit accessLimit = null;
        // Handler 是否为 HandlerMethod 实例
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
            //方法上没有访问控制的注解，直接通过
            if (accessLimit != null) {
                result = limitAll(request, accessLimit.seconds(), accessLimit.maxCount());
            }
        }
        return new Entry<>(result, accessLimit);
    }

}
