package top.cutexingluo.tools.utils.ee.redisson;

import com.baomidou.lock.DefaultLockKeyBuilder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;

/**
 * 自定义分布式锁key生成规则
 * 可以用 DefaultLockKeyBuilder 代替，然后自行配置Bean
 *
 * <p>需要导入 com.baomidou:lock4j-spring-boot-starter 包 或者 com.baomidou:lock4j-core 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 18:30
 */
@ConditionalOnClass(DefaultLockKeyBuilder.class)
@Component
public class CustomLockKeyBuilder extends DefaultLockKeyBuilder {

    public CustomLockKeyBuilder(BeanFactory beanFactory) {
        super(beanFactory);
    }
}
