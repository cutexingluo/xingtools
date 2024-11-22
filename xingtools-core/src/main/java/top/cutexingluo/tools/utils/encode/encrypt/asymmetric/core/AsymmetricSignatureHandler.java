package top.cutexingluo.tools.utils.encode.encrypt.asymmetric.core;

import org.jetbrains.annotations.NotNull;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * 非对称算法 加签验证处理器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/1 10:59
 * @since 1.1.6
 */
public interface AsymmetricSignatureHandler extends AsymmetricHandler {

    // Signature

    /**
     * 初始化Signature
     */
    Signature initSignature() throws Exception;

    /**
     * 直接加签
     *
     * @param data 待加签的数据
     * @param key  私钥
     * @return 加签后的数据
     */
    byte[] signDirect(byte[] data, PrivateKey key) throws Exception;

    /**
     * 直接验证
     *
     * @param data          待验证的数据
     * @param signatureData 签名数据
     * @param key           公钥
     * @return 验证结果
     */
    boolean verifyDirect(byte[] data, byte[] signatureData, PublicKey key) throws Exception;

    /**
     * 加签 (Base64编码)
     *
     * @param data 待加签的数据
     * @param key  私钥
     * @return 加签后的数据
     */
    default String signBySecurity(@NotNull String data, PrivateKey key) throws Exception {
        byte[] bytes = signDirect(data.getBytes(), key);
        return encodeBase64String(bytes);
    }

    /**
     * 验证 (Base64编码)
     *
     * @param data      待验证的数据
     * @param signature 签名数据
     * @param key       公钥
     * @return 验证结果
     */
    default boolean verifyBySecurity(@NotNull String data, String signature, PublicKey key) throws Exception {
        return verifyDirect(data.getBytes(), decodeBase64String(signature), key);
    }

}
