package com.cloud.controller.admin;


import com.cloud.annotation.AdminDrawUser;
import com.cloud.domain.request.CreateOrderReqDTO;
import com.cloud.result.Result;
import com.cloud.service.OrderService;
import com.cloud.utils.AdminUserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author guojianbo
 * @since 2023-06-26
 */
@Log4j2
@RestController
@CrossOrigin
@AdminDrawUser
@RequestMapping("/admin/order")
@Api(tags = "订单管理")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping(value = "/create")
    public Result<Void> create(@RequestBody @Validated CreateOrderReqDTO reqDTO) {
        orderService.create(AdminUserUtils.getUserId(),AdminUserUtils.getUserName(),reqDTO);
        return Result.success();
    }

    @PostMapping(value = "/create2")
    public Result<Void> create2(@RequestBody @Validated CreateOrderReqDTO reqDTO) {
        orderService.create2(AdminUserUtils.getUserId(),AdminUserUtils.getUserName(),reqDTO);
        return Result.success();
    }
}

