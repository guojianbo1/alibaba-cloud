package com.cloud.fegin;

import com.cloud.annotation.ExcludeFromGlobalException;
import com.cloud.domain.request.TccReduceBalanceDTO;
import com.cloud.remote.ApiUserService;
import com.cloud.result.Result;
import com.cloud.service.TccReduceBalanceUserServiceV2;
import com.cloud.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * userApi接口实现
 *
 * @author guojianbo
 * @date 2023/7/3 8:34
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class ApiUserServiceImpl implements ApiUserService {
    @Autowired
    private UserService userService;
    @Autowired
    private TccReduceBalanceUserServiceV2 tccReduceBalanceUserServiceV2;

    @Override
    @GetMapping(value = "/getAllMenu")
    public Result<List<String>> getAllMenu(String userId) throws InterruptedException {
        System.out.println("userService:"+userService);
        System.out.println("userService.hashCode:"+userService.hashCode());
        int i = 1/0;
        return Result.success(userService.getAllMenus(userId));
    }

    @Override
    @PostMapping(value = "/reduceBalance")
    public Result<Void> reduceBalance(String userId, BigDecimal balance) {
        userService.reduceBalance(userId,balance);
        return Result.success();
    }

    @Override
    @PostMapping(value = "/tccReduceBalance")
    @GlobalTransactional(name = "TCC扣减用户金额事务")
    public Result<Void> tccReduceBalance(TccReduceBalanceDTO dto) {
        tccReduceBalanceUserServiceV2.prepare(dto);
        return Result.success();
    }
}
