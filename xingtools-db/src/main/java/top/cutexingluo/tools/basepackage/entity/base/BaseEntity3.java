package top.cutexingluo.tools.basepackage.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/1 18:24
 */
public class BaseEntity3<DateType> extends BaseEntity2<DateType> {
    /**
     * 删除标志
     */
    @TableField(value = "del_flag")
    protected Integer delFlag;
}
