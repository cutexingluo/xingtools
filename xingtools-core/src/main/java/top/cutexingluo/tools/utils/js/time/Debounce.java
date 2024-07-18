package top.cutexingluo.tools.utils.js.time;

import lombok.Data;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 防抖类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/10 21:34
 */
@Data
public class Debounce {
    private ScheduledExecutorService timer;
    private Long delay;

    @SuppressWarnings("all")
    public Debounce(Long delay) {
        this.delay = delay;
        this.timer = Executors.newSingleThreadScheduledExecutor();
    }

    public static Debounce create(Long delay) {
        return new Debounce(delay);
    }

    public void run(Runnable runnable) {
        if (timer != null) {
            if (!timer.isShutdown()) {
                timer.shutdown();
            }
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    timer = null;
                    runnable.run();
                }
            }, delay, TimeUnit.MILLISECONDS);
        }
    }
}
