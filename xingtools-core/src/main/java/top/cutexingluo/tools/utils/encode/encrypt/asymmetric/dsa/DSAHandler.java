package top.cutexingluo.tools.utils.encode.encrypt.asymmetric.dsa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.encode.encrypt.asymmetric.core.AbstractAsymmetricSignatureHandler;
import top.cutexingluo.tools.utils.encode.base.EncType;

import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

/**
 * DSA 数字签名算法
 * <p>数字签名算法（DSA）是一种公开密钥数字签名算法，其公钥用于加密数据，私钥用于解密数据。DSA可以确保信息在传输过程中不被篡改，并验证信息的来源。</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/1 12:11
 * @since 1.1.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DSAHandler extends AbstractAsymmetricSignatureHandler {

    /**
     * 签名算法
     */
    protected String signAlgorithm = "SHA256withDSA";

    @NotNull
    @Contract(value = " -> new", pure = true)
    public static DSAHandler newInstance() {
        return new DSAHandler();
    }

    @Override
    public KeyPairGenerator initKeyPairGenerator() throws NoSuchAlgorithmException {
        this.keyPairGenerator = KeyPairGenerator.getInstance(EncType.DSA.getName());
        keyPairGenerator.initialize(1024); // 密钥长度
        return keyPairGenerator;
    }

    @Override
    public KeyFactory initKeyFactory() throws NoSuchAlgorithmException {
        this.keyFactory = KeyFactory.getInstance(EncType.DSA.getName());
        return keyFactory;
    }

    @Override
    public Signature initSignature() throws NoSuchAlgorithmException {
        this.signature = Signature.getInstance(signAlgorithm);
        return signature;
    }
}
