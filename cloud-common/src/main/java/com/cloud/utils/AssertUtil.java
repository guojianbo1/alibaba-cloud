package com.cloud.utils;

import com.cloud.constant.Constants;
import com.cloud.exception.BusinessException;
import org.springframework.util.Assert;

/**
 * 断言工具类
 *
 * @author guojianbo
 * @date 2023/6/19 11:20
 */
public class AssertUtil extends Assert {

    /**
     * 抛出异常(默认错误1000)
     * @param message
     */
    public static void businessInvalid(String message) {
        throw new BusinessException(Constants.ERROR, message);
    }

    /**
     * 表达式为真即抛出异常(默认错误1000)
     *
     * @param expression
     * @param message
     */
    public static void businessInvalid(boolean expression, String message) {
        if (expression) {
            throw new BusinessException(Constants.ERROR, message);
        }
    }

    /**
     * 表达式为真即抛出异常
     *
     * @param expression
     * @param message
     */
    public static void businessInvalid(boolean expression, int code, String message) {
        if (expression) {
            throw new BusinessException(code, message);
        }
    }
}
