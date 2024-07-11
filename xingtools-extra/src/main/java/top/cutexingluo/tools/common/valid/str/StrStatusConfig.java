package top.cutexingluo.tools.common.valid.str;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/19 16:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StrStatusConfig {

    boolean notNull = true;

    boolean notBlankIfPresent = true;

    boolean lenLimit = false;

    int minLength = 0;

    int maxLength = Integer.MAX_VALUE;

    Set<String> anyStr = null;

    String[] anyReg = {};
}
