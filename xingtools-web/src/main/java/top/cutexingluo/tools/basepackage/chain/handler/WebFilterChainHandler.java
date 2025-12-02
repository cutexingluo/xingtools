package top.cutexingluo.tools.basepackage.chain.handler;


import top.cutexingluo.core.basepackage.chain.base.FilterChainProcessor;
import top.cutexingluo.tools.bridge.servlet.HttpServletBundle;

/**
 * web http servlet 过滤器链处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/6 15:42
 * @see org.springframework.web.filter.GenericFilterBean
 * @see org.springframework.web.filter.OncePerRequestFilter
 * @since 1.1.4
 */
public interface WebFilterChainHandler extends FilterChainProcessor<HttpServletBundle> {
}
