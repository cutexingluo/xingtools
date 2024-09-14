package top.cutexingluo.tools.basepackage.chain.base;

import top.cutexingluo.tools.common.base.IData;
import top.cutexingluo.tools.common.data.PairEntry;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 进阶建造者执行链
 *
 * <p>可以通过关系建造的方式获取目标对象, 支持多级向上查找</p>
 * <p>缺点就是没有泛型限制</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/11 14:34
 * @see BuilderChainProcessor
 */
public interface BuilderMapChainProcessor {

    /**
     * 获取指定索引(层数)的节点
     * <p>直接获取节点, 不进行任何操作</p>
     * <p>最好先调用create 系列方法</p>
     */
    <V> IData<V> getNode(int index);


    /**
     * 获取指定索引(层数)的节点的值
     * <p>直接获取节点的值, 不进行任何操作</p>
     * <p>最好先调用create 系列方法</p>
     */
    default <V> V getValue(int index) {
        IData<V> node = getNode(index);
        return node.data();
    }

    /**
     * 获取当前索引(层数)的节点
     * <p>最好先调用create 系列方法</p>
     */
    <V> IData<V> getNode();

    /**
     * 获取当前索引(层数)的节点的值
     * <p>最好先调用create 系列方法</p>
     */
    default <V> V getValue() {
        IData<V> node = getNode();
        return node.data();
    }

    /**
     * 获取指定索引(层数)的节点
     * <p>第一次获取目标节点，先进行预处理操作</p>
     * <p>不推荐自己调用</p>
     */
    <V> IData<V> getNodeFirst(int index);

    /**
     * 获取指定索引(层数)的节点的值
     * <p>第一次获取目标节点的值，先进行预处理操作</p>
     * <p>不推荐自己调用</p>
     */
    default <V> V getValueFirst(int index) {
        IData<V> node = getNodeFirst(index);
        return node.data();
    }

    /**
     * 判断是否访问过指定层数的节点
     */
    boolean isVisit(int index);

    /**
     * 使用 DFS (深度优先) 从最大层数向最小层数 进行创建
     * <p>front - 从relationGetters 索引小到大(从前往后)</p>
     * <p>dfs - 从最大层数向最小层数 递归访问</p>
     * <pre>
     * 返回值：
     * 1. 层数为0 代表无法生成目标对象,目标对象为 null, 即 Entry(0,null)
     * 2. 层数大于等于1 代表向上查找父级的调用层数, 直到找到目标对象, 1 为当前层数对象, 2 为上一个with 对象, 以此类推
     * </pre>
     *
     * @param layer 目标层索引
     * @param <V>   目标节点值类型
     * @return 返回 PairEntry (递归调用的层数, 目标层的值)
     */
    <V> PairEntry<Integer, V> createFrontDfs(final int layer);


    /**
     * 使用 DFS (深度优先) 从最大层数向最小层数 进行创建
     * <p>back - 从relationGetters 索引大到小(从后往前)</p>
     * <p>dfs - 从最大层数向最小层数 递归访问</p>
     * <pre>
     * 返回值：
     * 1. 层数为0 代表无法生成目标对象,目标对象为 null, 即 Entry(0,null)
     * 2. 层数大于等于1 代表向上查找父级的调用层数, 直到找到目标对象, 1 为当前层数对象, 2 为上一个with 对象, 以此类推
     * </pre>
     *
     * @param layer 目标层索引
     * @param <V>   目标节点值类型
     * @return 返回 PairEntry (递归调用的层数, 目标层的值)
     */
    <V> PairEntry<Integer, V> createBackDfs(final int layer);


    /**
     * 使用 BFS (广度优先, 层级访问 从最小层数向最大层数 进行创建
     * <p>front - 从relationGetters 索引小到大(从前往后)</p>
     * <p>bfs - 从最小层数到最大层数 循环访问</p>
     * <pre>
     * 返回值：
     * 1. 层数为0 代表无法生成目标对象,目标对象为 null, 即 Entry(0,null)
     * 2. 层数大于等于1 代表向上查找父级的调用层数, 直到找到目标对象, 1 为当前层数对象, 2 为上一个with 对象, 以此类推
     * </pre>
     *
     * @param layer 目标层索引
     * @param <V>   目标节点值类型
     * @return 返回 PairEntry (relationGetter 调用的次数, 目标层的值)
     */
    <V> PairEntry<Integer, V> createFrontBfs(final int layer);


