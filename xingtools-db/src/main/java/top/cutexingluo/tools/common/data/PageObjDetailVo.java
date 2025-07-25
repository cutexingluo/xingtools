package top.cutexingluo.tools.common.data;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Page 扩展封装
 *
 * <p>根据需求, 扩展PageDetailVo , 并且兼容mp Page 对象 和 任意对象</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/5/23 14:44
 * @since 1.1.7
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageObjDetailVo<T> extends PageObjVo<T> implements IPageDetail {

    /**
     * 当前页 , 第几页 - current
     */
    protected long pageNum = 1;

    /**
     * 页大小 -  size
     */
    protected long pageSize = 10;


    /**
     * 使用 page 其他参数填充
     */
    public PageObjDetailVo(T rows, @NotNull IPage<?> page) {
        this(rows, page.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 推荐
     */
    public PageObjDetailVo(@NotNull IPage<T> page) {
        super(page);
        this.pageNum = page.getCurrent();
        this.pageSize = page.getSize();
    }

    public PageObjDetailVo(@NotNull IPage<T> page, long pageNum, long pageSize) {
        super(page);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageObjDetailVo(T rows, long total, long pageNum, long pageSize) {
        this.rows = rows;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    /**
     * 其他参数赋值
     * <p>除了数据外, 其他参数的赋值</p>
     *
     * @since 1.1.5
     */
    public PageObjDetailVo(T rows, @NotNull IPageDetail pageDetail) {
        this(rows, pageDetail.getTotal(), pageDetail.getPageNum(), pageDetail.getPageSize());
    }

}
