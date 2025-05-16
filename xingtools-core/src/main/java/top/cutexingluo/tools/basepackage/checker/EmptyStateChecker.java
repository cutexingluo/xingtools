package top.cutexingluo.tools.basepackage.checker;

/**
 * has empty and not empty check
 * <p>空/非空 值检查器</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/5/16 15:11
 */
public interface EmptyStateChecker extends EmptyChecker {

    /**
     * check not empty
     * <p>检查是否不为空</p>
     *
     * @return true if not empty
     */
    boolean isNotEmpty();
}
