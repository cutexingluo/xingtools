package top.cutexingluo.tools.utils.encode.core;

import java.security.MessageDigest;

/**
 * 摘要算法处理器
 *
 * <p>Message Digest 信息摘要</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 10:13
 * @since 1.1.6
 */
public interface DigestHandler extends CryptHandler {

    // MessageDigest

    /**
     * 初始化 MessageDigest 对象
     */
    MessageDigest initMessageDigest() throws Exception;


    /**
     * 使用默认方式进行摘要
     *
     * <p>核心方法</p>
     *
     * @param data 原数据
     * @return 摘要结果
     */
    byte[] encodeBySecurity(byte[] data) throws Exception;

    /**
     * 对 String 使用默认方式进行摘要
     */
    default byte[] encodeBySecurity(String data) throws Exception {
        return encodeBySecurity(data.getBytes());
    }


    /**
     * 校验摘要是否正确
     *
     * @param data    原数据
     * @param encoded 摘要后的数据
     * @return 是否正确
     */
    boolean validateBySecurity(byte[] data, CharSequence encoded) throws Exception;

    /**
     * 对 String  校验摘要是否正确
     *
     * @return 是否正确
     */
    default boolean validateBySecurity(String data, CharSequence encoded) throws Exception {
        return validateBySecurity(data.getBytes(), encoded);
    }
}
