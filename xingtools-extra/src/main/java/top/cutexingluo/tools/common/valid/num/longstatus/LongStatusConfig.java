package top.cutexingluo.tools.common.valid.num.longstatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 16:12
 * @since 1.0.3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LongStatusConfig {

    boolean notNull = false;

    Set<Long> matchNum = null;

    boolean limit = false;

    long min = 0;

    long max = Long.MAX_VALUE;

    LongRange[] range;


}
