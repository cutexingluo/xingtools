package top.cutexingluo.tools.utils.ee.redis;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import top.cutexingluo.tools.designtools.juc.thread.XTThreadPool;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * XTRedis工具类
 * <p>需要导入 spring-data-redis 相关的包 </p>
 * <p>
 * 1.使用前需要注入 redisTemplate , 或者 开启 redisconfig 配置 <br>
 * 2.有 redisTemplate 后，可自行注入bean, 或者 开启 redisconfig-util 配置
 * </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/4 16:07
 * @since 2022-12-14 <br>
 *
 * 在java层面实现Redis高性能为目标
 *
 * 里面很多方法，可实现异步操作
 */


@ConditionalOnBean(XTRedisData.class)
//@AutoConfigureAfter(XTRedisData.class)
//@Component
@AllArgsConstructor
@Data
//@Slf4j
public class XTRedisUtil {
    //*******************common**************
    @Autowired
    private XTRedisData xtRedisData;

    public static XTThreadPool xtThreadPool;
    public static RedisUtil redisUtil;
    public static ThreadPoolExecutor threadPoolExecutor;
    public static RedissonClient redisson;

    //    @PostConstruct
    public void init() {
        redisUtil = xtRedisData.getRedisUtil();
        xtThreadPool = xtRedisData.getXtThreadPool();
        if (xtThreadPool != null) threadPoolExecutor = xtThreadPool.getThreadPool();
        redisson = xtRedisData.getRedisson();
    }

    public XTRedisUtil refreshThreadPool() {
        if (xtThreadPool != null) threadPoolExecutor = xtThreadPool.getThreadPool();
        return this;
    }

    //******************外部使用**********

    /**
     * 已做key非空判断, 并且如果不存在该key 则不设置
     * <br>timeout小于0 永久设置
     */
    public static Object safeSet(String key, Object value, long timeout) {
        if (StrUtil.isBlank(key) || !redisUtil.hasKey(key)) return null;
        return set(key, value, timeout);
    }

    /**
     * 已做key非空判断,并且如果不存在该key则不设置
     */
    public static Object safeSet(String key, Object value) {
        if (StrUtil.isBlank(key) || !redisUtil.hasKey(key)) return null;
        return set(key, value, -1);
    }

    /**
     * 已做key非空判断, 并且如果不存在该key, 都返回空
     */
    public static Object safeGet(String key) {
        if (StrUtil.isBlank(key) || !redisUtil.hasKey(key)) return null;
        return get(key);
    }

    //*******************内部使用*********
    //外部已检测，排除安全

    /**
     * 未做安全判断, timeout 小于 0 永久设置
     */
    public static boolean set(String key, Object value, long timeout) {
//        if (StrUtil.isBlank(key) || value == null) return false;
        if (timeout < 0) return redisUtil.set(key, value);
        return redisUtil.set(key, value, timeout);
    }

    /**
     * 未做安全判断, 永久设置
     */
    public static Object set(String key, Object value) {
        return set(key, value, -1);
    }

    /**
     * 未做安全判断, 获取key对应的值
     */
    protected static Object get(String key) {
//        if (StrUtil.isBlank(key) || !redisUtil.hasKey(key)) return null;
        return redisUtil.get(key);
    }

    //******************************和SpringCache注解同步
    // 要开启SpringCache
    //@EnableCaching  //开启缓存
    // @CacheConfig 自行配置
//    @Deprecated

    /**
     * 永久设置,执行成功返回true
     *
     * @param key       要设置的键
     * @param value     要设置的值
     * @param beforePut 设置之前的动作,如果为空则不设置
     */
    public static boolean cachePut(String key, Object value, Runnable beforePut) {
        return cachePut(key, value, -1, beforePut);
    }

    /**
     * 执行成功返回true
     *
     * @param key       要设置的键
     * @param value     要设置的值
     * @param timeout   设置的时间 timeout<0 永久设置
     * @param beforeSet 设置之前的动作,如果为空则不设置
     */
    public static boolean cachePut(String key, Object value, int timeout, Runnable beforeSet) {
        if (beforeSet == null) return false;
        beforeSet.run();
        set(key, value, timeout);
        return true;
    }

    /**
     * 执行成功返回true
     *
     * @param key       要删除的键
     * @param beforeDel 删除之前的动作,如果为空则不删除
     */
    public static boolean cacheEvict(String key, Runnable beforeDel) {
        if (beforeDel == null) return false;
        beforeDel.run();
        redisUtil.del(key);
        return true;
    }
}
