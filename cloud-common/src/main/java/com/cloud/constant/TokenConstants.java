package com.cloud.constant;

/**
 * 令牌常量
 *
 * @author guojianbo
 * @date 2023/6/16 10:17
 */
public class TokenConstants {
    /**
     * 缓存有效期，默认720（分钟）
     */
    public final static long EXPIRATION = 720;
    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;
    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";
    /**
     * token标识
     */
    public static final String TOKEN = "token";
}
