package top.cutexingluo.tools.basepackage.base;

/**
 * 二元执行接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 17:43
 */
@FunctionalInterface
public interface BasePairRunnable {
    void run(Runnable before, Runnable after);
}

