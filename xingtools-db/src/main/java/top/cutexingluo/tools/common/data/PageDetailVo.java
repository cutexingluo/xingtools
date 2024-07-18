package top.cutexingluo.tools.common.data;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Page 扩展封装
 *
 * <p>根据需求, 新增PageDetailVo , 并且兼容mp Page 对象</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/4 18:08
 * @since 1.0.4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDetailVo<T> extends PageVo<T> {

    /**
     * 当前页 , 第几页 - current
     */
    protected long pageNum = 1;

    /**
     * 页大小 -  size
     */
    protected long pageSize = 10;

    public PageDetailVo(List<T> rows, long total) {
        super(rows, total);
    }

    /**
     * 使用 page 其他参数填充
     */
    public PageDetailVo(List<T> rows, @NotNull IPage<?> page) {
        this(rows, page.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 推荐
     */
    public PageDetailVo(@NotNull IPage<T> page) {
        super(page);
        this.pageNum = page.getCurrent();
        this.pageSize = page.getSize();
    }

    public PageDetailVo(@NotNull IPage<T> page, long pageNum, long pageSize) {
        super(page);
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public PageDetailVo(List<T> rows, long total, long pageNum, long pageSize) {
        this.rows = rows;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
