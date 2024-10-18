package top.cutexingluo.tools.utils.encrypt.asymmetric.rsa;

import top.cutexingluo.tools.utils.encrypt.asymmetric.AsymmetricHelper;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

/**
 * RSA 公钥私钥 抽象接口
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 16:00
 */
public interface RSAHelper extends AsymmetricHelper {

    /**
     * 获取 RSA 处理器
     */
    default RSAHandler getRSAHandler() {
        return RSAHandler.newInstance();
    }

    @Override
    default KeyPair newKeyPair() throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAHandler rsaHandler = getRSAHandler();
        PublicKey publicKey = rsaHandler.getPublicKeyByBase64(getPublicKey());
        PrivateKey privateKey = rsaHandler.getPrivateKeyByBase64(getPrivateKey());
        return new KeyPair(publicKey, privateKey);
    }
}
