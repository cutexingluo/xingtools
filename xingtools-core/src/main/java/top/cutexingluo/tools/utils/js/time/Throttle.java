package top.cutexingluo.tools.utils.js.time;

import lombok.Data;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 节流类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/10 21:36
 */
@Data
//@SuppressWarnings("all")
public class Throttle {
    private ScheduledExecutorService timer;
    private Long delay;
    private boolean needWait = false;

    @SuppressWarnings("all")
    public Throttle(Long delay) {
        this.delay = delay;
        this.timer = Executors.newSingleThreadScheduledExecutor();
    }

    public Throttle(Long delay, ScheduledExecutorService timer) {
        this.delay = delay;
        this.timer = timer;
    }

    public static Throttle create(Long delay) {
        return new Throttle(delay);
    }

    /**
     * 不稳定版
     */
    public void run(Runnable runnable) {
        if (!needWait) {
            needWait = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    needWait = false;
                    runnable.run();
                }
            }, delay, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 稳定版
     */
    public void runFixedInterval(Runnable runnable) {
        if (!needWait) {
            needWait = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runnable.run();
                    needWait = false;
                }
            }, delay, TimeUnit.MILLISECONDS);
        }
    }
}
