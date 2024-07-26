package top.cutexingluo.tools.security.oauth.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 签名实体类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/16 12:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XTSign {

    protected String signKey = "sign_key";
}
