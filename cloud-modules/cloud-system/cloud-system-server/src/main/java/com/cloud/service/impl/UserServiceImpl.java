package com.cloud.service.impl;

import com.cloud.entity.User;
import com.cloud.mapper.UserMapper;
import com.cloud.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
