package top.cutexingluo.tools.common.data;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * Page 常规封装
 * <p>支持任意类型</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/5/23 14:35
 * @since 1.1.7
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageObjVo<O> {

    protected O rows;
    protected long total = 0; // 兼容Page


    public PageObjVo(@NotNull O object) {
        this.rows = object;
    }

    public <T> PageObjVo(@NotNull IPage<T> page) {
        this.rows = (O) page.getRecords();
        this.total = page.getTotal();
    }

    /**
     * 同类型赋值
     */
    public PageObjVo(@NotNull PageObjVo<O> pageVo) {
        this.rows = pageVo.getRows();
        this.total = pageVo.getTotal();
    }
}
