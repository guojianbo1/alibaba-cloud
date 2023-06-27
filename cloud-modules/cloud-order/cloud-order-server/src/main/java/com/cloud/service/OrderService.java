package com.cloud.service;

import com.cloud.domain.request.CreateOrderReqDTO;
import com.cloud.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-26
 */
public interface OrderService extends IService<Order> {

    /**
     * 创建订单
     * @param userId 用户id
     * @param userName 用户名
     * @param reqDTO 下单参数
     */
    void create(String userId, String userName, CreateOrderReqDTO reqDTO);

    /**
     * 创建订单
     * @param userId 用户id
     * @param userName 用户名
     * @param reqDTO 下单参数
     */
    void create2(String userId, String userName, CreateOrderReqDTO reqDTO);
}
