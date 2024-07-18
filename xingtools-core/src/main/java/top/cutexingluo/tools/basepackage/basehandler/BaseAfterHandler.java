package top.cutexingluo.tools.basepackage.basehandler;

/**
 * 后处理程序
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/27 9:18
 */
@FunctionalInterface
public interface BaseAfterHandler {
    Object handleAfter() throws Exception;
}
