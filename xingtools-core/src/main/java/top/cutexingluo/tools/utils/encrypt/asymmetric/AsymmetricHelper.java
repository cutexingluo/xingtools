package top.cutexingluo.tools.utils.encrypt.asymmetric;

import java.security.KeyPair;

/**
 * 公钥私钥 抽象接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 15:57
 */
public interface AsymmetricHelper {

    /**
     * 获取公钥
     */
    String getPublicKey();

    /**
     * 获取私钥
     */
    String getPrivateKey();

    /**
     * 生成新的公私钥对
     */
    KeyPair newKeyPair() throws Exception;
}
