package top.cutexingluo.tools.common.base;


/**
 * message 字符串单接口
 * <p>* 返回数据接口</p>
 * <p>仅提供 getMsg 方法接口</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/7/13 20:33
 */
@FunctionalInterface
public interface IR {

    /**
     * 获取信息/消息
     *
     * @return the msg
     */
    String getMsg();

}
