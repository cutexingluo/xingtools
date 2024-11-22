package top.cutexingluo.tools.utils.encode.encrypt.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.cutexingluo.tools.utils.encode.base.EncMode;

/**
 * 加密元数据，默认为ECB模式，不设置iv
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 14:25
 * @since 1.1.6
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncMeta {


    /**
     * 模式
     */
    private String mode = EncMode.ECB.name();

    /**
     * 向量, 偏移向量, 加盐
     */
    private byte[] iv;


    /**
     * 获取Transformation
     */
    public String getTransformation(String algorithm, String padding) {
        return algorithm + "/" + mode + "/" + padding;
    }

    /**
     * 获取默认的Transformation
     */
    public String getTransformation(String algorithm) {
        return getTransformation(algorithm, "PKCS5Padding");
    }
}
