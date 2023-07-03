package com.cloud.controller.client;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.annotation.AdminDrawUser;
import com.cloud.domain.response.UserDetailDTO;
import com.cloud.entity.User;
import com.cloud.result.Result;
import com.cloud.service.UserService;
import com.cloud.utils.AdminUserUtils;
import com.cloud.utils.AssertUtil;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * C端用户Controller
 *
 * @author guojianbo
 * @date 2023/6/19 10:57
 */
@Log4j2
@RestController
@CrossOrigin
@AdminDrawUser
@RequestMapping("/client/user")
@Api(tags = "用户管理")
public class ClientUserController {
    @Autowired
    private UserService userService;

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/detail")
    public Result<UserDetailDTO> getData() {
        System.out.println("userService1:"+userService);
        System.out.println("userService1.hashCode:"+userService.hashCode());
        User user = userService.getOne(new QueryWrapper<User>().eq(User.USER_ID, AdminUserUtils.getUserId()));
        AssertUtil.businessInvalid(user==null,"用户不存在");
        return Result.success(new UserDetailDTO(AdminUserUtils.getUserId(),user.getUserName(),user.getAge(), Lists.newArrayList("用户管理","订单管理")));
    }

}
