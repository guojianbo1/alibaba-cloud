package com.cloud.domain.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * tcc参数
 *
 * @author guojianbo
 * @date 2023/6/27 8:53
 */
@Data
public class TccReduceBalanceDTO {
    @NotBlank(message = "userId不能为空")
    private String userId;
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;
    @NotNull(message = "订单id不能为空")
    private Long orderId;
}
