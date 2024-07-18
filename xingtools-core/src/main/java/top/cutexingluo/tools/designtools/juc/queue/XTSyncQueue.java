package top.cutexingluo.tools.designtools.juc.queue;

import java.util.concurrent.SynchronousQueue;

/**
 * 无容量阻塞队列
 *
 * @author XingTian
 */
public class XTSyncQueue<T> extends SynchronousQueue<T> {
    public void putOne(T item) throws InterruptedException {
        super.put(item);
    }

    public T takeOne() throws InterruptedException {
        return super.take();
    }

    public T getOne() throws InterruptedException {
        return super.take();
    }
}
