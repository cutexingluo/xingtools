package top.cutexingluo.tools.security.self.base;


import org.jetbrains.annotations.Nullable;

import java.time.Instant;

/**
 * Token
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/7/25 14:34
 * @see org.springframework.security.oauth2.core.OAuth2Token
 * @since 1.1.2
 */
public interface AuthToken {

    /**
     * Returns the token value.
     *
     * @return the token value
     */
    String getTokenValue();

    /**
     * Returns the time at which the token was issued.
     *
     * @return the time the token was issued or {@code null}
     */
    @Nullable
    default Instant getIssuedAt() {
        return null;
    }

    /**
     * Returns the expiration time on or after which the token MUST NOT be accepted.
     *
     * @return the token expiration time or {@code null}
     */
    @Nullable
    default Instant getExpiresAt() {
        return null;
    }
}