    /**
     * 使用 BFS (广度优先, 层级访问) 从最小层数向最大层数 进行创建
     * <p>back - 从relationGetters 索引大到小(从后往前)</p>
     * <p>bfs - 从最小层数到最大层数 循环访问</p>
     * <pre>
     * 返回值：
     * 1. 层数为0 代表无法生成目标对象,目标对象为 null, 即 Entry(0,null)
     * 2. 层数大于等于1 代表向上查找父级的调用层数, 直到找到目标对象, 1 为当前层数对象, 2 为上一个with 对象, 以此类推
     * </pre>
     *
     * @param layer 目标层索引
     * @param <V>   目标节点值类型
     * @return 返回 PairEntry (relationGetter 调用的次数, 目标层的值)
     */
    <V> PairEntry<Integer, V> createBackBfs(final int layer);


    /**
     * 组装链条,创建节点, 建立关联, 后面的操作会通过该关联获取目标对象
     * <p>value 为空会调用 relationGetter 生成</p>
     *
     * @param value          目标对象
     * @param relationGetter 关系生成器, 如果目标对象为空则执行relationGetter
     */
    <V> BuilderMapChainProcessor with(V value, Function<Object, V> relationGetter);


    /**
     * 组装链条,创建节点, 建立关联, 后面的操作会通过该关联获取目标对象
     * <p>value 为空会调用 relationGetters 生成</p>
     *
     * @param value           目标对象
     * @param relationGetters 关系生成器列表, 如果目标对象为空则根据列表顺序执行relationGetters, 索引0 为与上一个关联, 索引1代表与上上个关联, 以此类推, 多则丢弃
     */
    <V> BuilderMapChainProcessor withList(V value, List<Function<Object, V>> relationGetters);

    /**
     * 组装链条,创建节点, 建立关联, 后面的操作会通过该关联获取目标对象
     * <p>valueGetter 返回值为空会调用 relationGetter 生成</p>
     *
     * @param valueGetter    目标对象获取器
     * @param relationGetter 关系生成器, 如果目标对象为空则执行relationGetter
     */
    <V> BuilderMapChainProcessor withGetter(Supplier<V> valueGetter, Function<Object, V> relationGetter);

    /**
     * 组装链条,创建节点, 建立关联, 后面的操作会通过该关联获取目标对象
     * <p>valueGetter 返回值为空会调用 relationGetters 生成</p>
     *
     * @param valueGetter     目标对象获取器
     * @param relationGetters 关系生成器列表, 如果目标对象为空则根据列表顺序执行relationGetters, 索引0 为与上一个关联, 索引1代表与上上个关联, 以此类推, 多则丢弃
     */
    <V> BuilderMapChainProcessor withListGetter(Supplier<V> valueGetter, List<Function<Object, V>> relationGetters);


    /**
     * 组装链条,创建节点, 建立关联, 后面的操作会通过该关联获取目标对象
     * <p>value 为空后调用valueGetter, 其返回值为空会调用 relationGetter 生成</p>
     *
     * @param value          目标对象
     * @param valueGetter    目标对象获取器
     * @param relationGetter 关系生成器, 如果目标对象为空则执行relationGetter
     */
    <V> BuilderMapChainProcessor withBoth(V value, Supplier<V> valueGetter, Function<Object, V> relationGetter);


    /**
     * 组装链条,创建节点, 建立关联, 后面的操作会通过该关联获取目标对象
     * <p>value 为空后调用valueGetter, 其返回值为空会调用 relationGetters 生成</p>
     *
     * @param value           目标对象
     * @param valueGetter     目标对象获取器
     * @param relationGetters 关系生成器列表, 如果目标对象为空则根据列表顺序执行relationGetters, 索引0 为与上一个关联, 索引1代表与上上个关联, 以此类推, 多则丢弃
     */
    <V> BuilderMapChainProcessor withListBoth(V value, Supplier<V> valueGetter, List<Function<Object, V>> relationGetters);

}
