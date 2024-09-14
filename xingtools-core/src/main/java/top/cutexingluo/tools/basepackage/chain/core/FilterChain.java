package top.cutexingluo.tools.basepackage.chain.core;


import top.cutexingluo.tools.basepackage.chain.base.AroundChain;
import top.cutexingluo.tools.basepackage.chain.base.FilterChainProcessor;

import java.util.List;

/**
 * 过滤器链
 *
 * <p>过滤链的处理类</p>
 * <p>可以使用 {@link top.cutexingluo.tools.basepackage.chain.handler.CompositeFilterChainFactory}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 16:14
 * @since 1.1.4
 */
public class FilterChain<T> implements AroundChain<T> {

    protected final AroundChain<? super T> originalChain;
    protected final List<FilterChainProcessor<T>> additionalFilters;

    protected int currentPosition = 0;

    public FilterChain(AroundChain<? super T> originalChain, List<FilterChainProcessor<T>> additionalFilters) {
        this.originalChain = originalChain;
        this.additionalFilters = additionalFilters;
    }

    @Override
    public void doFilter(T source) {
        if (this.currentPosition == this.additionalFilters.size()) {
            this.originalChain.doFilter(source);
        } else {
            this.currentPosition++;
            FilterChainProcessor<T> nextFilter = this.additionalFilters.get(this.currentPosition - 1);
            nextFilter.doFilter(source, this);
        }
    }
}
