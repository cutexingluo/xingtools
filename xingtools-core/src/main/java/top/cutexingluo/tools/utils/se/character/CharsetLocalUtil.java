package top.cutexingluo.tools.utils.se.character;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 字符本地编码工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/9/15 14:10
 */
public class CharsetLocalUtil {
    /**
     * 加载资源文件
     *
     * @return {@link ResourceBundle}
     */
    public static ResourceBundle getBundle(String path) {
        return ResourceBundle.getBundle(path);
    }

    /**
     * 加载资源文件
     *
     * @return {@link ResourceBundle}
     */
    public static ResourceBundle getBundle(String path, Locale locale) {
        return ResourceBundle.getBundle(path, locale);
    }
}
