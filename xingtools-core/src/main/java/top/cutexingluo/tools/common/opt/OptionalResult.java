package top.cutexingluo.tools.common.opt;


import lombok.Data;
import top.cutexingluo.tools.common.base.IDataValue;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Optional  扩展类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/22 16:37
 * @since 1.0.4
 */
@Data
public class OptionalResult<T> implements IDataValue<T> {

    /**
     * 值
     */
    protected T value;

    /**
     * 是否存在
     */
    protected boolean exists;


    /**
     * 空对象
     */
    public OptionalResult() {
        this.exists = false;
    }

    /**
     * value 存在 则 exists = true
     */
    public OptionalResult(T value) {
        this.value = value;
        this.exists = this.value != null;
    }

    /**
     * 仅使用 exists 变量
     */
    public OptionalResult(boolean exists) {
        this.exists = exists;
    }

    public OptionalResult(T value, boolean exists) {
        this.value = value;
        this.exists = exists;
    }

    //-----methods------

    /**
     * 如果 exists == true , value 却为空则执行 consumer
     *
     * @return 最终结果/执行之后是否存在值
     */
    public boolean existsIfAbsent(Consumer<OptionalResult<T>> consumer) {
        if (exists && value == null) {
            if (consumer == null) {
                return false;
            }
            consumer.accept(this); // 需手动赋值 exists
        }
        return exists;
    }

    /**
     * 如果 exists == true , value 却为空则执行 consumer
     * <p>自动判断结果是否有值</p>
     *
     * @return 最终结果/执行之后是否存在值
     */
    public boolean existsIfAbsentCheck(Consumer<OptionalResult<T>> consumer) {
        if (exists && value == null) {
            if (consumer == null) {
                return false;
            }
            consumer.accept(this);
            exists = value != null;
        }
        return exists;
    }


    /**
     * 如果 exists == true , value 却为空则执行 supplier
     * <p>自动判断结果是否有值</p>
     *
     * @return 最终结果/执行之后是否存在值
     */
    public boolean existsIfAbsentCheck(Supplier<T> supplier) {
        return existsIfAbsentCheck((o) -> {
            if (supplier != null) {
                o.value = supplier.get();
            }
        });
    }

    @Override
    public T data() {
        return value;
    }
}
