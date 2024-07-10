package top.cutexingluo.tools.basepackage.basehandler;

/**
 * 前处理程序
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/27 9:17
 */
@FunctionalInterface
public interface BaseBeforeHandler {
    Object handleBefore() throws Exception;
}
