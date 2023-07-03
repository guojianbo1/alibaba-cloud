package com.cloud.feign;

import com.cloud.domain.request.TccReduceBalanceDTO;
import com.cloud.remote.ApiUserService;
import com.cloud.result.Result;
import com.cloud.service.TccReduceBalanceUserServiceV2;
import com.cloud.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "用户管理")
public class ApiUserServiceImpl implements ApiUserService {
    @Autowired
    private UserService userService;
    @Autowired
    private TccReduceBalanceUserServiceV2 tccReduceBalanceUserServiceV2;

    @Override
    @ApiOperation("获取用户所有菜单")
    @GetMapping(value = "/getAllMenu")
    public Result<List<String>> getAllMenu(@ApiParam(value = "用户id") String userId) {
        System.out.println("userService:"+userService);
        System.out.println("userService.hashCode:"+userService.hashCode());
        return Result.success(userService.getAllMenus(userId));
    }

    @Override
    @ApiOperation("扣减用户余额")
    @PostMapping(value = "/reduceBalance")
    public Result<Void> reduceBalance(@ApiParam(value = "用户id")String userId,@ApiParam(value = "扣减金额") BigDecimal balance) {
        userService.reduceBalance(userId,balance);
        return Result.success();
    }

    @Override
    @ApiOperation("Tcc扣减用户余额")
    @PostMapping(value = "/tccReduceBalance")
    @GlobalTransactional(name = "TCC扣减用户金额事务")
    public Result<Void> tccReduceBalance(TccReduceBalanceDTO dto) {
        tccReduceBalanceUserServiceV2.prepare(dto);
        return Result.success();
    }
}
