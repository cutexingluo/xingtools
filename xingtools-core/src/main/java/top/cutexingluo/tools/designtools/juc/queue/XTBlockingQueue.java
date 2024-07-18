package top.cutexingluo.tools.designtools.juc.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
/**
 * 数组阻塞队列
 *
 * @author XingTian
 * @date 2023/2/2 20:50
 * @version 1.0.0
 */
public class XTBlockingQueue<T> extends ArrayBlockingQueue<T> {
    protected long time = 2;

    public XTBlockingQueue(int capacity) {
        super(capacity);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean addOneMills(T t) throws InterruptedException {
        return super.offer(t, time, TimeUnit.SECONDS);
    }

    public T pollOneMills() throws InterruptedException {
        return super.poll(time, TimeUnit.SECONDS);
    }
}
