package top.cutexingluo.tools.utils.encode.encrypt.asymmetric.ec;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.utils.encode.encrypt.asymmetric.core.AbstractAsymmetricCipherHandler;
import top.cutexingluo.tools.utils.encode.base.EncType;
import top.cutexingluo.tools.utils.encode.core.CipherKeyHandler;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

/**
 * ECC 非对称加密算法
 * <p>椭圆曲线密码算法（Elliptic Curve Cryptography，ECC）是一种基于椭圆曲线数学原理的加解密算法，具有较高的安全性和较小的计算量。在Java中，我们可以使用Bouncy Castle库来实现ECC加密算法。</p>
 * <p><b>需导入 org.bouncycastle:bcprov-jdk15on , 并且使用前需调一次 ECCHandler.init() </b></p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/31 16:44
 * @since 1.1.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ECCHandler extends AbstractAsymmetricCipherHandler<Key> implements CipherKeyHandler {


    /**
     * 标准曲线名称
     */
    public static final String CURVE_NAME = "secp256r1";

    /**
     * 标准转换
     */
    public static final String TRANSFORMATION = "ECIES";

    /**
     * 初始化
     * <p>使用前 必须调用</p>
     */
    public static void init() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    public static ECCHandler newInstance() {
        return new ECCHandler();
    }

    @Override
    public KeyPairGenerator initKeyPairGenerator() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchProviderException {
        // 初始化密钥对生成器
        keyPairGenerator = KeyPairGenerator.getInstance(EncType.EC.getName(), EncType.BC.getName());
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(CURVE_NAME);
        keyPairGenerator.initialize(ecSpec);
        return keyPairGenerator;
    }

    @Override
    public KeyFactory initKeyFactory() throws NoSuchAlgorithmException, NoSuchProviderException {
        this.keyFactory = KeyFactory.getInstance(EncType.EC.getName(), EncType.BC.getName());
        return keyFactory;
    }

    @Override
    public Cipher initCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        this.cipher = Cipher.getInstance(TRANSFORMATION, EncType.BC.getName());
        return cipher;
    }


}
