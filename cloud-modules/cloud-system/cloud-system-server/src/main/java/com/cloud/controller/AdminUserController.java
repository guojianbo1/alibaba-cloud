package com.cloud.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.annotation.AdminDrawUser;
import com.cloud.domain.response.UserDetailDTO;
import com.cloud.entity.User;
import com.cloud.result.Result;
import com.cloud.service.UserService;
import com.cloud.utils.AdminUserUtils;
import com.cloud.utils.AssertUtil;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * B端用户Controller
 *
 * @author guojianbo
 * @date 2023/6/19 10:57
 */
@Log4j2
@RestController
@CrossOrigin
@AdminDrawUser
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/detail")
    public Result<UserDetailDTO> getData() {
        User user = userService.getOne(new QueryWrapper<User>().eq(User.USER_ID, AdminUserUtils.getUserId()));
        AssertUtil.businessInvalid(user==null,"用户不存在");
        return Result.success(new UserDetailDTO(AdminUserUtils.getUserId(),AdminUserUtils.getUserName(),user.getAge(), Lists.newArrayList("用户管理","订单管理")));
    }

}
