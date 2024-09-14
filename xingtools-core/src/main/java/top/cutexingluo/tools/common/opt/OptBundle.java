package top.cutexingluo.tools.common.opt;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.base.IDataValue;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * OptBundle 类
 * <p>类似 Opt 或 Optional</p>
 * <p>可用于执行链策略, 责任链设计模式</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/22 17:04
 * @since 1.0.4
 */
@Data
public class OptBundle<T, Meta> implements IDataValue<T> {

    /**
     * 执行函数
     */
    public interface OptAction<T, Meta> extends Function<OptData<T, Meta>, OptRes<T>> {
    }

    /**
     * 执行消费者
     */
    public interface OptConsumer<T, Meta> extends Consumer<OptData<T, Meta>> {
    }

    /**
     * 执行条件
     *
     * @since 1.0.5
     */
    public interface OptCondition<T, Meta> extends Function<OptData<T, Meta>, Boolean> {
    }


    /**
     * 提供者
     *
     * @since 1.0.5
     */
    public interface OptGetter<T, Meta, R> extends Function<OptData<T, Meta>, R> {
    }


    @NotNull
    protected OptData<T, Meta> data;

    /**
     * @param data data 不能为 null
     */
    public OptBundle(@NotNull OptData<T, Meta> data) {
        this.data = data;
    }


    /**
     * 获取内部的值
     */
    @Override
    public T data() {
        return data.value;
    }

    public boolean hasValue() {
        return data.value != null;
    }

    public boolean hasMeta() {
        return data.meta != null;
    }

    public boolean hasType(Class<?> type) {
        return type != null && type.isInstance(data.value);
    }

    /**
     * 比较clazz 变量
     */
    public boolean hasClazz(Class<?> type) {
        return data.clazz != null && data.clazz.equals(type);
    }


    /**
     * 比较clazz 变量
     * <p>clazz 是否是 type 子类</p>
     */
    public boolean isSubClazzFrom(Class<?> type) {
        return type != null && type.isAssignableFrom(data.clazz);
    }

    /**
     * 获取值
     *
     * @since 1.0.5
     */
    @Override
    public T getValue() {
        return data.value;
    }

    /**
     * 获取 Meta
     *
     * @since 1.0.5
     */
    public Meta getMeta() {
        return data.meta;
    }


    public void update(Object obj, boolean updateClazzIfPresent) {
        data.update(obj, updateClazzIfPresent);
    }

    public void updateCheckPresent(Object obj, boolean updateClazzIfPresent) {
        data.updateCheckPresent(obj, updateClazzIfPresent);
    }

    // then

    /**
     * then 方法, 返回值会赋值
     *
     * @param updateClazzIfPresent true=> clazz 存在才赋值 clazz 变量
     */
    public OptBundle<T, Meta> then(boolean updateClazzIfPresent, OptAction<T, Meta> action) {
        if (action != null) {
            OptRes<T> res = action.apply(data);
            data.update(res, updateClazzIfPresent);
        }
        return this;
    }

    public OptBundle<T, Meta> then(OptAction<T, Meta> action) {
        return then(false, action);
    }

    /**
     * thenCheckPresent 方法 res 不为空才赋值
     *
     * @param updateClazzIfPresent true=> clazz 存在才赋值 clazz 变量
     */
    public OptBundle<T, Meta> thenCheckPresent(boolean updateClazzIfPresent, OptAction<T, Meta> action) {
        if (action != null) {
            OptRes<T> res = action.apply(data);
            data.updateCheckPresent(res, updateClazzIfPresent);
        }
        return this;
    }

    public OptBundle<T, Meta> thenCheckPresent(OptAction<T, Meta> action) {
        return thenCheckPresent(false, action);
    }


    // pick

    /**
     * pick 读取数据
     */
    public OptBundle<T, Meta> pick(OptConsumer<T, Meta> consumer) {
        if (consumer != null) {
            consumer.accept(data);
        }
        return this;
    }

    /**
     * pick 读取数据
     * <p>value 存在才读取数据</p>
     */
    public OptBundle<T, Meta> filterPick(OptConsumer<T, Meta> consumer) {
        if (consumer != null && hasValue()) {
            consumer.accept(data);
        }
        return this;
    }

