package top.cutexingluo.tools.utils.ee.web.limit.easylimit;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import top.cutexingluo.tools.bridge.servlet.adapter.HttpServletRequestAdapter;
import top.cutexingluo.tools.bridge.servlet.adapter.HttpServletResponseAdapter;
import top.cutexingluo.tools.common.HttpStatus;
import top.cutexingluo.tools.common.Result;
import top.cutexingluo.tools.common.base.IResult;
import top.cutexingluo.tools.common.utils.GlobalResultFactory;
import top.cutexingluo.tools.utils.ee.redis.RYRedisCache;
import top.cutexingluo.tools.utils.ee.redis.RYRedisUtil;
import top.cutexingluo.tools.utils.ee.web.ip.util.IPUtil;
import top.cutexingluo.tools.utils.ee.web.limit.submit.base.RequestLimit;
import top.cutexingluo.tools.utils.ee.web.limit.submit.pkg.RequestLimitHandler;
import top.cutexingluo.tools.utils.spring.SpringUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 限流工具类
 *
 * <p>1.0.4 及以后版本推荐使用 {@link RequestLimit} 注解 或 {@link RequestLimitHandler} 编程式工具</p>
 * <p>为防止过多依赖, 1.1.4 之后采用 IPUtil 代替原来的 IpUtils</p>
 * <p>需要导入 spring-data-redis 相关的包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/17 20:30
 */
@Slf4j
public class AccessLimitUtil {

    public static RedisTemplate<String, Object> redisTemplate;
    public static RYRedisCache redisCache;
    protected static ApplicationContext applicationContext;
    protected static boolean showLog = true;
    protected static boolean firstTime = true;

    protected static GlobalResultFactory globalResultFactory = null;


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

    public static boolean checkRedis() throws Exception {
        if (applicationContext == null) {
            applicationContext = SpringUtils.getApplicationContext();
        }
        boolean b = RYRedisUtil.checkRedisSource(redisCache, redisTemplate, applicationContext);
        firstTime = false;
        if (!b) {
            throw new RuntimeException("" +
                    "条件一： RYRedisCache未设置 或者" +
                    "RedisTemplate未设置。" +
                    "条件二： 如果两个均未设置，则需设置ApplicationContext，" +
                    "如果ApplicationContext已设置，" +
                    "但找不到相应的bean！");
        }
        return b;
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
     */
    public static <T> boolean limit(HttpServletResponseAdapter response,
                                    String redisKey,
                                    int interval, int maxCount, T responseResult) throws Exception {
        if (firstTime && !checkRedis()) { //第一次对配置进行检查
            return false;
        }
        boolean result = true;
        try {
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
        } catch (RedisConnectionFailureException e) {
            throw e;
        }
        return result;
    }
}
