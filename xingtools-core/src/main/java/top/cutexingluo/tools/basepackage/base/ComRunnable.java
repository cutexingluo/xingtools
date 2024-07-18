package top.cutexingluo.tools.basepackage.base;

/**
 * 三元整合接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 17:58
 */
@FunctionalInterface
public interface ComRunnable {
    Runnable getRunnable(Runnable now, Runnable before, Runnable after);
}
