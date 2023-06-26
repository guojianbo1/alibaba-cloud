package com.cloud.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.cloud.annotation.AdminDrawUser;
import com.cloud.domain.response.UserDetailDTO;
import com.cloud.result.Result;
import com.cloud.service.UserService;
import com.cloud.utils.AdminUserUtils;
import com.google.common.collect.Lists;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ThreadUtils;
import org.apache.tomcat.util.digester.Rules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

/**
 * Api用户Controller
 *
 * @author guojianbo
 * @date 2023/6/19 10:57
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class ApiUserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/getAllMenu")
    public Result<List<String>> getAllMenu(@RequestParam("userId") String userId) throws InterruptedException {
        System.out.println("userService:"+userService);
        System.out.println("userService.hashCode:"+userService.hashCode());
        return Result.success(userService.getAllMenus(userId));
    }

    @PostMapping(value = "/reduceBalance")
    @GlobalTransactional(name = "扣减用户金额事务")
    public Result<Void> reduceBalance(@RequestParam("userId") String userId,
                                              @RequestParam("balance") BigDecimal balance) {
        userService.reduceBalance(userId,balance);
        return Result.success();
    }
}
