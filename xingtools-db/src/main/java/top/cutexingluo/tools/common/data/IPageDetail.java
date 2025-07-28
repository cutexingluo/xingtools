package top.cutexingluo.tools.common.data;

/**
 * page 通用方法接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/5/23 14:41
 * @since 1.1.7
 */
public interface IPageDetail {

    /**
     * 获取数据总量
     */
    long getTotal();

    /**
     * 获取当前页码
     */
    long getPageNum();

    /**
     * 获取页大小
     */
    long getPageSize();
}
