package top.cutexingluo.tools.utils.encrypt.asymmetric.core;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 公钥私钥 抽象接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 15:57
 */
public interface AsymmetricHelper {

    /**
     * 获取公钥 (base64 编码)
     */
    String getPublicKey();

    /**
     * 获取私钥 (base64 编码)
     */
    String getPrivateKey();

    /**
     * 获取非对称密钥处理器
     */
    AsymmetricHandler getAsymmetricHandler();

    /**
     * 从配置生成新的公私钥对
     */
    default KeyPair newKeyPair() throws Exception {
        AsymmetricHandler handler = getAsymmetricHandler();
        PublicKey publicKey = handler.getPublicKeyByBase64(getPublicKey());
        PrivateKey privateKey = handler.getPrivateKeyByBase64(getPrivateKey());
        return new KeyPair(publicKey, privateKey);
    }
}
