package com.cloud.domain.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建订单
 *
 * @author guojianbo
 * @date 2023/6/26 12:01
 */
@Data
public class CreateOrderReqDTO {

    private BigDecimal orderAmount;
}
