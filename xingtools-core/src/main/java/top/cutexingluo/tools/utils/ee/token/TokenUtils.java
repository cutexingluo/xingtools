package top.cutexingluo.tools.utils.ee.token;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import top.cutexingluo.tools.common.Constants;
import top.cutexingluo.tools.exception.ServiceException;

import java.util.Date;


/**
 * JWT Token 原生工具类
 * <p>适用于Springboot </p>
 *
 * @author XingTian
 * @version v1.0.0
 * @date 2023/11/14
 * @since 2022-11-14 v1.0.1
 */
public class TokenUtils {

    /**
     * 设置token过期时间
     * <p>该属性公有静态可修改</p>
     */
    //设置token过期时间,必须要公有了
    public static int offset = 5;                                        //************************

    public TokenUtils setOffset(int offset) {
        TokenUtils.offset = offset;
        return this;
    }


    //生成 token ，编码
    public static String getToken(String userId, String sign) { //用户id字符串，密钥，可以为密码
        // 将 user id 保存到 token 里面,作为载荷
        return JWT.create().withAudience(userId)
                .withExpiresAt(DateUtil.offsetHour(new Date(), offset)) //五小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
    }

    //解码, 拦截器里面使用，解码原userId
    public static String getUserId(String token) throws ServiceException {//从前端获取的token，返回解码后的id,然后利用id获取数据库的DAO
        // 执行认证
        if (StrUtil.isBlank(token)) {
            throw new ServiceException(Constants.CODE_401.getCode(), "无token请重新登录");
        }
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new ServiceException(Constants.CODE_401.getCode(), "token验证失败，请重新登录");
        }
        return userId;
    }

    // 验证token，解码后使用
    public static <T> boolean checkToken(String token, T user, String sign) throws ServiceException {//从前端获取的token，数据库用户,密钥，可以为密码,因为不想用反射，所以这样写
        if (user == null) {//需要提前验证user是否为空
            throw new ServiceException(Constants.CODE_401.getCode(), "用户不存在，请重新登录");
        }
        // 用户密码加签 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(sign)).build();
        try {
            jwtVerifier.verify(token); //比较
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401.getCode(), "token验证失败，请重新登录");
        }
        return true;
    }
}
