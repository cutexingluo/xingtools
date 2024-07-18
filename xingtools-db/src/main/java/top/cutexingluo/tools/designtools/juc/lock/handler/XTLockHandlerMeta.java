package top.cutexingluo.tools.designtools.juc.lock.handler;

import lombok.Data;
import lombok.experimental.Accessors;
import org.redisson.api.RedissonClient;
import top.cutexingluo.tools.designtools.juc.lock.extra.XTLockMeta;


/**
 * LockHandler 元数据
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/17 15:27
 * @since 1.0.2
 */
@Data
@Accessors(chain = true)
public class XTLockHandlerMeta {

    private XTLockMeta lockMeta;
    private RedissonClient redissonClient;
    private boolean useRedissonClient = false;

    /**
     * 使用 RedissonClient
     */
    public XTLockHandlerMeta(XTLockMeta lockMeta, RedissonClient redissonClient,boolean useRedissonClient) {
        this.lockMeta = lockMeta;
        this.useRedissonClient = useRedissonClient;
        this.redissonClient = redissonClient;
    }

    /**
     * 使用 RedissonClient
     */
    public XTLockHandlerMeta(XTLockMeta lockMeta, RedissonClient redissonClient) {
        this(lockMeta, redissonClient,true );
    }

    /**
     * 不使用 RedissonClient
     */
    public XTLockHandlerMeta(XTLockMeta lockMeta) {
        this.lockMeta = lockMeta;
    }

    /**
     * 自行决定
     */
    public XTLockHandlerMeta(XTLockMeta lockMeta, boolean useRedissonClient) {
        this(lockMeta, null,useRedissonClient) ;
    }
}