    /**
     * pick 读取数据
     * <p>value 不存在才读取数据</p>
     */
    public OptBundle<T, Meta> emptyPick(OptConsumer<T, Meta> consumer) {
        if (consumer != null && !hasValue()) {
            consumer.accept(data);
        }
        return this;
    }

    // then filter

    /**
     * filterThen 方法
     * <p>value 存在才执行函数</p>
     * <p>返回值会赋值</p>
     *
     * @param updateClazzIfPresent true=> clazz 存在才赋值 clazz 变量
     */
    public OptBundle<T, Meta> filterThen(boolean updateClazzIfPresent, OptAction<T, Meta> action) {
        if (action != null && hasValue()) {
            OptRes<T> res = action.apply(data);
            data.update(res, updateClazzIfPresent);
        }
        return this;
    }

    public OptBundle<T, Meta> filterThen(OptAction<T, Meta> action) {
        return filterThen(false, action);
    }

    /**
     * filterThen 方法
     * <p>value 存在才执行函数</p>
     * <p>返回值 value 存在才会赋值</p>
     *
     * @param updateClazzIfPresent true=> clazz 存在才赋值 clazz 变量
     */
    public OptBundle<T, Meta> filterThenPresent(boolean updateClazzIfPresent, OptAction<T, Meta> action) {
        if (action != null && hasValue()) {
            OptRes<T> res = action.apply(data);
            data.updateCheckPresent(res, updateClazzIfPresent);
        }
        return this;
    }

    public OptBundle<T, Meta> filterThenPresent(OptAction<T, Meta> action) {
        return filterThenPresent(false, action);
    }


    /**
     * emptyThen 方法
     * <p>value 不存在才执行函数</p>
     * <p>返回值会赋值</p>
     *
     * @param updateClazzIfPresent true=> clazz 存在才赋值 clazz 变量
     */
    public OptBundle<T, Meta> emptyThen(boolean updateClazzIfPresent, OptAction<T, Meta> action) {
        if (action != null && !hasValue()) {
            OptRes<T> res = action.apply(data);
            data.update(res, updateClazzIfPresent);
        }
        return this;
    }

    public OptBundle<T, Meta> emptyThen(OptAction<T, Meta> action) {
        return emptyThen(false, action);
    }


    /**
     * emptyThen 方法
     * <p>value 不存在才执行函数</p>
     * <p>返回值 value 存在才会赋值</p>
     *
     * @param updateClazzIfPresent true=> clazz 存在才赋值 clazz 变量
     */
    public OptBundle<T, Meta> emptyThenPresent(boolean updateClazzIfPresent, OptAction<T, Meta> action) {
        if (action != null && !hasValue()) {
            OptRes<T> res = action.apply(data);
            data.updateCheckPresent(res, updateClazzIfPresent);
        }
        return this;
    }

    public OptBundle<T, Meta> emptyThenPresent(OptAction<T, Meta> action) {
        return emptyThenPresent(false, action);
    }


    /**
     * 有条件 pick
     * <p>根据条件执行消费者</p>
     *
     * @param condition 条件
     * @param consumer  消费者
     * @since 1.0.5
     */
    public OptBundle<T, Meta> conditionalPick(boolean condition, OptConsumer<T, Meta> consumer) {
        if (condition && consumer != null) {
            consumer.accept(data);
        }
        return this;
    }

    /**
     * 有条件 pick
     * <p>根据条件执行消费者</p>
     *
     * @param condition 条件
     * @param consumer  消费者
     * @since 1.0.5
     */
    public OptBundle<T, Meta> conditionalPick(OptCondition<T, Meta> condition, OptConsumer<T, Meta> consumer) {
        if (condition != null && Boolean.TRUE.equals(condition.apply(data)) && consumer != null) {
            consumer.accept(data);
        }
        return this;
    }

    /**
     * 有条件 pick
     * <p>根据条件执行消费者</p>
     *
     * @param getter      目标值获取器
     * @param retValCheck 返回值检查 （如果为空，依然会执行消费者）
     * @param consumer    消费者
     * @since 1.0.5
     */
    public <R> OptBundle<T, Meta> conditionalPick(OptGetter<T, Meta, R> getter, Function<R, Boolean> retValCheck, OptConsumer<T, Meta> consumer) {
        if (getter != null) {
            R retVal = getter.apply(data);
            if (retValCheck == null || Boolean.TRUE.equals(retValCheck.apply(retVal))) {
                consumer.accept(data);
            }
        }
        return this;
    }

