package top.cutexingluo.tools.designtools.juc.utils;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 休眠线程
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/6 20:56
 */
public class XTJUC {

    public static boolean printTrace = true;
    public static Consumer<Exception> exceptionHandler = null;

    public static int getCoresNumber() {
        return Runtime.getRuntime().availableProcessors();
    }

    //    @XTException(name = "XTJUCUtils sleepMillis error", desc = "可能为中断错误")
    public static void sleepMillis(long millis) {//毫秒
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            if (exceptionHandler != null) exceptionHandler.accept(e);
            else if (printTrace) e.printStackTrace();
        }
    }

    //    @XTException(name = "XTJUCUtils sleepBySecond error", desc = "可能为中断错误")
    public static void sleepBySecond(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds); //基于Thread
        } catch (InterruptedException e) {
            if (exceptionHandler != null) exceptionHandler.accept(e);
            else if (printTrace) e.printStackTrace();
        }
    }
}
