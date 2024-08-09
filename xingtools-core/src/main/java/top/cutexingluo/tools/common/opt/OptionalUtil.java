package top.cutexingluo.tools.common.opt;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Optional 工具类
 *
 * <p>可以在你的类文件里避免类的导入</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/24 17:59
 * @since 1.1.2
 */
public class OptionalUtil {

    /**
     * 如果target不为null，则执行consumer
     */
    public static <T> void ifPresent(T target, Consumer<T> consumer) {
        if (target != null) {
            consumer.accept(target);
        }
    }

    /**
     * 如果target不为null，则执行filter ，并返回filter执行值
     * <p>如果为 null 返回 null </p>
     */
    public static <T, R> R ifPresentFilter(T target, Function<T, R> filter) {
        if (target != null) {
            return filter.apply(target);
        }
        return null;
    }

    /**
     * 如果target为null，则执行 supplier 并返回
     *
     * @return supplier 执行值
     */
    public static <T> T absentDefault(T target, Supplier<T> supplier) {
        if (target == null) {
            target = supplier.get();
        }
        return target;
    }
}
