package top.cutexingluo.tools.basepackage.chain.core;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.basepackage.base.Refreshable;
import top.cutexingluo.tools.basepackage.chain.base.BuilderMapChainProcessor;
import top.cutexingluo.tools.common.base.IDataValue;
import top.cutexingluo.tools.common.data.Entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 扩展建造者链
 * <p>{@link BuilderChain} 升级版</p>
 * <p>支持跨层访问 , 一个节点多个生成方法, 缺点是没有泛型限制需要转型</p>
 * <p>使用方式</p>
 * <pre>
 *             BuilderMapChain chain = new BuilderMapChain(3, applicationContext)
 *                 .with(redisTemplate, o -> {  //  1.如果 redisTemplate 为空, 调用该方法生成
 *                     ApplicationContext ac = (ApplicationContext) o;  // 使用上层数据
 *                     return ac.getBean(RedisTemplate.class);
 *                 }).withList(redisCache, Arrays.asList(
 *                                  //  2.如果 redisCache 为空, 调用该方法生成
 *                                 o -> {
 *                                     RedisTemplate &lt String, Object> rt = (RedisTemplate &lt String, Object>) o; // 使用上层数据
 *                                     return new RYRedisCache(rt);
 *                                 },
 *                                 //  3.如果 redisCache 为空, 调用该方法生成
 *                                 o -> {
 *                                     ApplicationContext ac = (ApplicationContext) o; // 使用上上层数据
 *                                     return ac.getBean(RYRedisCache.class);
 *                                 }
 *                         )
 *                 );
 *                 // front-dfs 会优先 下层 2 , 然后 1, 最后是3 顺序执行
 *             Entry &lt Integer, RYRedisCache &gt entry = chain.createFrontDfs(3);
 *             return entry.getValue();
 * </pre>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/11 15:05
 * @since 1.1.4
 */
@Data
public class BuilderMapChain implements BuilderMapChainProcessor, Refreshable<BuilderMapChain> {

    /**
     * 层级映射 map
     * <p>从1 开始到 maxLayer 的节点 mapper </p>
     */
    protected Map<Integer, Node<?>> layers;

    /**
     * 最大层级
     */
    protected int maxLayer = 0;

    public BuilderMapChain(@NotNull Map<Integer, Node<?>> layers) {
        this.layers = layers;
        this.maxLayer = layers.size();
    }

    public BuilderMapChain() {
        this.layers = new HashMap<>();
    }

    /**
     * @param cap HashMap 容量 - 需要组装的层数
     */
    public BuilderMapChain(int cap) {
        this.layers = new HashMap<>(cap);
    }

    /**
     * @param cap       HashMap 容量 - 需要组装的层数
     * @param rootValue 根节点值, 相当于先调用了一次 组装 , 故容量需要包含该次
     */
    public BuilderMapChain(int cap, Object rootValue) {
        this(cap);
        with(rootValue, null);
    }

    /**
     * @param cap       HashMap 容量 - 需要组装的层数
     * @param rootValue 根节点值, 相当于先调用了一次 组装 , 故容量需要包含该次
     */
    public <V> BuilderMapChain(int cap, V rootValue, Supplier<V> rootValueSupplier) {
        this(cap);
        withBoth(rootValue, rootValueSupplier, null);
    }

