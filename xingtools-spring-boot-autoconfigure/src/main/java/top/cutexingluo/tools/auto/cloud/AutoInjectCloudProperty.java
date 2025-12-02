package top.cutexingluo.tools.auto.cloud;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

/**
 * 需要打开@EnableXingToolsCloudServer 注解
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/4 16:32
 */
@Data
@Primary
@ConfigurationProperties(prefix = "xingtools.cloud.enabled")
@Slf4j
public class AutoInjectCloudProperty {

    //-----------------feign------------------------

//    /**
//     * feign 调用保留请求头，默认关闭
//     */
//    private boolean retainFeignRequest = false;
//
//    /**
//     * 需要保留的请求头，配置前需要把 retain-feign-request 开启
//     */
//    private List<String> retainFeignRequestHeaders = Arrays.asList("Authorization", "token");
//
//    /**
//     * 需要保留所有 Cookie，配置前需要把 retain-feign-request 开启
//     *
//     * @since 1.0.4
//     */
//    private boolean retainFeignRequestAllowAllCookies = false;

    /**
     * 动态 feign 调用，使用编程式来调用 feign
     * <br>默认关闭
     * <ul>
     * <li>
     *     使用方法: 注入 DynamicClient 类
     * </li>
     * </ul>
     */
    private boolean dynamicFeign = false;

    /**
     * 开启 feign retry aop注解功能
     * <br> 需要导入spring-retry 包
     * <br> 默认关闭
     */
    private boolean feignRetry = false;

    /**
     * 开启 controller 接口限流 Limit Aop注解 <br>
     * 需要提前导入 google 的 guava 包
     */
    private boolean currentLimit = false;

//    /**
//     * 开启 ruoyi redisLimit 限流 RateLimiter Aop注解 <br>
//     * 利用Redis 对指定ip 限流保存 <br>
//     * - 需要导入redis相关依赖，并配置 RedisTemplate 或者打开 redis-config
//     */
//    private boolean redisLimit = false;

//    /**
//     * 开启 ip 拦截 注解<br>
//     * 需要提前导入 org.lionsoul 的 ip2region 包
//     */
//    private boolean ipSearch = false;

}
