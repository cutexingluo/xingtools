package top.cutexingluo.tools.utils.encode.all;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.utils.encode.core.CryptHandler;
import top.cutexingluo.tools.utils.encode.digest.md5.Md5Handler;
import top.cutexingluo.tools.utils.encode.digest.sha.SHA256Handler;
import top.cutexingluo.tools.utils.encode.encrypt.asymmetric.dsa.DSAHandler;
import top.cutexingluo.tools.utils.encode.encrypt.asymmetric.ec.ECCHandler;
import top.cutexingluo.tools.utils.encode.encrypt.asymmetric.rsa.RSAHandler;
import top.cutexingluo.tools.utils.encode.encrypt.core.EncMeta;
import top.cutexingluo.tools.utils.encode.encrypt.symmetric.aes.AESHandler;
import top.cutexingluo.tools.utils.encode.encrypt.symmetric.des.DESHandler;

/**
 * 密码学综合工具类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 17:22
 * @since 1.1.6
 */
public class XTCryptUtil {


    /**
     * 根据指定的加密算法创建一个加密对象
     *
     * @param clazz 加密算法类
     * @param meta  加密算法参数
     * @return 加密对象
     */
    @Nullable
    public static <T extends CryptHandler> T createHandler(Class<T> clazz, @Nullable EncMeta meta) {
        if (clazz == null) {
            return null;
        } else if (Md5Handler.class.isAssignableFrom(clazz)) {
            return (T) Md5Handler.newInstance();
        } else if (SHA256Handler.class.isAssignableFrom(clazz)) {
            return (T) SHA256Handler.newInstance();
        } else if (AESHandler.class.isAssignableFrom(clazz)) {
            return (T) AESHandler.newInstance(meta);
        } else if (DESHandler.class.isAssignableFrom(clazz)) {
            return (T) DESHandler.newInstance(meta);
        } else if (RSAHandler.class.isAssignableFrom(clazz)) {
            return (T) RSAHandler.newInstance();
        } else if (DSAHandler.class.isAssignableFrom(clazz)) {
            return (T) DSAHandler.newInstance();
        } else if (ECCHandler.class.isAssignableFrom(clazz)) {
            return (T) ECCHandler.newInstance();
        }
        return null;
    }


    /**
     * 创建一个Md5对象
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static Md5Handler createMd5() {
        return Md5Handler.newInstance();
    }

    /**
     * 创建一个SHA256对象
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static SHA256Handler createSHA256() {
        return SHA256Handler.newInstance();
    }

    /**
     * 创建一个AES对象
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static AESHandler createAES(@Nullable EncMeta meta) {
        return AESHandler.newInstance(meta);
    }

    /**
     * 创建一个DES对象
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static DESHandler createDES(@Nullable EncMeta meta) {
        return DESHandler.newInstance(meta);
    }

    /**
     * 创建一个RSA对象
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static RSAHandler createRSA() {
        return RSAHandler.newInstance();
    }

    /**
     * 创建一个DSA对象
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static DSAHandler createDSA() {
        return DSAHandler.newInstance();
    }

    /**
     * 创建一个ECC对象
     * <p>需要导包 并 调用 init</p>
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public static ECCHandler createECC() {
        return ECCHandler.newInstance();
    }
}
