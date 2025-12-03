package top.cutexingluo.tools.utils.orm.mybatisplus;



import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.function.Supplier;


/**
 * MybatisPlus 字段填充器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/12/2 16:58
 * @since 1.2.1
 */
public interface MetaObjectHandlerPlus extends MetaObjectHandler {

    /**
     * 目标字段值为空才会赋值
     */
     default void nullFill(String fieldName, Supplier<?> fieldValSupplier, MetaObject metaObject) {
        if (metaObject.hasGetter(fieldName)) {
            Object fieldValOld = metaObject.getValue(fieldName);
            if (fieldValOld == null) { // 如果字段为空，则填充新值
                if (fieldValSupplier != null) {
                    this.setFieldValByName(fieldName, fieldValSupplier.get(), metaObject);
                }
            }
        }
    }

    /**
     * 目标字段存在会赋值
     */
    default void fill(String fieldName, Supplier<?> fieldValSupplier, MetaObject metaObject) {
        if (metaObject.hasGetter(fieldName)) {
            if (fieldValSupplier != null) {
                this.setFieldValByName(fieldName, fieldValSupplier.get(), metaObject);
            }
        }
    }
}
