package top.cutexingluo.tools.basepackage.checker;

/**
 * has ValueChecker
 *
 * <p>值检查器</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/5/16 14:33
 * @since 1.1.7
 */
public interface OptionalChecker extends ValueChecker {

    /**
     * check present
     * <p>检查是否存在/不为空</p>
     *
     * @return true if present
     */
    @Override
    default boolean isPresent() {
        return !isEmpty();
    }
}
