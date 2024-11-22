package top.cutexingluo.tools.utils.encode.digest.md5;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.encode.base.EncType;
import top.cutexingluo.tools.utils.encode.core.AbstractDigestHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * md5  信息摘要算法
 * <p>密码散列函数</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 15:14
 * @since 1.1.6
 */
public class Md5Handler extends AbstractDigestHandler {

    @Contract(value = " -> new", pure = true)
    public static @NotNull
    Md5Handler newInstance() {
        return new Md5Handler();
    }

    // interface
    @Override
    public MessageDigest initMessageDigest() throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance(EncType.MD5.getName());
        return messageDigest;
    }

    /**
     * 使用Security 并格式化
     *
     * @param data 数据
     */
    public String encodeBySecurityFormat(@NotNull String data) {
        byte[] digest = this.encodeBySecurity(data);
        Formatter formatter = new Formatter();
        for (byte b : digest) { // 补齐前导0
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

}
