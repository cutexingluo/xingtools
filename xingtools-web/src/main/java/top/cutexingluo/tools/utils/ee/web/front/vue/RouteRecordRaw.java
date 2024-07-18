package top.cutexingluo.tools.utils.ee.web.front.vue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * 兼容前端 vue-router 的 RouteRecordRaw 类型
 * <p>利于操作，推荐继承该类</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/2/12 12:40
 * @since 1.0.4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteRecordRaw<T> {
    /**
     * 路由路径
     * <p>不可为空</p>
     */
    protected String path;
    /**
     * 名称
     * <p>可空</p>
     */
    protected String name;
    /**
     * 文件路径
     * <p>推荐 String 类型</p>
     * <p>可空</p>
     */
    protected Object component;

    /**
     * multi 组件
     * <p>可以为 Map 类型</p>
     * <p>可空</p>
     */
    protected Object components;

    /**
     * 重定向
     * <p>建议 String 类型</p>
     * <p>可空</p>
     */
    protected Object redirect;

    /**
     * 别名
     * <p>支持String 和 String[] </p>
     * <p>可空</p>
     */
    protected Object alias;

    /**
     * 元数据
     * <p>可空</p>
     */
    protected T meta;

    /**
     * 参数
     * <p>Map 类型 或者 boolean 类型</p>
     * <p>可空</p>
     */
    protected Object props;


    /**
     * 子节点
     * <p>可空</p>
     */
    protected Collection<? extends RouteRecordRaw<T>> children;


    /**
     * 必填项
     * <p>建议使用</p>
     *
     * @param path 路由路径
     */
    public RouteRecordRaw(String path) {
        this.path = path;
    }

    /**
     * 常规项
     */
    public RouteRecordRaw(String path, Object component) {
        this.path = path;
        this.component = component;
    }

    /**
     * 常规项
     */
    public RouteRecordRaw(String path, String name, Object component) {
        this.path = path;
        this.name = name;
        this.component = component;
    }

}
