package top.cutexingluo.tools.utils.ee.redisson;

import com.baomidou.lock.executor.AbstractLockExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * redisson 重入锁执行器<br>
 * 可以用 RedissonLockExecutor 代替
 *
 * <p>需要导入 org.redisson:redisson 包</p>
 * <p>需要导入 com.baomidou:lock4j-spring-boot-starter 包 或者 com.baomidou:lock4j-core 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 18:10
 */
@ConditionalOnClass({AbstractLockExecutor.class, RedissonClient.class})
@Slf4j
@RequiredArgsConstructor
public class XTRedissonExecutor extends AbstractLockExecutor<RLock> {

    private final RedissonClient redissonClient;

    @Override
    public boolean renewal() {
        return true;
    }

    @Override
    public RLock acquire(String lockKey, String lockValue, long expire, long acquireTimeout) {
        try {
            final RLock lockInstance = redissonClient.getLock(lockKey);
            final boolean locked = lockInstance.tryLock(acquireTimeout, expire, TimeUnit.MILLISECONDS);
            return obtainLockInstance(locked, lockInstance);
        } catch (InterruptedException e) {
            return null;
        }
    }

    @Override
    public boolean releaseLock(String key, String value, RLock lockInstance) {
        if (lockInstance.isHeldByCurrentThread()) {
            try {
                return lockInstance.forceUnlockAsync().get();
            } catch (ExecutionException | InterruptedException e) {
                return false;
            }
        }
        return false;
    }
}
