package com.cloud.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.domain.request.TccReduceBalanceDTO;
import com.cloud.entity.User;
import com.cloud.remote.ApiUserTccReduceBalanceService;
import com.cloud.result.Result;
import com.cloud.service.impl.UserServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * test
 *
 * @author guojianbo
 * @date 2023/6/21 17:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ApiUserTccReduceBalanceService apiUserTccReduceBalanceService;

    @Test
    public void save(){
        User user = new User();
        user.setUserId("456");
        user.setUserName("王五");
        user.setAge(24);
//        user.setCreateTime(LocalDateTime.now());
        boolean save = userService.save(user);
        System.out.println(JSONObject.toJSONString(user));
        System.out.println(user.getId());
        System.out.println(save);
    }

    @Test
    public void get() throws InterruptedException {
        TccReduceBalanceDTO tccReduceBalanceDTO = new TccReduceBalanceDTO();
        tccReduceBalanceDTO.setUserId("123");
        tccReduceBalanceDTO.setAmount(new BigDecimal(10));
        tccReduceBalanceDTO.setOrderId(1099L);
        Result<Void> prepare = apiUserTccReduceBalanceService.prepare(tccReduceBalanceDTO);
        System.out.println(JSONObject.toJSONString(prepare));
        Thread.sleep(10000L);
        System.out.println("end");
    }

    @Test
    public void page(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq(User.USER_ID,"123")
                .eq(User.AGE,20);
        Page<User> page = userService.page(new Page<>(1, 10),queryWrapper);
        System.out.println(page.getTotal());
        System.out.println(JSONObject.toJSONString(page));
        System.out.println(JSONObject.toJSONString(page.getRecords()));
    }
}
