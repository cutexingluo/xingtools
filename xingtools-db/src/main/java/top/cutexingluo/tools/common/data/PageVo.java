package top.cutexingluo.tools.common.data;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Page 简单封装
 *
 * <p>提供常规版本 {@link PageDetailVo}</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/4/26 20:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo<T> {
    protected List<T> rows;
    protected long total = 0; // 兼容Page

    /**
     * 不推荐使用，因为无法获得总数据量
     *
     * @param list 数据列表
     */
    @Deprecated
    public PageVo(@NotNull List<T> list) {
        this.rows = list;
        this.total = list.size();
    }

    public PageVo(@NotNull IPage<T> page) {
        this.rows = page.getRecords();
        this.total = page.getTotal();
    }

}
