package com.cloud.mapper;

import com.cloud.entity.UserBalanceFlow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 余额变更流水表 Mapper 接口
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-27
 */
public interface UserBalanceFlowMapper extends BaseMapper<UserBalanceFlow> {

    /**
     * 根据orderId查询,加锁
     * @param orderId 订单id
     * @return 流水记录
     */
    UserBalanceFlow findByOrderIdForUpdate(Long orderId);
}
