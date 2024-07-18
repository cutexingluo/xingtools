package top.cutexingluo.tools.auto.server;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import top.cutexingluo.tools.utils.ee.redis.core.RedisSerializerEnum;


/**
 * xingtools.enabled 配置文件开启
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 18:52
 */
@Data
@Primary
@ConfigurationProperties(prefix = "xingtools.enabled")
@Slf4j
public class AutoInjectProperty {

    /**
     * 启动日志是否开启
     * <p>log-info</p>
     */
    private boolean logInfo = true;

    /**
     * spring getBean spring工具类是否开启，默认开启
     * <p>springutils</p>
     */
    private boolean springutils = true;


//    /**
//     * 代码生成器配置类,默认开启
//     */
//    private boolean vmGenerator = true;

    /**
     * <p>
     * aop Lock锁注解是否开启 依赖于redissonConfig 所以上面redisconfig需要开启<br><br>
     * </p>
     *<p>默认关闭</p>
     * <p>xt-aop-lock</p>
     */
    private boolean xtAopLock = true;

    // 全局异常拦截是否开启
    /**
     * 全局异常拦截是否开启，推荐开启<br>
     * <p>默认关闭</p>
     * <p>global-exception</p>
     */
    private boolean globalException = false;


    /**
     * 自定义操作注解是否开启 <br>
     * Add @MethodLog annotation.
     * <p>optlog-anno</p>
     */
    private boolean optlogAnno = false;

    /**
     * 方法调用日志注解是否开启 <br>
     * Add @MethodLog annotation.
     * <p>methodlog-anno</p>
     */
    private boolean methodlogAnno = false;

    /**
     * 接口调用日志注解是否开启 <br>
     * Add @XTSystemLog annotation.
     * <p>xt-systemlog-anno</p>
     */
    private boolean xtSystemlogAnno = false;

    /**
     * <p> 异步线程（含事务，锁）综合注解是否开启</p>
     * Add @MainThread  and  @SonThread
     * <p>async-thread-aop-anno</p>
     */
    private boolean asyncThreadAopAnno = false;

    /**
     * redis默认配置 并注入RedisTemplate，默认关闭
     * <p>开启后需要导入 redis 相关依赖包，例如spring-boot-starter-data-redis</p>
     * <p>redisconfig</p>
     */
    private boolean redisconfig = false;


    /**
     * redisTemplate 序列化方式，默认jackson， 目前支持jackson, fastjson 两种
     *
     * <p>redisconfig-setting</p>
     */
    private RedisSerializerEnum redisconfigSetting = RedisSerializerEnum.jackson;

    /**
     * redis注入一系列的Redis工具类，默认关闭
     * <p>例如RYRedisCache,QGRedisUtils,RedisUtil,XTRedisUtil等工具类全部自动注入</p>
     * <p>开启前需要把 redisconfig 项打开，并导入spring-data-redis 相关依赖</p>
     *
     * <p>redisconfig-util</p>
     */
    private boolean redisconfigUtil = false;


    //********以上一般都会遇到

    //sa-token整合jwt，默认关闭
    /**
     * sa-token整合jwt，默认关闭
     * <p>satokenjwt</p>
     */
    private boolean satokenjwt = false;


    // redisson 分布式锁是否开启 默认关闭
    /**
     * 分布式锁是否开启 默认关闭<br>
     * 需要引入 redisson 依赖<br>
     * 打开后会自动装配RedissonClient (会利用redis端口)
     * - 推荐使用 redisson-aop 配置
     * <p>redisson-config</p>
     */
    private boolean redissonConfig = false;

    /**
     * 分布式锁Aop是否开启 默认关闭<br>
     * 需要引入 redisson 依赖 和 lock4j-redisson-spring-boot-starter依赖 <br>
     * <p>
     * <b>打开后，可使用@Lock4j 注解 和 LockTemplate</b>
     * </p>
     * <ul>
     *     <li>1.使用方式：<br>   @Lock4j(keys = {"#key"}, acquireTimeout = 10, expire = 10000
     *             ,executor = XTRedissonExecutor.class
     *     )</li>
     *     <li>
     *         2.使用方式：<br>    @Autowired <br>
     *     private LockTemplate lockTemplate;
     *     </li>
     * </ul>
     * <p>redisson-aop</p>
     */
    private boolean redissonAop = false;

    /**
     * 全局异常拦截扩展是否开启，支持sa-token扩展<br>
     * 默认关闭
     * <p>global-exception-ext</p>
     */
    private boolean globalExceptionExt = false;

    // springcache 配置是否开启
    /**
     * springcache  自动配置类 是否开启，默认关闭
     * <p>springcache</p>
     */
    private boolean springcache = false;

    // XTThreadPool全局单例 线程池
//    /**
//     * XTThreadPool全局单例 线程池 默认关闭
//     * <p>xt-thread-pool</p>
//     */
//    private boolean xtThreadPool = false;
    //MybatisPlus 分页插件
    /**
     * MybatisPlus 分页插件自动配置 使用分页插件推荐开启<br> 默认关闭
     * <p>mybatis-plus-config</p>
     */
    private boolean mybatisPlusConfig = false;
    // 跨域拦截
//    /**
//     * 跨域拦截默认配置是否开启<br>默认关闭
//     * <p>cors-config</p>
//     */
//    private boolean corsConfig = false;

    // 拦截器
//    /**
//     * 默认拦截器配置，默认关闭
//     * <p>interceptor-config</p>
//     */
//    private boolean interceptorConfig = false;
    // swagger
//    /**
//     * 默认swagger配置，默认关闭
//     * <p>swagger-config</p>
//     */
//    private boolean swaggerConfig = false;

    /**
     * 是否开启 @RequestLimit 防重复提交注解，默认关闭
     *
     * <p>如果需要默认的Redis策略，则需要把 request-limit-redis 配置打开</p>
     *<p>request-limit</p>
     * @since 1.0.4
     */
    private boolean requestLimit = false;


    /**
     * 提供自带的 Redis 策略 防重复提交注解，默认关闭
     * <p>1.需要导入 RedisTemplate 相关的包</p>
     * <p>2.需要开启request-limit配置，使注解 @RequestLimit  配置生效</p>
     *<p>request-limit-redis</p>
     * @since 1.0.4
     */
    private boolean requestLimitRedis = false;


    /**
     * 是否启用 @WebLog 注解 AOP
     * <p>极力推荐，根据策略自定义打印字符串</p>
     * <p>默认关闭</p>
     *<p>web-log-aop</p>
     * @since 1.0.4
     */
    private boolean webLogAop = false;
}
