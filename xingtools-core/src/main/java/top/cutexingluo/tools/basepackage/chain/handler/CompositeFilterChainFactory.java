package top.cutexingluo.tools.basepackage.chain.handler;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.chain.base.AroundChain;
import top.cutexingluo.tools.basepackage.chain.base.FilterChainProcessor;
import top.cutexingluo.tools.basepackage.chain.core.FilterChain;

import java.util.ArrayList;
import java.util.List;

/**
 * 复合过滤器链处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 17:18
 * @since 1.1.4
 */
@Data
public class CompositeFilterChainFactory<T> implements FilterChainProcessor<T> {

    /**
     * 过滤器链列表
     */
    @NotNull
    protected List<FilterChainProcessor<T>> filters;

    public CompositeFilterChainFactory() {
        filters = new ArrayList<>();
    }

    public CompositeFilterChainFactory(@NotNull List<FilterChainProcessor<T>> filters) {
        this.filters = filters;
    }

    /**
     * 核心方法，执行该方法，完成过滤器链的处理
     *
     * @param source 源数据
     * @param chain  初始过滤器链, (默认会作为最后一个进行处理)
     */
    @Override
    public void doFilter(T source, AroundChain<? super T> chain) {
        new FilterChain<>(chain, this.filters).doFilter(source);
    }
}
