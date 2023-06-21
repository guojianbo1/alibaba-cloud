package com.cloud.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 登录resp
 *
 * @author guojianbo
 * @date 2023/6/19 15:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResp {
    private String userId;
    private String token;
    private List<String> menuList;
}
