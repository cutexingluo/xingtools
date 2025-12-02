package top.cutexingluo.tools.utils.ee.redis;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import top.cutexingluo.core.basepackage.baseimpl.XTRunCallUtil;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Redis 锁 脚本工具类
 *
 * <p>需要导入 spring-data-redis 相关的包 </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/3 21:38
 */


//@Component
@Slf4j
public class RedisLockUtil {
    // 原子操作
    public static final String LUA_SCRIPT =
            "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

    public RedisLockUtil(RedisTemplate redisTemplate) {
        init(redisTemplate);
    }

    //    @Autowired
//    @PostConstruct
    public void init(RedisTemplate redisTemplate) {
        RedisLockUtil.redisTemplate = redisTemplate;
//        if (RedisLockUtil.redisTemplate != null) log.info("redisTemplate autoInjected success !");
    }

    private static RedisTemplate redisTemplate;

    /**
     * 利用脚本执行redis加解锁操作
     *
     * @param key   键
     * @param value 值
     */
    public static void executeUnlock(String key, String value) {
        redisTemplate.execute(new DefaultRedisScript<Long>(LUA_SCRIPT, Long.class),
                Arrays.asList(key), value);
    }

    // 加锁执行

    /**
     * 利用脚本执行加解锁操作，设置自旋次数
     *
     * @param callTask 执行任务
     * @param count    自旋次数，需大于0
     */
    public static <T> T runWithLock(Callable<T> callTask, int count) throws Exception {
        String uuid = UUID.randomUUID().toString();
        Boolean isLock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (Boolean.TRUE.equals(isLock)) {
            // 执行任务，返回执行的结果
            return XTRunCallUtil.getTryCallable(callTask, null, () -> executeUnlock("lock", uuid)).call();
        } else {
            // 自旋
            return count > 0 ? runWithLock(callTask, count - 1) :
                    count == -1 ? runWithLock(callTask, count) : null;
        }
    }
}
