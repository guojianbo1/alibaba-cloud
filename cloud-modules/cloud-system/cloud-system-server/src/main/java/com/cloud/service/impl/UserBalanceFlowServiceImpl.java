package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.entity.User;
import com.cloud.entity.UserBalanceFlow;
import com.cloud.mapper.UserBalanceFlowMapper;
import com.cloud.service.UserBalanceFlowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.utils.AdminUserUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 余额变更流水表 服务实现类
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-27
 */
@Service
public class UserBalanceFlowServiceImpl extends ServiceImpl<UserBalanceFlowMapper, UserBalanceFlow> implements UserBalanceFlowService {

    @Override
    public UserBalanceFlow findByOrderId(Long orderId) {
        return this.getOne(new QueryWrapper<UserBalanceFlow>().eq(UserBalanceFlow.ORDER_ID, orderId));
    }

    @Override
    public UserBalanceFlow findByOrderIdForUpdate(Long orderId) {
        return this.baseMapper.findByOrderIdForUpdate(orderId);
    }
}
