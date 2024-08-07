package top.cutexingluo.tools.utils.se.collection;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.se.algo.XTFindUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * List 集合相关工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/8/7 10:10
 * @since 1.1.2
 */
public class XTListUtil extends XTFindUtil {

    /**
     * 转换List
     *
     * @param obj   obj
     * @param clazz clazz
     * @return {@link List <T>}
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return result;
    }


    /**
     * List 添加值
     * <p>list 为null时使用nullDefaultSupplier 默认list 并添加</p>
     *
     * @param list list 集合
     * @return {@link List <T>} 装有该元素的值
     * @throws NullPointerException list 为null时且nullDefaultSupplier 为null时抛出
     */
    @Contract("null, _, null -> fail")
    public static <T> @NotNull List<T> checkAdd(List<T> list, T value, Supplier<? extends List<T>> nullDefaultSupplier) {
        List<T> checkList = XTCollUtil.defaultIfEmptyCheck(list, nullDefaultSupplier);
        list.add(value);
        return list;
    }

}
