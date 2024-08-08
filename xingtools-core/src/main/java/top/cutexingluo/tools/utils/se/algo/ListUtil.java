package top.cutexingluo.tools.utils.se.algo;

import java.util.ArrayList;
import java.util.List;

/**
 * list 辅助工具类
 *
 * <p>已过时，未来并入 {@link top.cutexingluo.tools.utils.se.collection.XTListUtil}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/15 17:24
 */
@Deprecated
public class ListUtil extends XTFindUtil {


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
}
