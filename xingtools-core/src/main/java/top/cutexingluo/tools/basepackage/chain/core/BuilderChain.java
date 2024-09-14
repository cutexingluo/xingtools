package top.cutexingluo.tools.basepackage.chain.core;

import lombok.Data;
import top.cutexingluo.tools.basepackage.chain.base.BuilderChainProcessor;
import top.cutexingluo.tools.common.data.Entry;
import top.cutexingluo.tools.common.data.node.IParent;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 建造者链
 *
 * <p><b>使用方式如下</b></p>
 * <pre>
 *  Entry &lt Integer, String&gt ss = new BuilderChain<>("ss")  //1
 *       .with(new Hero("s"), s -> new Hero(s) ) //2
 *       .with(null, hero -> hero.getName()) //3
 *       .get();
 * </pre>
 * <p>第 3 句传入的value 是 null , 所以会调用后面的函数生成该对象, 而后面函数的参数是 上面第2句的value值, 该value 存在则返回给3句的函数作为参数</p>
 * <p>而hero是一个新对象, 所以hero.getName()为"s" , 所以最后结果返回 Entry(2,"s") , （调用2层, 最后的结果)</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/9/10 15:38
 * @since 1.1.4
 */
@Data
public class BuilderChain<T, P> implements BuilderChainProcessor<T>, IParent<BuilderChain<P, ?>> {

    /**
     * 当前值
     */
    protected T value;

    /**
     * 值获取器
     * <p>如果value 为null ,get 会优先调用该方法</p>
     */
    protected Supplier<T> valueGetter;

    /**
     * 关系获取器
     */
    protected Function<P, T> relationGetter;

    protected BuilderChain<P, ?> parent;

    public BuilderChain(T value) {
        this.value = value;
    }

    public BuilderChain(Supplier<T> valueGetter) {
        this.valueGetter = valueGetter;
    }

    public BuilderChain(T value, Supplier<T> valueGetter) {
        this.value = value;
        this.valueGetter = valueGetter;
    }


    protected BuilderChain(T value, Function<P, T> relationGetter, BuilderChain<P, ?> parent) {
        this.value = value;
        this.relationGetter = relationGetter;
        this.parent = parent;
    }

    protected BuilderChain(Supplier<T> valueGetter, Function<P, T> relationGetter, BuilderChain<P, ?> parent) {
        this.valueGetter = valueGetter;
        this.relationGetter = relationGetter;
        this.parent = parent;
    }

    protected BuilderChain(T value, Supplier<T> valueGetter, Function<P, T> relationGetter, BuilderChain<P, ?> parent) {
        this.value = value;
        this.valueGetter = valueGetter;
        this.relationGetter = relationGetter;
        this.parent = parent;
    }


    @Override
    public T getCurrent() {
        // init now
        if (value == null && valueGetter != null) { // 初始化当前值
            value = valueGetter.get();
        }
        return value;
    }

    @Override
    public Entry<Integer, T> get() {
        // init now
        getCurrent();
        if (value != null) {
            return new Entry<>(1, value);
        }
        // find parent value
        if (parent != null && relationGetter != null) { // 获取父节点的值
            Entry<Integer, P> entry = parent.get();
            if (entry.getKey() == 0 || entry.getValue() == null) {
                return new Entry<>(0, this.value);
            } else {
                P parentValue = entry.getValue();
                this.value = relationGetter.apply(parentValue);
                return new Entry<>(entry.getKey() + 1, this.value);
            }
        }
        return new Entry<>(0, null);
    }

    @Override
    public <R> BuilderChain<R, T> with(R value, Function<T, R> relationGetter) {
        return new BuilderChain<>(value, relationGetter, this);
    }

    @Override
    public <R> BuilderChain<R, T> withGetter(Supplier<R> valueGetter, Function<T, R> relationGetter) {
        return new BuilderChain<>(valueGetter, relationGetter, this);
    }

    @Override
    public <R> BuilderChain<R, T> withBoth(R value, Supplier<R> valueGetter, Function<T, R> relationGetter) {
        return new BuilderChain<>(value, valueGetter, relationGetter, this);
    }

    @Override
    public boolean isPresent() {
        return value != null;
    }

    @Override
    public boolean isEmpty() {
        return value == null;
    }
}
