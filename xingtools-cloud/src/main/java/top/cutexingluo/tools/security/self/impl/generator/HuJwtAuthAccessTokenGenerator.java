package top.cutexingluo.tools.security.self.impl.generator;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSigner;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.security.self.base.AuthAccessToken;
import top.cutexingluo.tools.security.self.base.AuthTokenGenerator;

/**
 * hutool jwt 生成器
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/26 10:50
 */
@Data
public class HuJwtAuthAccessTokenGenerator implements AuthTokenGenerator {

    protected JWTSigner signer;

    protected JWT jwt;


    public HuJwtAuthAccessTokenGenerator(@NotNull JWTSigner signer) {
        this.signer = signer;
    }

    @Override
    public AuthTokenGenerator initSelf() {
        jwt = JWT.create().setSigner(signer);
        return this;
    }

    @Override
    public String generate(@NotNull AuthAccessToken authAccessToken) {
        if (jwt == null) initSelf();
        jwt.addPayloads(authAccessToken.getAdditionalInformation());
        return jwt.sign();
    }
}
