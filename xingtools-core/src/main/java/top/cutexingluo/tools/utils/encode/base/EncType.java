package top.cutexingluo.tools.utils.encode.base;

/**
 * 算法类型
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/10/18 9:38
 * @since 1.1.6
 */
public enum EncType {

    /**
     * MD5
     */
    MD5,
    /**
     * SHA-256
     */
    SHA256("SHA-256"),
    /**
     * DES
     */
    DES,
    /**
     * AES
     */
    AES,
    /**
     * RSAOrigin
     */
    RSA,

    /**
     * DSA
     */
    DSA,

    /**
     * ECC
     */
    ECC,


    /**
     * EC
     */
    EC,

    /**
     * BC
     */
    BC,


    ;


    EncType() {
        this.name = super.name();
    }

    EncType(String name) {
        this.name = name;
    }

    private final String name;

    public String getName() {
        return name;
    }
}
