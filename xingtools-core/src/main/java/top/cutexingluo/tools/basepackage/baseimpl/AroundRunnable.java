package top.cutexingluo.tools.basepackage.baseimpl;

import top.cutexingluo.tools.basepackage.base.BaseAroundRunnable;

/**
 * 三元执行类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 17:45
 */

public class AroundRunnable implements BaseAroundRunnable, Runnable {

    private Runnable now, before, after;

    public AroundRunnable(Runnable now, Runnable before, Runnable after) {
        this.now = now;
        this.before = before;
        this.after = after;
    }

    public AroundRunnable() {
    }

    @Override
    public void run(Runnable now, Runnable before, Runnable after) {
        if (before != null) before.run();
        if (now != null) now.run();
        if (after != null) after.run();
    }

    @Override
    public void run() {
        this.run(this.now, this.before, this.after);
    }
}
