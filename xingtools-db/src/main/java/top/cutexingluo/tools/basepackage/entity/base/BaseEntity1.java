package top.cutexingluo.tools.basepackage.entity.base;

import com.baomidou.mybatisplus.annotation.TableField;

/**
 * 实体类，时间父类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/1 18:12
 */
public class BaseEntity1<DateType> {
    /**
     * 创建时间
     */
    @TableField("create_time")
    protected DateType createTime;

    /**
     * 更新时间
     */
    @TableField("create_time")
    protected DateType updateTime;
}
