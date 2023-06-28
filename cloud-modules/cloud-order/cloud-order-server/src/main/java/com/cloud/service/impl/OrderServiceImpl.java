package com.cloud.service.impl;

import com.cloud.domain.request.CreateOrderReqDTO;
import com.cloud.domain.request.TccReduceBalanceDTO;
import com.cloud.entity.Order;
import com.cloud.mapper.OrderMapper;
import com.cloud.remote.ApiIntegralRecordService;
import com.cloud.remote.ApiUserService;
import com.cloud.remote.ApiUserTccReduceBalanceService;
import com.cloud.result.Result;
import com.cloud.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.utils.AssertUtil;
import io.seata.rm.tcc.TCCResourceManager;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-26
 */
@Service
@RefreshScope
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private ApiUserService apiUserService;
    @Autowired
    private ApiIntegralRecordService apiIntegralRecordService;
    @Autowired
    private ApiUserTccReduceBalanceService apiUserTccReduceBalanceService;
    @Value("${at.error:false}")
    private boolean atError;

    @Override
    @GlobalTransactional(name = "订单创建事务")
    @Transactional(rollbackFor = Exception.class)
    public void create(String userId, String userName, CreateOrderReqDTO reqDTO) {
        //创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderAmount(reqDTO.getOrderAmount());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        this.save(order);
        //使用Tcc扣减金额
        TccReduceBalanceDTO tccReduceBalanceDTO = new TccReduceBalanceDTO();
        tccReduceBalanceDTO.setUserId(userId);
        tccReduceBalanceDTO.setAmount(reqDTO.getOrderAmount());
        tccReduceBalanceDTO.setOrderId(order.getOrderId());
//        boolean prepare = apiUserTccReduceBalanceService.prepare(tccReduceBalanceDTO);
        Result<Void> result = apiUserService.tccReduceBalance(tccReduceBalanceDTO);
        AssertUtil.businessInvalid(!result.isSuccess(),result.getMsg());
        if (atError){
            AssertUtil.businessInvalid("TCC扣减金额后异常");
        }
//        //保存积分
//        Result<Void> save = apiIntegralRecordService.save(userId, order.getOrderId(), reqDTO.getOrderAmount());
//        AssertUtil.businessInvalid(!save.isSuccess(),save.getMsg());
//        //otherTodo
//        System.out.println("otherTodo");
    }

    @Override
    @GlobalTransactional(name = "订单创建事务2")
    @Transactional(rollbackFor = Exception.class)
    public void create2(String userId, String userName, CreateOrderReqDTO reqDTO) {
        //扣减金额(需要获取用户的金额锁，再执行操作)
        Result<Void> result = apiUserService.reduceBalance(userId, reqDTO.getOrderAmount());
        AssertUtil.businessInvalid(!result.isSuccess(),result.getMsg());
        //创建订单
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderAmount(reqDTO.getOrderAmount());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        this.save(order);

    }
}
