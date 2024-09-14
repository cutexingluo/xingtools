package top.cutexingluo.tools.designtools.protocol.adapter;

import top.cutexingluo.tools.basepackage.base.ExtInitializable;

/**
 * 模组适配器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/14 11:38
 */
public interface ModuleAdapter<T> extends ExtInitializable<ModuleAdapter<T>> {

    /**
     * 设置 mapper
     */
    ModuleAdapter<T> setMapper(T mapper);

    /**
     * 获取 mapper
     */
    T getMapper();


    @Override
    default ModuleAdapter<T> initSelf() {
        initConfigure();
        initSerializers();
        initDeserializers();
        initModule();
        return this;
    }

    /**
     * 初始化模组
     * <p>在这里注册 module</p>
     */
    default ModuleAdapter<T> initModule() {
        return this;
    }

    /**
     * 初始化序列化器
     * <p>在这里添加 serializer</p>
     */
    default ModuleAdapter<T> initSerializers() {
        return this;
    }

    /**
     * 初始化反序列化器
     * <p>在这里添加 deserializer</p>
     */
    default ModuleAdapter<T> initDeserializers() {
        return this;
    }

    /**
     * 初始化配置
     * <p>在这里配置 属性配置</p>
     */
    default ModuleAdapter<T> initConfigure() {
        return this;
    }

}
