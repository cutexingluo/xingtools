package top.cutexingluo.tools.utils.ee.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.connection.RedisConnectionCommands;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;


/**
 * QGRedisUtils 简化版本<br>
 *
 * <p>需要导入 spring-data-redis 相关的包 </p>
 *
 * 青戈版本的工具类
 *
 * <p>
 * 1.使用前需要注入 redisTemplate , 或者 开启 redisconfig 配置 <br>
 * 2.有 redisTemplate 后，可自行注入bean, 或者 开启 redisconfig-util 配置
 * </p>
 */
@SuppressWarnings(value = {"unchecked"})
@ConditionalOnBean(RedisTemplate.class)
//@AutoConfigureAfter(RedisTemplate.class) // 多个不知道哪个
//@Component
@Slf4j
public class QGRedisUtils {
    private static RedisTemplate<String, Object> staticRedisTemplate;


    //    @Autowired
//    @Qualifier("xtRedisTemplate")
//    @Resource
//    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    public QGRedisUtils() {
    }

    public QGRedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        QGRedisUtils.staticRedisTemplate = redisTemplate;
    }

//    @Autowired
//    public QGRedisUtils(@Qualifier("xtRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }

    // Springboot启动成功之后会调用这个方法
    @PostConstruct
    public void initRedis() {
        // 初始化设置 静态staticRedisTemplate对象，方便后续操作数据
        if (staticRedisTemplate == null) staticRedisTemplate = redisTemplate;

    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public static <T> void setCacheObject(final String key, final T value) {
        staticRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public static <T> void setCacheObject(final String key, final T value, final long timeout, final TimeUnit timeUnit) {
        staticRedisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> T getCacheObject(final String key) {
        return (T) staticRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public static boolean deleteObject(final String key) {
        return Boolean.TRUE.equals(staticRedisTemplate.delete(key));
    }

    /**
     * 获取单个key的过期时间
     *
     * @param key
     * @return
     */
    public static Long getExpireTime(final String key) {
        return staticRedisTemplate.getExpire(key);
    }

    /**
     * 发送ping命令
     * redis 返回pong
     */
    public static void ping() {
        String res = staticRedisTemplate.execute(RedisConnectionCommands::ping);
        log.info("Redis ping ==== {}", res);
    }

}
