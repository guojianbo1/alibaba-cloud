package com.cloud.utils;

import com.cloud.constant.JwtConstants;
import org.slf4j.MDC;

/**
 * B端获取用户工具类
 *
 * @author guojianbo
 * @date 2023/6/19 11:20
 */
public class AdminUserUtils {

    public static String getUserKey(){
        return MDC.get(JwtConstants.USER_KEY);
    }

    public static String getUserId(){
        return MDC.get(JwtConstants.DETAILS_USER_ID);
    }

    public static String getUserName(){
        return MDC.get(JwtConstants.DETAILS_USERNAME);
    }
}
