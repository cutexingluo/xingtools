package top.cutexingluo.tools.utils.ee.web.request;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import top.cutexingluo.tools.basepackage.function.TriFunction;
import top.cutexingluo.tools.bridge.servlet.adapter.HttpServletRequestAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HttpServletRequest 处理工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/4 11:53
 * @since 1.1.6
 */
public class XTRequestUtil {


    /**
     * 获取请求参数
     *
     * @param requestAdapter 请求适配器
     * @return 请求参数
     */
    @NotNull
    public static LinkedMultiValueMap<String, String> getParameters(@NotNull HttpServletRequestAdapter requestAdapter) {
        Map<String, String[]> parameterMap = requestAdapter.getParameterMap();
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

    /**
     * 获取请求参数(去重)
     *
     * @param requestAdapter  请求适配器
     * @param isDuplicateFunc 重复值处理  params: (key, map (old value), new value) => set value
     * @return 请求参数
     */
    @NotNull
    public static LinkedHashMap<String, String> getParametersDistinct(@NotNull HttpServletRequestAdapter requestAdapter,
                                                                      @Nullable TriFunction<String, LinkedHashMap<String, String>, String, String> isDuplicateFunc) {
        Map<String, String[]> parameterMap = requestAdapter.getParameterMap();
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            if (values == null) return;
            if (values.length > 0) {
                for (String value : values) {
                    if (parameters.containsKey(key)) {
                        if (isDuplicateFunc != null) {
                            parameters.put(key, isDuplicateFunc.apply(key, parameters, value));
                        }
                    } else {
                        parameters.put(key, value);
                    }
                }
            }
        });
        return parameters;
    }

    /**
     * 获取请求参数(第一个)
     *
     * @param requestAdapter 请求适配器
     * @return 请求参数
     */
    @NotNull
    public static LinkedHashMap<String, String> getParametersFirst(@NotNull HttpServletRequestAdapter requestAdapter) {
        Map<String, String[]> parameterMap = requestAdapter.getParameterMap();
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            if (values != null && values.length > 0) {
                parameters.put(key, values[0]);
            }
        });
        return parameters;
    }
}
