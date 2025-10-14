package top.cutexingluo.tools.utils.ee.web.spring;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;
import top.cutexingluo.core.common.data.TupleEntry;

import java.util.LinkedList;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Spring Web 工具类
 *
 * <p>需要注册 bean 使用</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/12 11:19
 * @since 1.1.6
 */
public class SpringWebHandler {

    @Autowired
    ApplicationContext applicationContext;

    /**
     * 获取所有 HandlerMethod
     */
    Map<RequestMappingInfo, HandlerMethod> getHandlerMethodMap() {
        RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        return handlerMapping.getHandlerMethods();
    }

    /**
     * 获取条件过滤的HandlerMethod 的 pattern list (url)
     *
     * @param filter 条件过滤器 , true 通过,  null 全部通过
     * @return pattern list (url)
     */
    LinkedList<String> getHandlerMethodUrlList(@NotNull Map<RequestMappingInfo, HandlerMethod> handlerMethodMap, @Nullable Predicate<TupleEntry<RequestMappingInfo, HandlerMethod>> filter) {
        LinkedList<String> urlList = new LinkedList<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            // 组成可变变量
            TupleEntry<RequestMappingInfo, HandlerMethod> tupleEntry = new TupleEntry<>(entry.getKey(), entry.getValue());
            if (filter != null && !filter.test(tupleEntry)) { // 过滤器
                continue;
            }
            PathPatternsRequestCondition pathPatternsCondition = tupleEntry.getKey().getPathPatternsCondition();
            // get the patterns
            if (pathPatternsCondition != null && !pathPatternsCondition.isEmptyPathMapping()) {
                for (PathPattern pattern : pathPatternsCondition.getPatterns()) {
                    urlList.add(pattern.getPatternString());
                }
            }
        }
        return urlList;
    }

    /**
     * 获取条件过滤的HandlerMethod 的 pattern list (url)\
     *
     * @return pattern list (url)
     */
    LinkedList<String> getHandlerMethodUrlList(@NotNull Map<RequestMappingInfo, HandlerMethod> handlerMethodMap) {
        return getHandlerMethodUrlList(handlerMethodMap, null);
    }

}