    protected void checkBounds(int index) {
        if (index <= 0 || index > maxLayer) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }
    }

    @Override
    public <V> Node<V> getNode(int index) {
        checkBounds(index);
        @SuppressWarnings("unchecked")
        Node<V> node = (Node<V>) layers.get(index);
        return node;
    }

    @Override
    public <V> Node<V> getNode() {
        return getNode(maxLayer);
    }


    @Override
    public <V> Node<V> getNodeFirst(int index) {
        checkBounds(index);
        @SuppressWarnings("unchecked")
        Node<V> node = (Node<V>) layers.get(index);
        if (!node.isFlag()) { // 是否有访问标记
            V value = node.getValue();
            Supplier<V> valueGetter = node.getValueGetter();
            if (value == null && valueGetter != null) {
                value = valueGetter.get();
                node.setValue(value);
            }
        }
        return node;
    }

    @Override
    public boolean isVisit(int index) {
        Node<Object> node = getNode(index);
        return node.isFlag();
    }

    @Override
    public <V> Entry<Integer, V> createFrontDfs(int layer) {
        checkBounds(layer);
        // init Value
        Node<V> currentNode = getNodeFirst(layer);
        V value = currentNode.getValue();
        if (currentNode.isFlag()) { // 访问过了
            return new Entry<>(1, value);
        }
        if (value != null) {
            currentNode.setFlag(true);
            return new Entry<>(1, value);
        }
        // find ancestor value
        List<Function<Object, V>> relationGetters = currentNode.getRelationGetters();
        if (relationGetters != null && !relationGetters.isEmpty()) {
            int maxSize = Math.min(maxLayer - 1, relationGetters.size());
            for (int i = 0; i < maxSize; i++) {
                int ancestorIndex = layer - (i + 1);
                Function<Object, V> relationGetter = relationGetters.get(i);
                if (relationGetter == null) continue;
                Entry<Integer, Object> entry = createFrontDfs(ancestorIndex);
                Object entryValue = entry.getValue();
                if (entry.getKey() == 0 || entryValue == null) {
                    continue;
                }
                V result = relationGetter.apply(entryValue); // 执行生成器函数
                if (result != null) { // 存在值则直接返回
                    currentNode.setValue(result);
                    currentNode.setFlag(true); // check
                    return new Entry<>(entry.getKey() + 1, result);
                }
            }
        }

        // 没有 getters 或遍历getters 完成 没有值, 只好 setFlag 为 true
        currentNode.setFlag(true);

        return new Entry<>(0, null);
    }

    @Override
    public <V> Entry<Integer, V> createBackDfs(int layer) {
        checkBounds(layer);
        // init Value
        Node<V> currentNode = getNodeFirst(layer);
        V value = currentNode.getValue();
        if (currentNode.isFlag()) { // 访问过了
            return new Entry<>(1, value);
        }
        if (value != null) {
            currentNode.setFlag(true);
            return new Entry<>(1, value);
        }
        // find ancestor value
        List<Function<Object, V>> relationGetters = currentNode.getRelationGetters();
        if (relationGetters != null && !relationGetters.isEmpty()) {
            int maxSize = Math.min(maxLayer - 1, relationGetters.size());
            for (int i = maxSize - 1; i >= 0; i--) {
                int ancestorIndex = layer - (i + 1);
                Function<Object, V> relationGetter = relationGetters.get(i);
                if (relationGetter == null) continue;
                Entry<Integer, Object> entry = createBackDfs(ancestorIndex);
                Object entryValue = entry.getValue();
                if (entry.getKey() == 0 || entryValue == null) {
                    continue;
                }
                V result = relationGetter.apply(entryValue); // 执行生成器函数
                if (result != null) { // 存在值则直接返回
                    currentNode.setValue(result);
                    currentNode.setFlag(true); // check
                    return new Entry<>(entry.getKey() + 1, result);
                }
            }
        }

        // 没有 getters 或遍历getters 完成 没有值, 只好 setFlag 为 true
        currentNode.setFlag(true);

        return new Entry<>(0, null);
    }

    @Override
    public <V> Entry<Integer, V> createFrontBfs(int layer) {
        checkBounds(layer);
        int applyCount = 0;
        for (int i = 1; i <= layer; i++) {
            Node<Object> currentNode = getNodeFirst(i);
            if (currentNode.isFlag()) continue;
            Object result = currentNode.getValue();
            if (result != null) {
                currentNode.setFlag(true);
                continue;
            }
            // find ancestor value
            List<Function<Object, Object>> relationGetters = currentNode.getRelationGetters();
            if (relationGetters != null && !relationGetters.isEmpty()) {
                int maxSize = Math.min(maxLayer - 1, relationGetters.size());
                for (int j = 0; j < maxSize; j++) {
                    int ancestorIndex = i - (j + 1);
                    Function<Object, Object> relationGetter = relationGetters.get(j);
                    if (relationGetter == null) continue;
                    Node<Object> ancestorNode = getNode(ancestorIndex);
                    Object ancestorValue = ancestorNode.getValue();
                    if (!ancestorNode.isFlag()) {
                        // 基本上不会到达这里
                        if (ancestorValue != null) ancestorNode.setFlag(true);
                        continue;
                    }
                    if (ancestorValue != null) {
                        result = relationGetter.apply(ancestorValue); // 执行生成器函数
                        applyCount++; // 执行次数+1
                        if (result != null) { // 存在值则直接返回
                            currentNode.setValue(result);
                            currentNode.setFlag(true); // check
                            break;
                        }
                    }
                }
            }

            // 没有 getters 或遍历getters 完成 没有值, 只好 setFlag 为 true
            currentNode.setFlag(true);

        }
        return new Entry<>(applyCount, getValue(layer));
    }

    @Override
    public <V> Entry<Integer, V> createBackBfs(int layer) {
        checkBounds(layer);
        int applyCount = 0;
        for (int i = 1; i <= layer; i++) {
            Node<Object> currentNode = getNodeFirst(i);
            if (currentNode.isFlag()) continue;
            Object result = currentNode.getValue();
            if (result != null) {
                currentNode.setFlag(true);
                continue;
            }
            // find ancestor value
            List<Function<Object, Object>> relationGetters = currentNode.getRelationGetters();
            if (relationGetters != null && !relationGetters.isEmpty()) {
                int maxSize = Math.min(maxLayer - 1, relationGetters.size());
                for (int j = maxSize - 1; j >= 0; j--) {
                    int ancestorIndex = i - (j + 1);
                    Function<Object, Object> relationGetter = relationGetters.get(j);
                    if (relationGetter == null) continue;
                    Node<Object> ancestorNode = getNode(ancestorIndex);
                    Object ancestorValue = ancestorNode.getValue();
                    if (!ancestorNode.isFlag()) {
                        // 基本上不会到达这里
                        if (ancestorValue != null) ancestorNode.setFlag(true);
                        continue;
                    }
                    if (ancestorValue != null) {
                        result = relationGetter.apply(ancestorValue); // 执行生成器函数
                        applyCount++; // 执行次数+1
                        if (result != null) { // 存在值则直接返回
                            currentNode.setValue(result);
                            currentNode.setFlag(true); // check
                            break;
                        }
                    }
                }
            }

            // 没有 getters 或遍历getters 完成 没有值, 只好 setFlag 为 true
            currentNode.setFlag(true);

        }
        return new Entry<>(applyCount, getValue(layer));
    }

    @Override
    public <V> BuilderMapChain with(V value, Function<Object, V> relationGetter) {
        if (relationGetter == null) return withList(value, null);
        ArrayList<Function<Object, V>> list = new ArrayList<>(1);
        list.add(relationGetter);
        return withList(value, list);
    }

    @Override
    public <V> BuilderMapChain withList(V value, List<Function<Object, V>> relationGetters) {
        Node<V> node = new Node<>(value, relationGetters);
        layers.put(++maxLayer, node);
        return this;
    }

    @Override
    public <V> BuilderMapChain withGetter(Supplier<V> valueGetter, Function<Object, V> relationGetter) {
        if (relationGetter == null) return withListGetter(valueGetter, null);
        ArrayList<Function<Object, V>> list = new ArrayList<>(1);
        list.add(relationGetter);
        return withListGetter(valueGetter, list);
    }

    @Override
    public <V> BuilderMapChain withListGetter(Supplier<V> valueGetter, List<Function<Object, V>> relationGetters) {
        Node<V> node = new Node<>(valueGetter, relationGetters);
        layers.put(++maxLayer, node);
        return this;
    }

    @Override
    public <V> BuilderMapChain withBoth(V value, Supplier<V> valueGetter, Function<Object, V> relationGetter) {
        if (relationGetter == null) return withListBoth(value, valueGetter, null);
        ArrayList<Function<Object, V>> list = new ArrayList<>(1);
        list.add(relationGetter);
        return withListBoth(value, valueGetter, list);
    }

    @Override
    public <V> BuilderMapChain withListBoth(V value, Supplier<V> valueGetter, List<Function<Object, V>> relationGetters) {
        Node<V> node = new Node<>(value, valueGetter, relationGetters);
        layers.put(++maxLayer, node);
        return this;
    }

    /**
     * 重置访问标记
     */
    @Override
    public BuilderMapChain refresh() {
        layers.forEach((k, node) -> node.setFlag(false)); // 访问标记全部未访问
        return this;
    }


    /**
     * BuilderMapChain 节点类
     *
     * @see BuilderChain
     */
    @Data
    public static class Node<V> implements IDataValue<V> {
        /**
         * 当前值
         */
        protected V value;
        /**
         * 值获取器
         */
        protected Supplier<V> valueGetter;
        /**
         * 关系获取器
         */
        protected List<Function<Object, V>> relationGetters;
        /**
         * 访问标记, 存在值或已遍历 relationGetters 完成
         */
        protected boolean flag;

        public Node(V value) {
            this.value = value;
        }

        public Node(Supplier<V> valueGetter) {
            this.valueGetter = valueGetter;
        }

        public Node(V value, Supplier<V> valueGetter) {
            this.value = value;
            this.valueGetter = valueGetter;
        }

        public Node(V value, List<Function<Object, V>> relationGetters) {
            this.value = value;
            this.relationGetters = relationGetters;
        }

        public Node(Supplier<V> valueGetter, List<Function<Object, V>> relationGetters) {
            this.valueGetter = valueGetter;
            this.relationGetters = relationGetters;
        }


        public Node(V value, Supplier<V> valueGetter, List<Function<Object, V>> relationGetters) {
            this.value = value;
            this.valueGetter = valueGetter;
            this.relationGetters = relationGetters;
        }

    }

}
