package top.cutexingluo.tools.utils.se.file.pkg;

/**
 * XTFile 基本接口
 * <p>目的是返回文件名</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/2 17:39
 */
@FunctionalInterface
public interface XTFileBaseSource {

    /**
     * @return 获取文件名
     */
    String getFilename();
}
