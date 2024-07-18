package top.cutexingluo.tools.basepackage.baseimpl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.cutexingluo.tools.basepackage.base.ComRunnable;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 17:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class XTRunnable implements Runnable, ComRunnable {

    private Runnable now, before, after;

    public XTRunnable(Runnable task) {
        this.now = task;
    }

    @Override
    public void run() {
        getRunnable().run();
    }


    public Runnable getRunnable() {
        return () -> {
            if (before != null) before.run();
            if (now != null) now.run();
            if (after != null) after.run();
        };
    }

    @Override
    public Runnable getRunnable(Runnable now, Runnable before, Runnable after) {
        this.before = before;
        this.after = after;
        this.now = now;
        return getRunnable();
    }

    public static Runnable getTryRunnable(Runnable now, Runnable before, Runnable after) {
        return () -> {
            if (before != null) before.run();
            try {
                if (now != null) now.run();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (after != null) after.run();
            }
        };
    }

    /**
     * 获取Runnable
     *
     * @param task       任务
     * @param canRunTask 是否可以运行任务
     * @param inCatch    异常捕获
     * @return {@link Runnable}
     */
    public static Runnable getTryRunnable(Runnable task, Supplier<Boolean> canRunTask, Consumer<Exception> inCatch) {
        return () -> {
            if (canRunTask != null && !canRunTask.get()) {
                return;
            }
            try {
                if (task != null) task.run();
            } catch (Exception e) {
                if (inCatch != null) inCatch.accept(e);
                else throw e;
            }
        };
    }


}
