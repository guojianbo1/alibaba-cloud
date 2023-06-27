package com.cloud.service;

import com.cloud.entity.UserBalanceFlow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 余额变更流水表 服务类
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-27
 */
public interface UserBalanceFlowService extends IService<UserBalanceFlow> {
    /**
     * 根据orderId查询
     * @param orderId 订单id
     * @return 流水记录
     */
    UserBalanceFlow findByOrderId(Long orderId);

    /**
     * 根据orderId查询，加锁
     * @param orderId 订单id
     * @return 流水记录
     */
    UserBalanceFlow findByOrderIdForUpdate(Long orderId);
}
