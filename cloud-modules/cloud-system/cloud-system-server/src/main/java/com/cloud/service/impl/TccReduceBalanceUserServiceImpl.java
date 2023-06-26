package com.cloud.service.impl;

import com.cloud.entity.User;
import com.cloud.service.TccReduceBalanceUserService;
import com.cloud.service.UserService;
import com.cloud.utils.AssertUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * tcc扣减金额
 *
 * @author guojianbo
 * @date 2023/6/26 17:50
 */
@Service
@Log4j2
public class TccReduceBalanceUserServiceImpl implements TccReduceBalanceUserService {
    @Autowired
    private UserServiceImpl userService;

    /**
     * todo:如何避免重复提交，幂等解决：
     * 可以由调用方传递交易流水号,在try阶段查询改流水号是否存在记录，不存在则插入流水表，并记录状态，
     * 然后commit、rollback查询该流水记录,对比状态，做相应操作，避免由于seata重试而出现问题
     * @param userId 用户id
     * @param amount 金额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void prepare(String userId, BigDecimal amount) {
        User user = userService.getUserByUserIdForUpdate(userId);
        AssertUtil.businessInvalid(user.getBalance().compareTo(amount)<0,"用户余额不足");
        user.setBalance(user.getBalance().subtract(amount));
        user.setFreezeBalance(user.getFreezeBalance().add(amount));
        user.setUpdateTime(new Date());
        userService.updateById(user);
        log.info("tcc:try:完成");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void commit(String userId, BigDecimal amount) {
        User user = userService.getUserByUserIdForUpdate(userId);
        user.setFreezeBalance(user.getFreezeBalance().subtract(amount));
        user.setUpdateTime(new Date());
        userService.updateById(user);
        log.info("tcc:commit:完成");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rollback(String userId, BigDecimal amount) {
        User user = userService.getUserByUserIdForUpdate(userId);
        user.setBalance(user.getBalance().add(amount));
        user.setFreezeBalance(user.getFreezeBalance().subtract(amount));
        user.setUpdateTime(new Date());
        userService.updateById(user);
        log.info("tcc:rollback:完成");
    }
}
