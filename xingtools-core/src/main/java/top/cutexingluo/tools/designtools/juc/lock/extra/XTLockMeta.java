package top.cutexingluo.tools.designtools.juc.lock.extra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 锁的元信息
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/10/1 17:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class XTLockMeta {

    /**
     * 锁名称,  仅分布式锁
     */
    protected String name;

    /**
     * 锁类型
     */
    protected XTLockType lockType = XTLockType.NonLock;


    /**
     * 是否公平, 仅本地锁
     */
    protected boolean isFair = false;

    // 由于Lock接口没有带 锁多长时间，所以这个只能适用于尝试获取锁。大于0则尝试获取，-1则直接锁

    /**
     * 尝试获取锁的时长，tryLock() ,默认-1, 单位 second
     * <p>如果为-1则直接阻塞获取锁，即lock()</p>
     */
    protected int tryTimeout = -1;


    public XTLockMeta(XTLockType lockType) {
        this.lockType = lockType;
    }

    public XTLockMeta(XTLockType lockType, boolean isFair) {
        this.lockType = lockType;
        this.isFair = isFair;
    }

    public XTLockMeta(XTLockType lockType, boolean isFair, int tryTimeout) {
        this.lockType = lockType;
        this.isFair = isFair;
        this.tryTimeout = tryTimeout;
    }
}
