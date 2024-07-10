package top.cutexingluo.tools.designtools.juc.lock;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * XTLock 锁扩展类 ，继承锁类
 * <p>
 * 自带消费者锁和生产者锁
 *
 * @author XingTian
 * @version 1.0
 * @date 2022-11-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class XTLockConAndSup extends XTLock {
    //即被condition的XTLock对象，SyncThread调用或者被XTLock加锁封装
    //加锁后的消费者 可执行接口 需要用同一把锁,
    public Runnable consumerLock(Runnable runnableTask) {
        Runnable around = () -> {
            try {
                while (productNumber == 0) {
                    condition.await();
                }
                productNumber--;
                runnableTask.run();
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        return XTLockUtils.runnableLock(around);
    }

    //加锁后的消费者 可执行接口 需要用同一把锁
    public Runnable supplierLock(Runnable runnableTask) {
        Runnable around = () -> {
            try {
                while (productNumber != 0) {
                    condition.await();
                }
                productNumber++;
                runnableTask.run();
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        return XTLockUtils.runnableLock(around);
    }
}
