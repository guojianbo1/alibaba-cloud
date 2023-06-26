package com.cloud.remote;

import com.cloud.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户接口
 *
 * @author guojianbo
 * @date 2023/6/19 13:53
 */
@FeignClient(name = "cloud-system", path = "/system/api/user")
public interface ApiUserService {

    /**
     * 获取用户拥有权限的所有菜单
     * @param userId 用户id
     * @return 结果
     */
    @RequestMapping(value = "/getAllMenu", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<List<String>> getAllMenu(@RequestParam("userId") String userId );

    /**
     * 扣减用户余额
     * @param userId 用户id
     * @param balance 扣减金额
     * @return 结果
     */
    @RequestMapping(value = "/reduceBalance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> reduceBalance(@RequestParam("userId") String userId, @RequestParam("balance") BigDecimal balance);
}
