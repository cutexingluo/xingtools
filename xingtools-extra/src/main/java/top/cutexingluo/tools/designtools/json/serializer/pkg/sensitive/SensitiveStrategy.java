package top.cutexingluo.tools.designtools.json.serializer.pkg.sensitive;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.designtools.json.serializer.StrJsonStrategy;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2024/1/1 14:29
 * @since 1.0.4
 */
@AllArgsConstructor
public enum SensitiveStrategy {


    /**
     * 用户名
     */
    USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),
    /**
     * 身份证
     */
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1****$2")),
    /**
     * 手机号
     */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),
    /**
     * 地址
     */
    ADDRESS(s -> s.replaceAll("(\\S{3})\\S{2}(\\S*)\\S{2}", "$1****$2****"));

    private final StrJsonStrategy desensitize;

    public StrJsonStrategy desensitize() {
        return desensitize;
    }

    @Contract(pure = true)
    public static SensitiveStrategy getStrategy(@NotNull String strategy) {
        switch (strategy.toUpperCase()) {
            case "ID_CARD":
                return ID_CARD;
            case "PHONE":
                return PHONE;
            case "ADDRESS":
                return ADDRESS;
            case "USERNAME":
            default:
                return USERNAME;
        }
    }

}
