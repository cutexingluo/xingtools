package top.cutexingluo.tools.utils.encode.encrypt.symmetric.des;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.utils.encode.encrypt.core.EncMeta;

/**
 * DES加密 hutool 实现
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 14:56
 * @since 1.1.6
 */
public class DESHuHandler {

    /**
     * DES加密
     *
     * @param data 待加密的数据
     * @param key  密钥，长度必须为8位
     * @param meta 元数据，可以设置模式
     * @return 加密后的数据，使用Base64编码
     */
    public String encode(String data, String key, @Nullable EncMeta meta) {
        if (meta == null) meta = new EncMeta();
        DES des = new DES(Mode.valueOf(meta.getMode()), Padding.PKCS5Padding, key.getBytes(), meta.getIv());
        return des.encryptBase64(data);
    }

    /**
     * DES解密
     *
     * @param encryptedData 加密后的数据，使用Base64编码
     * @param key           密钥，长度必须为8位
     * @param meta          元数据，可以设置模式
     * @return 解密后的数据
     */
    public String decode(String encryptedData, String key, @Nullable EncMeta meta) {
        if (meta == null) meta = new EncMeta();
        DES des = new DES(Mode.valueOf(meta.getMode()), Padding.PKCS5Padding, key.getBytes(), meta.getIv());
        return des.decryptStr(encryptedData);
    }

}
