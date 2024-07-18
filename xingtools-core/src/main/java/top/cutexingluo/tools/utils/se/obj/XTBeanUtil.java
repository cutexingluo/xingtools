package top.cutexingluo.tools.utils.se.obj;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.Editor;
import top.cutexingluo.tools.designtools.builder.XTBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean 对象复制类
 * <p>BeanUtil 复制属性类，可以进行不同类对象的属性复制</p>
 * <p>1. 不能new，利用Builder进行使用</p>
 * <p>2. 添加或简化了一些操作</p>
 * <p>3. 如果没想要的方法，请使用 {@link  BeanUtil}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/25 16:04
 */
public class XTBeanUtil<T> {
    protected XTBeanUtil() {
    }

    /**
     * 拷贝属性，返回新的对象
     *
     * @param srcList     源bean对象列表
     * @param targetClass 目标类型
     */
    public static <T> List<T> copyToList(Collection<?> srcList, Class<T> targetClass) {
        return BeanUtil.copyToList(srcList, targetClass);
    }

    /**
     * 拷贝属性，返回新的对象
     *
     * @param srcList     源bean对象列表
     * @param targetClass 目标类型
     */
    public static <T> List<T> copyToList(Collection<?> srcList, Class<T> targetClass, CopyOptions options) {
        return BeanUtil.copyToList(srcList, targetClass, options);
    }

    //----------------------------------------------------------------
    //  builder创建的功能


    protected CopyOptions options;
    protected Class<T> targetClass;

    /**
     * 不推荐，可以直接用BeanUtil.copyProperties代替
     */
    public T copyProperties(Object srcObject) {
        return BeanUtil.copyProperties(srcObject, targetClass);
    }

    public List<T> copyList(Collection<?> srcList) {
        return BeanUtil.copyToList(srcList, targetClass, options);
    }

    public List<T> copyList(Collection<?> srcList, Editor<String> fieldNameEditor) {
        options.setFieldNameEditor(fieldNameEditor);
        return BeanUtil.copyToList(srcList, targetClass, options);
    }

    /**
     * 直接使用options，可以直接使用BeanUtil.copyToList代替
     */
    public List<T> copyList(Collection<?> srcList, CopyOptions options) {
        return BeanUtil.copyToList(srcList, targetClass, options);
    }

    /**
     * 其他更多请使用CopyOptions.setXXX
     */
    public class Builder extends XTBuilder<XTBeanUtil<T>> {
        Map<String, String> fieldMapping;

        public Builder(Class<T> targetClass) {
            target = new XTBeanUtil<T>();
            target.targetClass = targetClass;
            fieldMapping = new HashMap<>();
        }

        /**
         * 添加字段映射
         *
         * @param srcFieldName 原字段
         * @param tarFieldName 映射过来的新对象字段
         */
        public Builder addMapping(String srcFieldName, String tarFieldName) {
            fieldMapping.put(srcFieldName, tarFieldName);
            return this;
        }

        /**
         * 添加字段映射
         *
         * @param fieldMap 字段映射集
         */
        public Builder addMapObject(Map<String, String> fieldMap) {
            fieldMapping.putAll(fieldMap);
            return this;
        }

        @Override
        public XTBeanUtil<T> build() {
            target.options.setFieldMapping(fieldMapping);
            return target;
        }
    }
}
