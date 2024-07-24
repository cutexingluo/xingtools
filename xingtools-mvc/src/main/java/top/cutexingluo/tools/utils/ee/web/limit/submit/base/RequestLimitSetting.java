package top.cutexingluo.tools.utils.ee.web.limit.submit.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.ee.web.limit.submit.strategy.LimitStrategy;

/**
 * 类注解信息存储类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/18 19:51
 */
@Data
@AllArgsConstructor
public class RequestLimitSetting {

    @NotNull
    protected RequestLimitConfig requestLimitConfig;

    protected LimitStrategy strategy;

    public RequestLimitSetting(@NotNull RequestLimitConfig requestLimitConfig) {
        this.requestLimitConfig = requestLimitConfig;
    }
}
