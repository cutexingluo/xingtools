package top.cutexingluo.tools.utils.ee.redis;

import org.jetbrains.annotations.Nullable;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import top.cutexingluo.tools.basepackage.chain.core.BuilderMapChain;
import top.cutexingluo.tools.common.data.Entry;

import java.util.Arrays;

/**
 * RYRedisCache 组合操作工具类
 *
 * <p>需要导入 spring-data-redis 相关的包 </p>
 * <br>
 * 1. 注入RYRedisCache <br>
 * 2.  获取Builder <br>
 * RYRedisUtil.Builder redisUtil = RYRedisUtil.builder(redisCache);
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/20 20:08
 */
public class RYRedisUtil {

    /**
     * 检查Redis源
     * <p>检查顺序 RYRedisCache 参数 -> RedisTemplate 参数(有则创建RYRedisCache赋给参数)
     * -> RYRedisCache bean(有则赋给参数)   ->  RedisTemplate 参数(有则创建RYRedisCache赋给参数) </p>
     *
     * @param redisCache         RY redis缓存
     * @param redisTemplate      redis 模板
     * @param applicationContext 应用程序上下文
     * @return boolean
     */
    @Nullable
    public static RYRedisCache checkRedisSource(RYRedisCache redisCache, RedisTemplate<String, Object> redisTemplate, ApplicationContext applicationContext) {


        BuilderMapChain chain = new BuilderMapChain(3, applicationContext)
                .with(redisTemplate, o -> {
                    ApplicationContext ac = (ApplicationContext) o;
                    return ac.getBean(RedisTemplate.class);
                }).withList(redisCache, Arrays.asList(
                                o -> {
                                    RedisTemplate<String, Object> rt = (RedisTemplate<String, Object>) o;
                                    return new RYRedisCache(rt);
                                },
                                o -> {
                                    ApplicationContext ac = (ApplicationContext) o;
                                    return ac.getBean(RYRedisCache.class);
                                }
                        )
                );

        try {
            Entry<Integer, RYRedisCache> entry = chain.createFrontDfs(3);
            return entry.getValue();
        } catch (Exception e) {
            return null;
        }

    }

}
