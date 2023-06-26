package com.cloud.service;

import com.cloud.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

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
    List<String> getAllMenus(String userId) throws InterruptedException;
}
