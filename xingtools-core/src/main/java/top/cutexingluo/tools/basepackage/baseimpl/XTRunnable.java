package top.cutexingluo.tools.basepackage.baseimpl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.cutexingluo.tools.basepackage.base.ComRunnable;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 17:56
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class XTRunnable extends XTAround implements Runnable, ComRunnable {

    protected Runnable now, before, after;

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

    /**
     * <p>v1.1.5 重新抛出异常</p>
     */
    public static Runnable getTryRunnable(Runnable now, Runnable before, Runnable after) {
        return () -> {
            if (before != null) before.run();
            try {
                if (now != null) now.run();
            } catch (Exception e) {
                throw e;
            } finally {
                if (after != null) after.run();
            }
        };
    }


}
