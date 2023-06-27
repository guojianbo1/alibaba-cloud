package com.cloud.service;

import java.math.BigDecimal;

/**
 * tcc扣减金额
 *
 * @author guojianbo
 * @date 2023/6/26 17:49
 */
public interface TccReduceBalanceUserService {

    /**
     * tcc扣减金额try
     * @param userId 用户id
     * @param amount 金额
     * @param orderId 订单id
     */
    void prepare(String userId, BigDecimal amount, Long orderId);

    /**
     * tcc扣减金额commit
     * @param userId 用户id
     * @param amount 金额
     */
    void commit(String userId, BigDecimal amount, Long orderId);

    /**
     * tcc扣减金额rollback
     * @param userId 用户id
     * @param amount 金额
     */
    void rollback(String userId, BigDecimal amount, Long orderId);
}
