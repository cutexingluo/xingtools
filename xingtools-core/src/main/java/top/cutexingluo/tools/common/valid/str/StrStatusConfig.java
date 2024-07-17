package top.cutexingluo.tools.common.valid.str;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * String 状态校验配置
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/19 16:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StrStatusConfig {
    /**
     * 非空
     */
    boolean notNull = true;

    /**
     * 如果不为 null 则判断是否 不为 空字符串 ""
     */
    boolean notBlankIfPresent = true;

    /**
     * 长度限制
     */
    boolean lenLimit = false;
    /**
     * 长度限制
     * <p>最小长度(包含)</p>
     *
     * <p> min length (inclusive)</p>
     */
    int minLength = 0;
    /**
     * 长度限制
     * <p>最大长度(包含)</p>
     *
     * <p> max length (inclusive)</p>
     */
    int maxLength = Integer.MAX_VALUE;

    /**
     * 匹配字符
     */
    Set<String> anyStr = null;

    /**
     * 正则匹配
     */
    String[] anyReg = {};
}