    /**
     * 有条件 pick
     * <p>根据条件执行消费者, list 列表需一一对应</p>
     *
     * @param listGetter      目标值列表获取器
     * @param retValCheckList 返回值检查 （如果为空，依然会执行消费者；并且如果list某个元素为空或者越界，则依然会执行对应消费者）
     * @param consumerList    消费者列表
     * @since 1.0.5
     */
    public OptBundle<T, Meta> conditionalPick(OptGetter<T, Meta, List<Object>> listGetter, List<Function<Object, Boolean>> retValCheckList, List<OptConsumer<T, Meta>> consumerList) {
        if (listGetter != null) {
            List<Object> retList = listGetter.apply(data);
            if (retList == null) return this; // return;
            if (consumerList == null) return this; // return
            if (retValCheckList == null) { // no retValCheckList
                for (int i = 0; i < retList.size() && i < consumerList.size(); i++) {
                    OptConsumer<T, Meta> optConsumer = consumerList.get(i);
                    if (optConsumer != null) {
                        optConsumer.accept(data);
                    }
                }
            } else {
                for (int i = 0; i < retList.size() && i < consumerList.size(); i++) {
                    Object ret = retList.get(i);
                    boolean runFlag = true;
                    if (i < retValCheckList.size()) {
                        Function<Object, Boolean> function = retValCheckList.get(i);
                        if (function != null) {
                            runFlag = function.apply(ret);
                        }
                    }
                    if (!runFlag) continue; // pass
                    OptConsumer<T, Meta> optConsumer = consumerList.get(i);
                    if (optConsumer != null) {
                        optConsumer.accept(data);
                    }
                }
            }
        }
        return this;
    }


    /**
     * 有条件 pick
     * <p>根据条件执行消费者, list 列表需一一对应</p>
     *
     * @param listGetter   目标值列表获取器
     * @param retValCheck  统一返回值检查 （如果为空，依然会执行消费者）
     * @param consumerList 消费者列表
     * @since 1.0.5
     */
    public OptBundle<T, Meta> conditionalPick(OptGetter<T, Meta, List<Object>> listGetter, Function<Object, Boolean> retValCheck, List<OptConsumer<T, Meta>> consumerList) {
        if (listGetter != null) {
            List<Object> retList = listGetter.apply(data);
            if (retList == null) return this; // return;
            if (consumerList == null) return this; // return
            if (retValCheck == null) { // no retValCheck
                for (int i = 0; i < retList.size() && i < consumerList.size(); i++) {
                    OptConsumer<T, Meta> optConsumer = consumerList.get(i);
                    if (optConsumer != null) {
                        optConsumer.accept(data);
                    }
                }
            } else {
                for (int i = 0; i < retList.size() && i < consumerList.size(); i++) {
                    Object ret = retList.get(i);
                    boolean runFlag = retValCheck.apply(ret);
                    if (!runFlag) continue; // pass
                    OptConsumer<T, Meta> optConsumer = consumerList.get(i);
                    if (optConsumer != null) {
                        optConsumer.accept(data);
                    }
                }
            }
        }
        return this;
    }
    //------ 语义区 ------- methods --------

    /**
     * alias  filterThenPresent 方法
     * <pre>
     *     语义是：
     *    * 如果不为空才进行下去
     *    * 返回值不为空则赋值
     *    * alias filterThenPresent
     *    * 语义为继续，代表语句继续执行
     * </pre>
     */
    public OptBundle<T, Meta> checkThen(OptAction<T, Meta> action) {
        return this.filterThenPresent(true, action);
    }

    /**
     * alias  filterThen 方法
     * <pre>
     *     语义是：
     *    * 如果不为空才进行下去
     *    * 返回值无论怎样都赋值 (替换)
     *    * alias filterThen
     *    * 语义为继续，代表语句继续执行
     * </pre>
     */
    public OptBundle<T, Meta> checkThenReplace(OptAction<T, Meta> action) {
        return this.filterThen(true, action);
    }

