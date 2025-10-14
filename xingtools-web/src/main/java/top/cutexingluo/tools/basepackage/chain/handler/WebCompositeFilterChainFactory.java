package top.cutexingluo.tools.basepackage.chain.handler;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.core.basepackage.chain.base.AroundChain;
import top.cutexingluo.core.basepackage.chain.base.FilterChainProcessor;
import top.cutexingluo.core.basepackage.chain.core.FilterChain;
import top.cutexingluo.tools.bridge.servlet.HttpServletBundle;

import java.util.ArrayList;
import java.util.List;

/**
 * web http servlet 复合过滤器链处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 17:25
 * @see org.springframework.web.filter.CompositeFilter
 * @since 1.1.4
 */
@Data
public class WebCompositeFilterChainFactory implements FilterChainProcessor<HttpServletBundle> {

    /**
     * 过滤器链列表
     */
    @NotNull
    protected List<FilterChainProcessor<HttpServletBundle>> filters;

    public WebCompositeFilterChainFactory() {
        filters = new ArrayList<>();
    }

    public WebCompositeFilterChainFactory(@NotNull List<FilterChainProcessor<HttpServletBundle>> filters) {
        this.filters = filters;
    }

    @Override
    public void doFilter(HttpServletBundle source, AroundChain<? super HttpServletBundle> chain) {
        new FilterChain<>(chain, this.filters).doFilter(source);
    }
}
