package com.cloud.service.impl;

import com.cloud.entity.User;
import com.cloud.mapper.UserMapper;
import com.cloud.result.Result;
import com.cloud.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-21
 */
@Service
@Log4j2
@RefreshScope //使用该注解来实现nacos配置动态更新，但注意该注解会重新创建bean
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Value("${sleep.time:0}")
    private Integer sleepTime;
    private int count;

    @Override
    public List<String> getAllMenus(String userId) throws InterruptedException {
        count++;
        System.out.println(count);
        System.out.println("this:"+this);
        System.out.println("this.hashCode:"+this.hashCode());
        log.info("sleepTime:"+sleepTime);
        if (sleepTime>0){
            Thread.sleep(sleepTime);
        }
        return Lists.newArrayList("用户管理","订单管理");
    }

    @Override
    @GlobalTransactional
    public boolean save(User entity) {
        return super.save(entity);
    }
}
