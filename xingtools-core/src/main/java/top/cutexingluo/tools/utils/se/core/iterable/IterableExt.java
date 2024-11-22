package top.cutexingluo.tools.utils.se.core.iterable;

import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.basepackage.function.TriFunction;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Iterable Extension
 *
 * <p>迭代器拓展</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/14 17:22
 * @since 1.1.6
 */
public interface IterableExt<T> extends Iterable<T> {

    /**
     * give the break, if  it returns false will break the loop
     *
     * <p>提供 break 方法</p>
     * <p>foreach((element ) -> {return true;})</p>
     *
     * @see Iterable#forEach(Consumer)
     */
    default void forEachCondition(Predicate<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            boolean continueOrBreak = action.test(t);
            if (!continueOrBreak) {
                break;
            }
        }
    }

    /**
     * give the return value to the loop and break the loop
     *
     * <p>if return null continue, else break and return </p>
     *
     * <p>提供 return 方法</p>
     * <p>foreach((element ) -> {return null;})</p>
     *
     * @see Iterable#forEach(Consumer)
     */
    default <R> R forEachRet(Function<? super T, R> action) {
        Objects.requireNonNull(action);
        for (T t : this) {
            R ret = action.apply(t);
            if (ret != null) {
                return ret;
            }
        }
        return null;
    }


    /**
     * give the index to the consumer
     *
     * <p>提供索引 index  方法</p>
     * <p> foreach((element , index) -> {})</p>
     *
     * @see Iterable#forEach(Consumer)
     */
    default void forEachIndex(BiConsumer<? super T, Integer> action) {
        Objects.requireNonNull(action);
        int index = 0;
        for (T t : this) {
            action.accept(t, index);
            index++;
        }
    }


    /**
     * give the index to the consumer. give the break, if  it returns false will break the loop
     *
     *
     * <p>提供索引 index  和 break 方法</p>
     * <p>foreach((element , index) -> {return true;})</p>
     *
     * @see #forEachIndex(BiConsumer)
     */
    default void forEachIndexCondition(BiPredicate<? super T, Integer> action) {
        Objects.requireNonNull(action);
        int index = 0;
        for (T t : this) {
            boolean continueOrBreak = action.test(t, index);
            if (!continueOrBreak) {
                break;
            }
            index++;
        }
    }


    /**
     * give the index and the return value to the loop and break the loop
     *
     * <p>if return null continue, else break and return </p>
     *
     * <p>提供索引 index  和 return 方法</p>
     * <p>foreach((element , index) -> {return null;})</p>
     *
     * @see #forEachIndex(BiConsumer)
     */
    default <R> R forEachIndexRet(BiFunction<? super T, Integer, R> action) {
        Objects.requireNonNull(action);
        int index = 0;
        for (T t : this) {
            R ret = action.apply(t, index);
            if (ret != null) {
                return ret;
            }
            index++;
        }
        return null;
    }


    // more


    /**
     * give the index and the return value to the loop and break the loop. and give the iterable list.
     *
     * <p>if return null continue, else break and return . And it needs to manually call next() iteration</p>
     *
     * <p>放入可迭代对象，提供索引 index , iterator 和 return 方法, 并且需要手动调用 next() 迭代</p>
     *
     * @see #forEachIndexRet(BiFunction)
     */
    default <R, C> R forEachIndexRetWith(
            @Nullable Iterable<C> Iterable,
            TriFunction<? super T, Integer, Iterator<C>, R> action) {
        Objects.requireNonNull(action);
        Iterator<C> iterator = null;
        if (Iterable != null) {
            iterator = Iterable.iterator();
        }
        int index = 0;
        for (T t : this) {
            R ret = action.apply(t, index, iterator);
            if (ret != null) {
                return ret;
            }
            index++;
        }
        return null;
    }

    /**
     * give the index and the return value to the loop and break the loop. and give the iterable list.
     *
     * <p>if return null continue, else break and return . And it needs to manually call next() iteration</p>
     *
     * <p>放入可迭代对象，提供索引 index , iterator 和 return 方法, 并且需要手动调用 next() 迭代</p>
     *
     * @see #forEachIndexRet(BiFunction)
     */
    default <R> R forEachIndexRetAll(
            @Nullable List<Iterable<?>> IterableList,
            TriFunction<? super T, Integer, List<Iterator<?>>, R> action) {
        Objects.requireNonNull(action);
        List<Iterator<?>> iterators = null;
        if (IterableList != null) {
            iterators = IterableList.stream().map(Iterable::iterator).collect(Collectors.toList());
        }
        int index = 0;
        for (T t : this) {
            R ret = action.apply(t, index, iterators);
            if (ret != null) {
                return ret;
            }
            index++;
        }
        return null;
    }


}
