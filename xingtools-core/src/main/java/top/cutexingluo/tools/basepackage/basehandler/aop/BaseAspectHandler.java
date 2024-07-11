package top.cutexingluo.tools.basepackage.basehandler.aop;

/**
 * Aop before after 接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/16 14:55
 */
public interface BaseAspectHandler<T> {

    void before(T t) throws Exception;

    void after(T t) throws Exception;
}
