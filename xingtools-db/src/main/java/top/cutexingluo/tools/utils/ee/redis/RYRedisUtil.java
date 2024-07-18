package top.cutexingluo.tools.utils.ee.redis;

import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import top.cutexingluo.tools.designtools.builder.XTBuilder;

import java.util.concurrent.Callable;

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
    public static boolean checkRedisSource(RYRedisCache redisCache, RedisTemplate<String, Object> redisTemplate, ApplicationContext applicationContext) {
        if (redisCache == null) {
            if (redisTemplate == null) {
                if (applicationContext == null) {
                    return false;
                }
                try {
                    RYRedisCache rc = applicationContext.getBean(RYRedisCache.class);
                    if (rc == null) {
                        RedisTemplate rt = applicationContext.getBean(RedisTemplate.class);
                        if (rt == null) {
                            return false;
                        }
                        redisTemplate = rt;
                    }
                    redisCache = rc;
                } catch (Exception e) {
                    return false;
                }
            }
            if (redisTemplate != null) redisCache = new RYRedisCache(redisTemplate);
        }
        return true;
    }


    private static Builder lastBuilder;

    public static Builder builder(RYRedisCache redisCache) {
        lastBuilder = new Builder(redisCache);
        return lastBuilder;
    }

    public static Builder getLastBuilder() {
        return lastBuilder;
    }


    public static class Builder extends XTBuilder<RYRedisCache> {
        Builder(RYRedisCache redisCache) {
            this.target = redisCache;
        }

        /**
         * 如果没有得到缓存对象
         * 组合操作类
         * <ul>
         *     <li>
         *         如果key存在，则获得key
         *     </li>
         *     <li>
         *         如果key不存在，则执行任务，结果存入redis，并返回对象
         *     </li>
         * </ul>
         *
         * @param key             关键
         * @param ifAbsentSetTask 如果没有对象则执行的任务
         * @param nullable        任务结果为空 是否 可以存入redis
         * @return {@link T}
         * @throws Exception 异常
         */
        public <T> T getCacheObjectIfAbsentSet(String key, Callable<T> ifAbsentSetTask, boolean nullable) throws Exception {
            T tar = target.getCacheObject(key);
            if (tar == null) {
                tar = ifAbsentSetTask.call();
                if (nullable || tar != null) target.setCacheObject(key, tar);
            }
            return tar;
        }

        /**
         * 如果没有得到缓存对象
         * 组合操作类
         * <ul>
         *     <li>
         *         如果key存在，则获得对象
         *     </li>
         *     <li>
         *         如果key不存在，则执行任务，结果非空存入redis，并返回对象
         *     </li>
         * </ul>
         *
         * @param key             关键
         * @param ifAbsentSetTask 如果没有对象则执行的任务
         * @return {@link T}
         * @throws Exception 异常
         */
        public <T> T getCacheObjectIfAbsentSet(String key, Callable<T> ifAbsentSetTask) throws Exception {
            return getCacheObjectIfAbsentSet(key, ifAbsentSetTask, false);
        }

        /**
         * 组合操作类 (确保无异常)
         * <ul>
         *     <li>
         *         如果key存在，则获得对象
         *     </li>
         *     <li>
         *         如果key不存在，则执行任务，结果存入redis，并返回对象
         *     </li>
         * </ul>
         *
         * @param key             关键
         * @param ifAbsentSetTask 如果没有对象则执行的任务
         * @return {@link T}
         */
        public <T> T getCacheObjectIfAbsentSetCheck(String key, Callable<T> ifAbsentSetTask) {
            try {
                return getCacheObjectIfAbsentSet(key, ifAbsentSetTask);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


    }
}
