package top.cutexingluo.tools.utils.ee.token;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTException;
import cn.hutool.jwt.JWTUtil;

/**
 * XTToken 原生工具类<br>
 * 这个不推荐使用, 仅供参考
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/6/18 15:14
 */
public class XTTokenUtil {

    /**
     * 验证令牌格式 由 jwt 法校验 无异常抛出版本
     * <p> 里面包含非空验证, 格式验证, sign验证</p>
     *
     * @param token   令牌
     * @param signKey signKey
     * @return boolean 是否校验成功 (sign 验证)
     */
    public static boolean verifyTokenByJwtNoException(String token, byte[] signKey) {
        boolean result = false;
        try {
            // 验证 jwt token
            if (StrUtil.isBlank(token)) {
                return false;
            }
            // 验证jwt是否正确 判断 token 格式是否正确
            result = JWTUtil.verify(token, signKey);
        } catch (JWTException e) {
            return false;
        } catch (JSONException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
        return result;
    }


    /**
     * 得到所有有效载荷
     * <br>
     * 解析令牌 为 json
     * <br>
     * 该方法不推荐
     *
     * @param token 令牌
     * @return {@link JSONObject}
     */
    public static JSONObject getAllPayloads(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        return jwt.getPayloads();
    }
}
