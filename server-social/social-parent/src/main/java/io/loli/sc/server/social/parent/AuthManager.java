package io.loli.sc.server.social.parent;

/**
 * 所有认证类都必须继承这个抽象类<br>
 * 
 * @author choco
 *
 */
public abstract class AuthManager {
    protected AuthInfo info;

    /**
     * 获取认证用的URL地址，用于用户点击授权
     * 
     * @return 返回本应用的授权页面URL
     */
    public abstract String getAuthUrl();

    /**
     * 用户授权后会返回一个code，通过此code能获取到该用户的accessToken
     * 
     * @param code 用户授权后的code
     * @return 返回通过此code获取到的accessToken
     */
    public abstract String getAccessToken(String code);

    /**
     * 用旧的accessToken获取新的accessToken
     * 
     * @param token 旧的accessToken
     * @return 返回新的accessToken
     */
    public abstract String refresh(String token);

    public AuthInfo getAuthInfo() {
        return info;
    }
}
