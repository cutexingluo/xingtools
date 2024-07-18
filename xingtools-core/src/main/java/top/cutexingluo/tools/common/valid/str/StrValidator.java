package top.cutexingluo.tools.common.valid.str;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import top.cutexingluo.tools.common.valid.Validator;

/**
 * String 检验器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/17 12:24
 * @since 1.1.1
 */
@Data
@AllArgsConstructor
public class StrValidator implements Validator<String> {

    /**
     * 条件
     */
    protected StrStatusConfig statusConfig;

    @Override
    public boolean isValid(String value) {
        if (value == null) {
            return !statusConfig.notNull;
        } else {

            // 1.非空判断
            if (statusConfig.notBlankIfPresent && StrUtil.isBlank(value)) { // 如果是空字符串，则不进行后续的匹配
                return false;
            }

            // 2.条件匹配
            int conditionCount = 0;
            // 先进行绝对匹配
            if (statusConfig.anyStr != null && !statusConfig.anyStr.isEmpty()) {
                conditionCount++;
                if (statusConfig.anyStr.contains(value)) {// optimize
                    return true;
                }
            }
            // 再进行正则匹配
            if (statusConfig.anyReg != null && statusConfig.anyReg.length > 0) {
                conditionCount++;
                for (String str : statusConfig.anyReg) {
                    if (ReUtil.isMatch(str, value)) {
                        return true;
                    }
                }
            }
            if (conditionCount != 0) { // 条件没通过
                return false;
            }

            // 3.长度限制
            if (statusConfig.lenLimit) {
                if (statusConfig.minLength >= 0 && value.length() < statusConfig.minLength) {
                    return false;
                }
                if (statusConfig.maxLength >= 0 && value.length() > statusConfig.maxLength) {
                    return false;
                }
            }
            // 直接通过
            return true;
        }

    }
}
