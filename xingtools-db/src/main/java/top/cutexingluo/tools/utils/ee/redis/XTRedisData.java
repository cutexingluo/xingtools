package top.cutexingluo.tools.utils.ee.redis;


import lombok.Data;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import top.cutexingluo.core.designtools.juc.thread.XTThreadPool;


/**
 * RedisData 元数据
 * <p>需要导入 spring-data-redis 相关的包 </p>
 * <p>需要导入 org.redisson:redisson 包</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/4 16:12
 */
@ConditionalOnBean({RedisUtil.class, RedissonClient.class})
//@AutoConfigureAfter(RedisTemplate.class)
//@Import(XTRedisUtil.class)
//@Component
@Data
public class XTRedisData {
    // Redis 操作工具
//    @Autowired
    private RedisUtil redisUtil;
    // 线程池
//    @Autowired(required = false)
    @Nullable
    private XTThreadPool xtThreadPool;
    // 分布式锁
//    @Autowired
    private RedissonClient redisson;

    public XTRedisData(RedisUtil redisUtil, @NonNull XTThreadPool threadPool, RedissonClient client) {
        this.redisUtil = redisUtil;
        this.xtThreadPool = threadPool;
        this.redisson = client;
    }

    public XTRedisData(RedisUtil redisUtil, RedissonClient client) {
        this.redisUtil = redisUtil;
        this.xtThreadPool = null;
        this.redisson = client;
    }
}
