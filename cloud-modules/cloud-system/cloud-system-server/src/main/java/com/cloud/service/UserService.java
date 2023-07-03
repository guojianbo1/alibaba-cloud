package com.cloud.service;

import com.cloud.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-21
 */
public interface UserService extends IService<User> {

    /**
     * 获取所有菜单
     * @param userId 用户id
     * @return 菜单列表
     */
    List<String> getAllMenus(String userId);

    /**
     * 扣减用户余额
     * @param userId
     * @param balance
     */
    void reduceBalance(String userId, BigDecimal balance);


}
