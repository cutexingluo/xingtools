package top.cutexingluo.tools.utils.encode.base;

/**
 * 模式
 * <p>加密算法模式，是用来描述加密算法（此处特指分组密码，不包括流密码，）在加密时对明文分组的模式，它代表了不同的分组方式</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 14:18
 * @see cn.hutool.crypto.Mode
 * @since 1.1.6
 */
public enum EncMode {

    /**
     * 无模式
     */
    NONE,
    /**
     * 密码分组连接模式（Cipher Block Chaining）
     */
    CBC,
    /**
     * 密文反馈模式（Cipher Feedback）
     */
    CFB,
    /**
     * 计数器模式（A simplification of OFB）
     */
    CTR,
    /**
     * Cipher Text Stealing
     */
    CTS,
    /**
     * 电子密码本模式（Electronic CodeBook）
     */
    ECB,
    /**
     * 输出反馈模式（Output Feedback）
     */
    OFB,
    /**
     * Propagating Cipher Block
     */
    PCBC
}
