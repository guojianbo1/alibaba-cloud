package com.cloud.result;

import com.cloud.constant.Constants;
import lombok.Data;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author guojianbo
 * @date 2023/6/16 10:19
 */
@Data
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 成功 */
    public static final int SUCCESS = Constants.SUCCESS;
    /** 失败 */
    public static final int FAIL = Constants.ERROR;
    private int code;
    private String msg;
    private T data;
    public static <T> Result<T> success() {
        return restResult(null, SUCCESS, Constants.SUCCESS_MSG);
    }
    public static <T> Result<T> success(T data) {
        return restResult(data, SUCCESS, Constants.SUCCESS_MSG);
    }
    public static <T> Result<T> success(T data, String msg) {
        return restResult(data, SUCCESS, msg);
    }
    public static <T> Result<T> error() {
        return restResult(null, FAIL, Constants.ERROR_MSG);
    }
    public static <T> Result<T> error(String msg) {
        return restResult(null, FAIL, msg);
    }
    public static <T> Result<T> error(T data) {
        return restResult(data, FAIL, Constants.ERROR_MSG);
    }
    public static <T> Result<T> error(T data, String msg) {
        return restResult(data, FAIL, msg);
    }
    public static <T> Result<T> error(int code, String msg) {
        return restResult(null, code, msg);
    }
    private static <T> Result<T> restResult(T data, int code, String msg) {
        Result<T> apiResult = new Result<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
