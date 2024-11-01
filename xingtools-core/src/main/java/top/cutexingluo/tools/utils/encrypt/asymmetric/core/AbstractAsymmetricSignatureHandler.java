package top.cutexingluo.tools.utils.encrypt.asymmetric.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import java.security.*;
import java.util.Objects;

/**
 * 非对称算法 加签验证处理器 抽象类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/1 11:59
 * @since 1.1.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class AbstractAsymmetricSignatureHandler extends AbstractAsymmetricHandler implements AsymmetricSignatureHandler {

    /**
     * 签名对象
     */
    protected Signature signature;

    protected void checkSignature() {
        Objects.requireNonNull(signature,
                "signature should not be null, please call initSignature() first or set it directly");
    }

    @Override
    public byte[] signDirect(byte[] data, PrivateKey key) throws SignatureException, InvalidKeyException {
        checkSignature();
        Signature signature = this.signature;
        signature.initSign(key);
        signature.update(data);
        return signature.sign();
    }

    @Override
    public boolean verifyDirect(byte[] data, byte[] signatureData, PublicKey key) throws InvalidKeyException, SignatureException {
        checkSignature();
        Signature signature = this.signature;
        signature.initVerify(key);
        signature.update(data);
        return signature.verify(signatureData);
    }

    @Override
    public String signBySecurity(@NotNull String data, PrivateKey key) throws SignatureException, InvalidKeyException {
        byte[] bytes = signDirect(data.getBytes(), key);
        return encodeToStringBase64(bytes);
    }

    @Override
    public boolean verifyBySecurity(@NotNull String data, String signature, PublicKey key) throws SignatureException, InvalidKeyException {
        return verifyDirect(data.getBytes(), decodeBase64(signature), key);
    }
}
