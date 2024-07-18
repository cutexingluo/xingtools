package top.cutexingluo.tools.utils.ee.redisson;

import com.baomidou.lock.LockFailureStrategy;
import com.baomidou.lock.exception.LockFailureException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 自定义抢占锁失败执行策略<br>
 * 可以用DefaultLockFailureStrategy代替，然后自行配置Bean
 * <p>需要导入 com.baomidou:lock4j-spring-boot-starter 包 或者 com.baomidou:lock4j-core 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 18:23
 */
@ConditionalOnClass(LockFailureStrategy.class)
@Component
public class GrabLockFailureStrategy implements LockFailureStrategy {

    @Override
    public void onLockFailure(String key, Method method, Object[] arguments) {
        String format = String.format("key: %s, method: %s , arguments: %s", key, method, arguments);
        throw new LockFailureException("获取锁失败  --> " + format);
    }
}
