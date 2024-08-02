package top.cutexingluo.tools.designtools.builder.builderimpl;


import lombok.Data;
import lombok.EqualsAndHashCode;
import top.cutexingluo.tools.basepackage.function.TriConsumer;
import top.cutexingluo.tools.designtools.builder.XTBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 高级建造者实例 builder
 * <br>
 * <p>
 * Temp tmp =
 * Builder.of(Temp::new)
 * .with(Temp::setName, "临时").build()
 * </p>
 * <br>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/5 22:51
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Builder<T> extends XTBuilder<T> {
    private final Supplier<T> instantiation;
    private List<Consumer<T>> modifiers = new ArrayList<>();

    public Builder(Supplier<T> instantiation) {
        this.instantiation = instantiation;
    }

    public static <T> Builder<T> of(Supplier<T> instantiation) {
        return new Builder<>(instantiation);
    }

    public <P1> Builder<T> with(BiConsumer<T, P1> consumer, P1 p1) {
        Consumer<T> c = instance -> consumer.accept(instance, p1);
        modifiers.add(c);
        return this;
    }

    public <P1, P2> Builder<T> with(TriConsumer<T, P1, P2> consumer, P1 p1, P2 p2) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2);
        modifiers.add(c);
        return this;
    }

    public <P1, P2, P3> Builder<T> with(TetraConsumer<T, P1, P2, P3> consumer, P1 p1, P2 p2, P3 p3) {
        Consumer<T> c = instance -> consumer.accept(instance, p1, p2, p3);
        modifiers.add(c);
        return this;
    }

    /**
     * 批量放入参数
     *
     * @since 1.1.2
     */
    public Builder<T> withAll(BiConsumer<T, Object[]> consumer, Object[] args) {
        Consumer<T> c = instance -> consumer.accept(instance, args);
        modifiers.add(c);
        return this;
    }

    @Override
    public T build() {
        T value = instantiation.get();
        modifiers.forEach(modifier -> modifier.accept(value));
        modifiers.clear();
        return value;
    }


    @FunctionalInterface
    public interface TetraConsumer<T, P1, P2, P3> {
        void accept(T t, P1 p1, P2 p2, P3 p3);
    }
}
