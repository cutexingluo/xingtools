package top.cutexingluo.tools.basepackage.chain.base;

/**
 * 围绕执行链处理器 接口
 * <p>可以实现该接口使用</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 10:47
 * @since 1.1.4
 */
@FunctionalInterface
public interface FilterChainProcessor<T> {

    /**
     * 执行过滤器操作方法
     */
    void doFilter(T source, AroundChain<? super T> chain);
}
