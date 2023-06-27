package com.cloud.domain.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * tcc参数
 *
 * @author guojianbo
 * @date 2023/6/27 8:53
 */
@Data
public class TccReduceBalanceDTO {
    private String userId;
    private BigDecimal amount;
    private Long orderId;
}
