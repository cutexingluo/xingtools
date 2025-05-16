package top.cutexingluo.tools.basepackage.checker;

/**
 * has present checker
 * <p>值检查器</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/5/16 14:24
 * @since 1.1.7
 */
@FunctionalInterface
public interface PresentChecker {

    /**
     * check present
     * <p>检查是否存在</p>
     *
     * @return true if present
     */
    boolean isPresent();
}
