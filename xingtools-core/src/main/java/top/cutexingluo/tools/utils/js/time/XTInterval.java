package top.cutexingluo.tools.utils.js.time;

import lombok.Data;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 计时器类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/10 20:48
 */
@Data
public class XTInterval {
    private ScheduledExecutorService scheduler;
    private final Long startDelay;
    private final Long delay;
    //    private XTThreadPool xtThreadPool;
    private int core = 5;

    public XTInterval(Long delay) {
        this(delay, 0L);
    }

    public XTInterval(Long delay, Long startDelay) {
        this.delay = delay;
        this.startDelay = startDelay;
    }

    public XTInterval(Long delay, ScheduledExecutorService service) {
        this(delay, 0L, service);
    }

    public XTInterval(Long delay, int scheduledThreadPoolCore) {
        this(delay, 0L, scheduledThreadPoolCore);
    }

    public XTInterval(Long delay, Long startDelay, ScheduledExecutorService service) {
        this.delay = delay;
        this.startDelay = startDelay;
        this.scheduler = service;
    }

    public XTInterval(Long delay, Long startDelay, int scheduledThreadPoolCore) {
        this.delay = delay;
        this.startDelay = startDelay;
        this.core = scheduledThreadPoolCore;
    }

    /**
     * @param delay 执行时间
     * @return 初始化 Debounce对象
     */
    public static XTInterval create(Long delay) {
        return new XTInterval(delay);
    }

    public static XTInterval create(Long delay, Long startDelay) {
        return new XTInterval(delay, startDelay);
    }

    public static XTInterval create(Long delay, Long startDelay, ScheduledExecutorService service) {
        return new XTInterval(delay, startDelay, service);
    }

    public static XTInterval create(Long delay, Long startDelay, int scheduledThreadPoolCore) {
        return new XTInterval(delay, startDelay, scheduledThreadPoolCore);
    }

    public static XTInterval create(Long delay, ScheduledExecutorService service) {
        return new XTInterval(delay, service);
    }

    public static XTInterval create(Long delay, int scheduledThreadPoolCore) {
        return new XTInterval(delay, scheduledThreadPoolCore);
    }

    @SuppressWarnings("all")
    protected void initProperty() {
        if (scheduler == null) {
            scheduler = Executors.newScheduledThreadPool(core);
        }
    }

    public void setInterval(Runnable runnable) {
        initProperty();
        scheduler.scheduleWithFixedDelay(runnable, startDelay, delay, TimeUnit.MILLISECONDS);
    }

    public void clearInterval() {
        if (scheduler != null && !scheduler.isShutdown()) scheduler.shutdown();
    }
}
