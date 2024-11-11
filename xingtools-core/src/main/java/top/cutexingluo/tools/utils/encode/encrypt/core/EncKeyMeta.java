package top.cutexingluo.tools.utils.encode.encrypt.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 加密元数据，默认为ECB模式，不设置iv
 *
 * <p>包含密钥</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/11 14:39
 * @since 1.1.6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EncKeyMeta extends EncMeta {

    /**
     * 密钥
     */
    private byte[] key;
}
