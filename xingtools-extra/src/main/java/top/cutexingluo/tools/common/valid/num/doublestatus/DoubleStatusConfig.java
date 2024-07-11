package top.cutexingluo.tools.common.valid.num.doublestatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/12/8 18:47
 * @since 1.0.3
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoubleStatusConfig {

    boolean notNull = false;

    List<Double> matchNum = null;

    boolean limit = false;

    double eps = 1E-6;

    double min = 0;

    double max = Double.MAX_VALUE;

    DoubleRange[] range;


}
