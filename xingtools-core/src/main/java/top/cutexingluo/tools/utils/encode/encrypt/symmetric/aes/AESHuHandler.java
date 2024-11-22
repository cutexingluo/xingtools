package top.cutexingluo.tools.utils.encode.encrypt.symmetric.aes;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.utils.encode.encrypt.core.EncMeta;

/**
 * AES加密 hutool 实现
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 14:13
 * @since 1.1.6
 */
public class AESHuHandler {


    /**
     * AES加密
     *
     * @param data 待加密的数据
     * @param meta 元数据，可以设置模式 (密钥，长度必须为8位)
     * @return 加密后的数据，使用Base64编码
     */
    public String encode(String data, byte[] key, @Nullable EncMeta meta) {
        if (meta == null) meta = new EncMeta();
        AES aes = new AES(Mode.valueOf(meta.getMode()), Padding.PKCS5Padding, key, meta.getIv());
        return aes.encryptBase64(data);
    }

    /**
     * AES解密
     *
     * @param encryptedData 加密后的数据，使用Base64编码
     * @param meta          元数据，可以设置模式 (密钥，长度必须为8位)
     * @return 解密后的数据
     */
    public String decode(String encryptedData, byte[] key, @Nullable EncMeta meta) {
        if (meta == null) meta = new EncMeta();
        AES aes = new AES(Mode.valueOf(meta.getMode()), Padding.PKCS5Padding, key, meta.getIv());
        return aes.decryptStr(encryptedData);
    }
}