    /**
     * alias  emptyThenPresent 方法
     * <pre>
     *     语义是：
     *    * 如果为空才进行下去
     *    *  返回值不为空则赋值
     *    * alias emptyThenPresent
     *    * 语义为合并，代表语句重新开始
     * </pre>
     */
    public OptBundle<T, Meta> combineEmpty(OptAction<T, Meta> action) {
        return this.emptyThenPresent(true, action);
    }


    /**
     * alias  emptyThen 方法
     * <pre>
     *     语义是：
     *    * 如果为空才进行下去
     *    * 返回值无论怎样都赋值 (替换)
     *    * alias emptyThen
     *    * 语义为合并，代表语句重新开始
     * </pre>
     */
    public OptBundle<T, Meta> combineEmptyReplace(OptAction<T, Meta> action) {
        return this.emptyThen(true, action);
    }


    /**
     * 如果目标为空，则执行
     *
     * @param target   目标
     * @param consumer 消费者
     * @since 1.0.5
     */
    public OptBundle<T, Meta> ifAbsentPick(Object target, OptConsumer<T, Meta> consumer) {
        return conditionalPick(target == null, consumer);
    }


    /**
     * 判断获取器返回值是否为空，则执行
     *
     * @param getter       获取器
     * @param consumer     消费者
     * @param checkPresent 检查存在 （true-> getter 值不为空则执行，false -> getter 值为空则执行）
     * @since 1.0.5
     */
    public <R> OptBundle<T, Meta> ifRetCheckPick(OptGetter<T, Meta, R> getter, boolean checkPresent, OptConsumer<T, Meta> consumer) {
        return conditionalPick(getter, retVal -> checkPresent && retVal != null || !checkPresent && retVal == null, consumer);
    }

    /**
     * 如果获取器返回值为空，则执行
     *
     * @param getter   获取器
     * @param consumer 消费者
     * @since 1.0.5
     */
    public <R> OptBundle<T, Meta> ifRetAbsentPick(OptGetter<T, Meta, R> getter, OptConsumer<T, Meta> consumer) {
        return ifRetCheckPick(getter, false, consumer);
    }


    /**
     * 如果获取器返回值不为空，则执行
     *
     * @param getter   获取器
     * @param consumer 消费者
     * @since 1.0.5
     */
    public <R> OptBundle<T, Meta> ifRetPresentPick(OptGetter<T, Meta, R> getter, OptConsumer<T, Meta> consumer) {
        return ifRetCheckPick(getter, true, consumer);
    }


    /**
     * 判断获取器返回值是否为空，则执行
     * <p>返回值和列表一一对应</p>
     *
     * @param listGetter   返回值列表获取器
     * @param consumerList 列表消费者
     * @param checkPresent 检查存在 （true-> getter 值不为空则执行，false -> getter 值为空则执行）
     * @since 1.0.5
     */
    public <R> OptBundle<T, Meta> ifRetListCheckPick(OptGetter<T, Meta, List<Object>> listGetter, boolean checkPresent, List<OptConsumer<T, Meta>> consumerList) {
        return conditionalPick(listGetter, retVal -> checkPresent && retVal != null || !checkPresent && retVal == null, consumerList);
    }

    /**
     * 如果获取器返回值为空，则执行
     * <p>返回值和列表一一对应</p>
     *
     * @param listGetter   返回值列表获取器
     * @param consumerList 列表消费者
     * @since 1.0.5
     */
    public <R> OptBundle<T, Meta> ifRetListAbsentPick(OptGetter<T, Meta, List<Object>> listGetter, List<OptConsumer<T, Meta>> consumerList) {
        return ifRetListCheckPick(listGetter, false, consumerList);
    }


    /**
     * 如果获取器返回值不为空，则执行
     * <p>返回值和列表一一对应</p>
     *
     * @param listGetter   返回值列表获取器
     * @param consumerList 列表消费者
     * @since 1.0.5
     */
    public <R> OptBundle<T, Meta> ifRetListPresentPick(OptGetter<T, Meta, List<Object>> listGetter, List<OptConsumer<T, Meta>> consumerList) {
        return ifRetListCheckPick(listGetter, true, consumerList);
    }

}
