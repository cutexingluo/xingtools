package top.cutexingluo.tools.utils.se.time;


import top.cutexingluo.tools.designtools.juc.utils.XTJUC;

/**
 * 简易Time工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/2 21:55
 */
public class XTTime {
    public static void sleepBySecond(long seconds) {
        XTJUC.sleepBySecond(seconds);
    }

    public static void sleepMillis(long millis) {
        XTJUC.sleepMillis(millis);
    }

    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static double clock() {
        return currentTimeMillis() / 1000.0;
    }
}
