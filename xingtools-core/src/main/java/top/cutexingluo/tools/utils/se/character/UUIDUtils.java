package top.cutexingluo.tools.utils.se.character;


import java.util.UUID;

/**
 * UUID 普通工具类
 * <p>建议使用 UUID  官方, 阿里 ,hutool , ruoyi 的</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/2/9 17:24
 */
public class UUIDUtils {

    /**
     * 简单uuid
     */
    public static String simpleUUID() {
        return cn.hutool.core.lang.UUID.randomUUID().toString(true);
    }

    /**
     * java 简单uuid
     */
    public static String originSimpleUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
