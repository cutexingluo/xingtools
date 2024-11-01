package top.cutexingluo.tools.security.oauthserver.constants;

import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.Serializable;

/**
 * Security 常量
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/11/1 18:11
 */
public interface SecurityConstant extends Serializable {

    /**
     * <pre>
     * `AUTHORIZATION_CODE` 是一种授权码认证机制。在该机制中，客户端向服务端申请访问某个资源时，需要先在服务端进行身份验证并获取授权码。
     *
     * 授权码认证机制的具体流程如下：
     *
     * 1. 客户端向服务端发送一个认证请求，请求中包含客户端的身份认证信息和所需访问的资源信息。
     *
     * 2. 服务端接收到请求后，需要验证客户端的身份信息是否正确。如果验证成功，服务端将向客户端返回一个授权码。
     *
     * 3. 客户端获取到授权码后，将其发送给服务端请求访问资源。此时，客户端还需要在请求中包含之前获取到的身份认证信息。
     *
     * 4. 服务端根据客户端提供的授权码和身份认证信息进行身份验证和授权，如果验证和授权成功，则服务端允许客户端访问所需资源。
     *
     * 授权码机制的主要优势在于，能够避免客户端在请求资源时携带敏感信息，增加了请求的安全性。同时，在授权码机制中，服务端可以对授权码进行有效期限制，从而减少了 token 泄露引起的风险。
     *
     * 在 OAuth2 认证协议中，授权码认证机制是最常用和最安全的一种认证机制，被广泛应用于各种 Web 应用程序和 API 认证中。
     * </pre>
     */
    String AUTHORIZATION_CODE = AuthorizationGrantType.AUTHORIZATION_CODE.getValue();

    /**
     * <pre>
     * `IMPLICIT` 是一种隐式授权认证机制。在该机制中，客户端通过浏览器直接向授权服务器发送请求，授权服务器直接将访问令牌（Access Token）返回给客户端。
     *
     * 隐式授权认证机制的具体流程如下：
     *
     * 1. 客户端向授权服务器发送请求，请求中包含客户端的身份认证信息和所需访问的资源信息。
     *
     * 2. 授权服务器收到请求后，需要验证客户端的身份信息。如果验证成功，授权服务器直接将访问令牌以及其他信息返回给客户端。
     *
     * 3. 客户端在收到授权服务器返回的访问令牌后，直接在浏览器中使用该令牌请求资源。
     *
     * 隐式授权认证机制的主要优势在于，能够避免客户端在请求资源时携带敏感信息，增加了请求的安全性。同时，在隐式授权认证机制中，访问令牌的有效期通常较短，并且无法刷新，这也可以减少因为访问令牌泄露引起的风险。
     *
     * 在 OAuth2 认证协议中，隐式授权认证机制被广泛应用于基于浏览器和 JavaScript 进行的 Web 应用和 API 认证中。
     * </pre>
     */
    @Deprecated
    String IMPLICIT = AuthorizationGrantType.IMPLICIT.getValue();

    /**
     * <pre>
     *     `REFRESH_TOKEN` 是一种刷新令牌认证机制。在该机制中，当 Access Token 过期时，客户端可以使用 Refresh Token 来获取新的 Access Token，从而不需要重新进行身份验证。
     *
     * 刷新令牌认证机制的具体流程如下：
     *
     * 1. 当 Access Token 过期时，客户端使用存储的 Refresh Token 向授权服务器请求新的 Access Token。
     *
     * 2. 授权服务器接收到请求后，验证 Refresh Token 是否有效。如果有效，则授权服务器颁发一个新的 Access Token 并返回给客户端。
     *
     * 3. 客户端使用新的 Access Token 进行资源访问。
     *
     * 使用 Refresh Token 可以避免客户端频繁向授权服务器请求 Access Token，从而减少网络传输和服务器压力。同时，Refresh Token 通常拥有较长的有效期，因此可以在一定程度上减少用户频繁登录的操作。
     *
     * 需要注意的是，使用 Refresh Token 机制也引入了新的安全风险。由于 Refresh Token 可以用于获取新的 Access Token，因此需要更加谨慎地使用和存储 Refresh Token，防止被第三方利用。建议在存储 Refresh Token 时，采用加密和随机化等方式进行安全加固。
     * </pre>
     */
    String REFRESH_TOKEN = AuthorizationGrantType.REFRESH_TOKEN.getValue();

