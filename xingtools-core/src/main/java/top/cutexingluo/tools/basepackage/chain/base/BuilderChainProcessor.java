package top.cutexingluo.tools.basepackage.chain.base;

import top.cutexingluo.tools.common.base.IDataValue;
import top.cutexingluo.tools.common.data.PairEntry;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 建造者执行链
 *
 * <p>可以通过关系建造的方式获取目标对象</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/10 15:44
 * @since 1.1.4
 */
public interface BuilderChainProcessor<T> extends IDataValue<T>, ChainProcessor {


    /**
     * 获取当前层目标对象
     * <p>仅使用当前时刻 value 和 valueGetter </p>
     * <p>调用 get 前后结果可能不一样</p>
     */
    T getCurrent();

    /**
     * 获取建立生成后的目标对象
     * <pre>
     * 返回值：
     * 1. 层数为0 代表无法生成目标对象,目标对象为 null, 即 Entry(0,null)
     * 2. 层数大于等于1 代表向上查找父级的调用层数, 直到找到目标对象, 1 为当前层数对象, 2 为上一个with (即父节点)对象, 以此类推
     * </pre>
     *
     * @return pair entry 调用层数和建立生成后的目标对象
     */
    PairEntry<Integer, T> get();

    /**
     * 组装链条,创建子节点, 建立关联, 后面的操作会通过该关联获取目标对象 (通过get 获取)
     * <p>value 为空会调用 builder 生成</p>
     *
     * @param value   目标对象
     * @param builder 如果目标对象为空则执行builder
     */
    <R> BuilderChainProcessor<R> with(R value, Function<T, R> builder);


    /**
     * 组装链条, 创建子节点, 建立关联, 后面的操作会通过该关联获取目标对象 (通过get 获取)
     * <p>valueGetter 返回值为空会调用 builder 生成</p>
     *
     * @param valueGetter 目标对象获取器
     * @param builder     如果目标对象为空则执行builder
     */
    <R> BuilderChainProcessor<R> withGetter(Supplier<R> valueGetter, Function<T, R> builder);


    /**
     * 组装链条,创建子节点,  建立关联, 后面的操作会通过该关联获取目标对象 (通过get 获取)
     * <p>value 为空后调用valueGetter, 其返回值为空会调用 builder 生成</p>
     *
     * @param value       目标对象
     * @param valueGetter 目标对象获取器
     * @param builder     如果目标对象为空则执行builder
     */
    <R> BuilderChainProcessor<R> withBoth(R value, Supplier<R> valueGetter, Function<T, R> builder);
}
