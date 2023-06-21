package com.cloud.domain.request;

import lombok.Data;

/**
 * 登录req
 *
 * @author guojianbo
 * @date 2023/6/19 15:19
 */
@Data
public class LoginReq {
    private String userId;
    private String pwd;
}
