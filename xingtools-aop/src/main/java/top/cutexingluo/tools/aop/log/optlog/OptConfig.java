package top.cutexingluo.tools.aop.log.optlog;

import lombok.Data;

/**
 * OptLogAdapter 中修改 OptConfig
 * <p>可以对某个方法进行单独配置，提高灵活性</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/18 16:50
 */
@Data
public class OptConfig {
    /**
     * 当前方法是否开启本次操作
     * <p>若关闭，则 runBody 和 afterRun 以及以下配置 均不会执行，直接返回方法原来的返回值</p>
     */
    boolean enabled = true;

    /**
     * 是否返回 null
     * <p>若开启，则 runBody 和 afterRun 均不会执行，直接返回 null </p>
     */
    boolean returnNull = false;


    /**
     * 手动指定方法返回值
     * <p>启用前需关闭 returnNull </p>
     * <p>若该项不为 null ，则 runBody 和 afterRun 均不会执行，直接返回 指定的返回值</p>
     */
    Object returnValue = null;
}
