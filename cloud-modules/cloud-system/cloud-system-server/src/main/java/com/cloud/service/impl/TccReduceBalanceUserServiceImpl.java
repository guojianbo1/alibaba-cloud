package com.cloud.service.impl;

import com.cloud.entity.User;
import com.cloud.entity.UserBalanceFlow;
import com.cloud.service.TccReduceBalanceUserService;
import com.cloud.service.UserBalanceFlowService;
import com.cloud.utils.AssertUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


/**
 * tcc扣减金额
 *
 * @author guojianbo
 * @date 2023/6/26 17:50
 */
@Service
@Log4j2
@RefreshScope
public class TccReduceBalanceUserServiceImpl implements TccReduceBalanceUserService {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserBalanceFlowService userBalanceFlowService;
    @Value("${tcc.error.try:false}")
    private boolean tryError;
    @Value("${tcc.error.commit:false}")
    private boolean commitError;
    @Value("${tcc.error.rollback:false}")
    private boolean rollbackError;

    /**
     * todo:如何避免重复提交，幂等解决：
     * 可以由调用方传递交易流水号,在try阶段查询改流水号是否存在记录，不存在则插入流水表，并记录状态，
     * 然后commit、rollback查询该流水记录,对比状态，做相应操作，避免由于seata重试而出现问题
     *
     * @param userId 用户id
     * @param amount 金额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void prepare(String userId, BigDecimal amount, Long orderId) {
        log.info("tcc:try:开始");
        UserBalanceFlow userBalanceFlow = userBalanceFlowService.findByOrderIdForUpdate(orderId);
        if (userBalanceFlow != null) {
            log.warn("{}已经处理,忽略重复提交", orderId);
            return;
        }
        User user = userService.getUserByUserIdForUpdate(userId);
        AssertUtil.businessInvalid(user == null, "用户不存在");
        AssertUtil.businessInvalid(user.getBalance().compareTo(amount) < 0, "用户余额不足");
        User userNew = new User();
        userNew.setId(user.getId());
        userNew.setBalance(user.getBalance().subtract(amount));
        userNew.setFreezeBalance(user.getFreezeBalance().add(amount));
        userService.updateById(userNew);

        userBalanceFlow = new UserBalanceFlow();
        userBalanceFlow.setOrderId(orderId);
        userBalanceFlow.setUserId(userId);
        userBalanceFlow.setAmount(amount);
        userBalanceFlow.setType(UserBalanceFlow.TYPE_ENUM.SUB.getType());
        userBalanceFlow.setStatus(UserBalanceFlow.STATUS_ENUM.PRE.getStatus());
        userBalanceFlow.setPreBalance(user.getBalance());
        userBalanceFlow.setAfterBalance(userNew.getBalance());
        userBalanceFlow.setCreateTime(new Date());
        userBalanceFlow.setUpdateTime(new Date());
        userBalanceFlowService.save(userBalanceFlow);
        if (tryError){
            AssertUtil.businessInvalid("try:error");
        }
        log.info("tcc:try:完成");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commit(String userId, BigDecimal amount, Long orderId) {
        log.info("tcc:commit:开始");
        UserBalanceFlow userBalanceFlow = userBalanceFlowService.findByOrderIdForUpdate(orderId);
        AssertUtil.businessInvalid(userBalanceFlow==null,orderId+"流水记录不存在,不可提交");
        if (Objects.equals(UserBalanceFlow.STATUS_ENUM.OK.getStatus(),userBalanceFlow.getStatus())){
            log.warn("{}已经是完成状态,忽略重复提交",orderId);
            return;
        }
        AssertUtil.businessInvalid(!Objects.equals(UserBalanceFlow.STATUS_ENUM.PRE.getStatus(),userBalanceFlow.getStatus()),orderId+"流水记录不是准备状态,不可提交");
        //更新流水状态
        UserBalanceFlow userBalanceFlowNew = new UserBalanceFlow();
        userBalanceFlowNew.setId(userBalanceFlow.getId());
        userBalanceFlowNew.setStatus(UserBalanceFlow.STATUS_ENUM.OK.getStatus());
        userBalanceFlowNew.setUpdateTime(new Date());
        userBalanceFlowService.updateById(userBalanceFlowNew);

        //冻结金额释放
        User user = userService.getUserByUserIdForUpdate(userId);
        User userNew = new User();
        userNew.setId(user.getId());
        userNew.setFreezeBalance(user.getFreezeBalance().subtract(amount));
        userService.updateById(userNew);
        if (commitError){
            AssertUtil.businessInvalid("try:error");
        }
        log.info("tcc:commit:完成");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollback(String userId, BigDecimal amount, Long orderId) {
        log.info("tcc:rollback:开始");
        UserBalanceFlow userBalanceFlow = userBalanceFlowService.findByOrderIdForUpdate(orderId);
        if (rollbackError){
            AssertUtil.businessInvalid("try:error");
        }
        //不存在流水记录，可能是try超时，然后调用了rollback，插入一条已取消的流水，确保后续的try不会执行
        //这里insert的orderId有唯一索引，所以要不rollback插入异常，要不try插入异常，rollback异常的话会重试rollback然后走正常判断；try异常的话，也会走rollback，所以也没问题
        if (userBalanceFlow == null){
            log.warn("{}流水不存在,插入取消状态",orderId);
            userBalanceFlow =  new UserBalanceFlow();
            userBalanceFlow.setOrderId(orderId);
            userBalanceFlow.setUserId(userId);
            userBalanceFlow.setAmount(amount);
            userBalanceFlow.setType(UserBalanceFlow.TYPE_ENUM.SUB.getType());
            userBalanceFlow.setStatus(UserBalanceFlow.STATUS_ENUM.CANCEL.getStatus());
            userBalanceFlow.setCreateTime(new Date());
            userBalanceFlow.setUpdateTime(new Date());
            userBalanceFlowService.save(userBalanceFlow);
            log.info("tcc:rollback:流水不存在:完成");
            return;
        }
        if (Objects.equals(UserBalanceFlow.STATUS_ENUM.CANCEL.getStatus(),userBalanceFlow.getStatus())){
            log.warn("{}是回滚状态，忽略重复提交",orderId);
            return;
        }
        AssertUtil.businessInvalid(Objects.equals(UserBalanceFlow.STATUS_ENUM.OK.getStatus(),userBalanceFlow.getStatus()),orderId+"已经提交的事务不能进行回滚");
        //更新流水状态
        UserBalanceFlow userBalanceFlowNew = new UserBalanceFlow();
        userBalanceFlowNew.setId(userBalanceFlow.getId());
        userBalanceFlowNew.setStatus(UserBalanceFlow.STATUS_ENUM.CANCEL.getStatus());
        userBalanceFlowNew.setUpdateTime(new Date());
        userBalanceFlowService.updateById(userBalanceFlowNew);

        //冻结金额释放
        User user = userService.getUserByUserIdForUpdate(userId);
        User userNew = new User();
        userNew.setId(user.getId());
        userNew.setBalance(user.getBalance().add(amount));
        userNew.setFreezeBalance(user.getFreezeBalance().subtract(amount));
        userService.updateById(userNew);
        log.info("tcc:rollback:完成");
    }
}
