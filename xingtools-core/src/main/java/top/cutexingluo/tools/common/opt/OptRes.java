package top.cutexingluo.tools.common.opt;

import lombok.Data;
import top.cutexingluo.tools.common.base.IData;

/**
 * Optional 扩展类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/22 17:04
 * @since 1.0.4
 */
@Data
public class OptRes<T> implements IData<T> {

    protected T value;

    protected Class<T> clazz;

    public OptRes(T value) {
        this.value = value;
        if (value != null) {  // if null equals no params
            this.clazz = (Class<T>) value.getClass();
        }
    }

    public OptRes() {
    }

    public OptRes(T value, Class<T> clazz) {
        this.value = value;
        this.clazz = clazz;
    }

    public static <V> OptRes<V> of(V value) {
        return new OptRes<>(value);
    }

    public static <V> OptRes<V> ofNoClazz(V value) {
        return new OptRes<>(value, null);
    }

    @Override
    public T data() {
        return value;
    }

    /**
     * 更新赋值
     *
     * @param updateClazzIfPresent true=> clazz 存在才赋值
     */
    public void update(Object obj, boolean updateClazzIfPresent) {
        if (obj == null) {
            this.value = null;
        } else {
            updateCheckPresent(obj, updateClazzIfPresent);
        }
    }


    /**
     * obj 存在则更新赋值
     *
     * @param updateClazzIfPresent true=> 对象存在 clazz 才赋值
     */
    public void updateCheckPresent(Object obj, boolean updateClazzIfPresent) {
        if (obj == null) return;
        if (obj instanceof OptRes) {
            OptRes<T> optRes = (OptRes<T>) obj;
            this.value = optRes.value;
            if (!updateClazzIfPresent || optRes.clazz != null) this.clazz = optRes.clazz;
        } else {
            this.value = (T) obj;
            if (!updateClazzIfPresent) this.clazz = (Class<T>) obj.getClass();
        }
    }
}
