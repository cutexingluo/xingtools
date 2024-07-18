package top.cutexingluo.tools.common;

import top.cutexingluo.tools.utils.se.array.XTArrayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IResult 公用方法
 * <p>v1.0.3 : 推荐用作函数式接口</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/13 21:18
 */
@FunctionalInterface
public interface ICommonResult<T> {

    T getData();

    public static int intCode(String code) {
        return Integer.parseInt(code);
    }

    public static String strCode(int code) {
        return String.valueOf(code);
    }

    /**
     * @param pairs Add the element you want to add,a pair of (key,value), odd totals. 数据对，最好是偶数
     * @return Result:
     */
    default Map<Object, Object> put(Object... pairs) {
        Map<Object, Object> map;
        T data = getData();
        if (!(data instanceof Map)) map = new HashMap<>();
        else map = (Map<Object, Object>) data;
        XTArrayUtil.putMapFromDValues(map, pairs);
        return map;
    }

    /**
     * @param items ; Add the element you want to add
     * @return Result ; Contains list
     */
    default List<Object> add(Object... items) {
        List<Object> list;
        T data = getData();
        if (!(data instanceof List)) list = new ArrayList<>();
        else list = (List<Object>) data;
        list.add(items);
        return list;
    }

}
