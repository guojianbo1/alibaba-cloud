package com.cloud.domain.request;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
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
    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "userId不能为空")
    private String userId;

    @ApiModelProperty(value = "扣减金额")
    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    @ApiModelProperty(value = "订单id")
    @NotNull(message = "订单id不能为空")
    private Long orderId;
}