    /**
     * <pre>
     * `CLIENT_CREDENTIALS` 是一种客户端认证方式，通常用于客户端向服务器获取受保护资源的访问权限。在该认证方式中，客户端通过提供自己的身份信息向授权服务器请求访问令牌（Access Token）
     *
     * 客户端认证的具体流程如下：
     *
     * 1. 客户端向授权服务器发送一个包含客户端身份令牌的请求，以获取 Access Token。
     *
     * 2. 授权服务器收到请求后，通过验证客户端身份令牌，确定客户端身份的合法性；如果验证通过，授权服务器会生成 Access Token 并返回给客户端。
     *
     * 3. 客户端在后续的 HTTP 请求中需要携带该 Access Token 进行资源访问，以验证客户端身份的合法性。
     *
     * 需要注意的是，使用客户端身份验证方式需要客户端和授权服务器之间建立信任关系，授权服务器需要验证客户端身份令牌的真实性和有效性，并判断是否授权给该客户端访问特定的受保护资源。
     *
     * 在 OAuth2 认证协议中，客户端凭证认证机制被广泛应用于通过 API 进行授权的场景，例如：某些云服务需要通过 API 访问，就需要使用 CLIENT_CREDENTIALS 认证方式。
     * </pre>
     */
    String CLIENT_CREDENTIALS = AuthorizationGrantType.CLIENT_CREDENTIALS.getValue();

    /**
     * <pre>
     * `PASSWORD` 是一种用户密码认证方式，通常用于将用户的帐户和密码直接提交给认证服务器，以获取访问令牌（Access Token）
     *
     * 密码认证方式的具体流程如下：
     *
     * 1. 用户向客户端提供其帐户名和密码。
     *
     * 2. 客户端通过 HTTPS 将用户的用户名和密码等信息发送到授权服务器进行认证。
     *
     * 3. 授权服务器接收到认证请求后，会对用户身份进行验证。如果身份验证成功，则授权服务器会颁发 Access Token。如果认证失败，则拒绝向客户端颁发 Access Token。
     *
     * 4. 客户端在 HTTP 请求中带上该 Access Token，服务端收到请求后会对 Access Token 进行验证。如果验证通过，则允许客户端访问受保护的资源。
     *
     * 需要注意的是，使用 PASSWORD 认证方式需要将用户名和密码直接提交给授权服务器进行认证，因此需要对传输数据进行加密和安全保护，避免密码泄露和身份伪造等安全问题。
     *
     * 在 OAuth2 认证协议中，密码认证方式通常用于用户需要访问自己的受保护资源的场景，例如：移动设备上的应用程序需要访问音乐、社交网络等云服务资源。
     * </pre>
     */
    String PASSWORD = AuthorizationGrantType.PASSWORD.getValue();

    /**
     * <pre>
     * `JWT_BEARER`（JSON Web Token Bearer）是一种基于 JWT 的认证机制。在 JWT_BEARER 认证机制中，客户端将包含 JWT 的 Bearer Token 发送给服务端进行认证。
     *
     * JWT_BEARER 认证机制的具体流程如下：
     *
     * 1. 客户端请求服务端时，在请求头中设置 `Authorization` 字段，并将 Bearer Token 放在该字段的值中。
     *
     * 2. 服务端接收到请求后，根据请求头中的 Bearer Token 进行认证。认证包括解析 JWT Token 和验证 Token 的签名是否正确等操作。如果认证成功，则服务端将允许客户端继续访问资源。
     *
     * 在使用 JWT_BEARER 认证机制时，要确保服务端和客户端使用相同的密钥对 JWT Token 进行加解密操作，以确保 Token 的一致性和安全性。同时还要注意 Token 的过期时间等安全问题，避免 Token 被劫持和滥用。
     * </pre>
     */
    String JWT_BEARER = AuthorizationGrantType.JWT_BEARER.getValue();
}
