package top.cutexingluo.tools.basepackage.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/1 18:15
 */
public class BaseEntity2<DateType> extends BaseEntity1<DateType> {
    /**
     * 创建者
     */
    @TableField("create_by")
    protected DateType createBy;

    /**
     * 更新者
     */
    @TableField("update_by")
    protected DateType updateBy;
}
