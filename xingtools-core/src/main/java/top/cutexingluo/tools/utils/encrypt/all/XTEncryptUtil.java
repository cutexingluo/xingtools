package top.cutexingluo.tools.utils.encrypt.all;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.encrypt.asymmetric.rsa.RSAHandler;
import top.cutexingluo.tools.utils.encrypt.hash.md5.Md5;
import top.cutexingluo.tools.utils.encrypt.hash.sha.SHA256;
import top.cutexingluo.tools.utils.encrypt.symmetric.aes.AESHandler;
import top.cutexingluo.tools.utils.encrypt.symmetric.des.DESHandler;

/**
 * 加密综合工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 17:22
 * @since 1.1.6
 */
public class XTEncryptUtil {

    /**
     * 创建一个Md5对象
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static Md5 createMd5() {
        return Md5.newInstance();
    }

    /**
     * 创建一个SHA256对象
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static SHA256 createSHA256() {
        return SHA256.newInstance();
    }


    /**
     * 创建一个DES对象
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static DESHandler createDES() {
        return DESHandler.newInstance();
    }

    /**
     * 创建一个AES对象
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static AESHandler createAES() {
        return AESHandler.newInstance();
    }

    /**
     * 创建一个RSA对象
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static RSAHandler newRSA() {
        return RSAHandler.newInstance();
    }
}
