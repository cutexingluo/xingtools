package top.cutexingluo.tools.utils.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.cutexingluo.tools.utils.log.base.BaseLogConfig;
import top.cutexingluo.tools.utils.log.base.ILogConfig;
import top.cutexingluo.tools.utils.log.strategy.LogStrategy;

/**
 * XTLog  Config 基本配置
 *
 * <p>泛型表示 LogStrategy 提供给其实现类的   config 配置对象类型</p>
 *
 * <p>组装基类 </p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/10 11:02
 * @since 1.0.4
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class XTLogConfig<Strategy extends LogStrategy<T>, T extends BaseLogConfig> extends StrategyLogConfig implements ILogConfig<T> {

    /**
     * 日志打印策略
     *
     * <p>不能在任何 LogStrategy 的子类 实现方法里面 调用该属性并执行属性的方法，否则会死循环</p>
     */
    protected Strategy logStrategy;


    public XTLogConfig(int index, String key, int logPkg, int levelCode, String levelStr, Strategy logStrategy) {
        super(index, key, logPkg, levelCode, levelStr);
        this.logStrategy = logStrategy;
    }

    public XTLogConfig(int logPkg, int levelCode, Strategy logStrategy) {
        super(logPkg, levelCode);
        this.logStrategy = logStrategy;
    }

    public XTLogConfig(int logPkg, Strategy logStrategy) {
        super(logPkg);
        this.logStrategy = logStrategy;
    }

}
