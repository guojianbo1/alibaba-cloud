package com.cloud.controller;


import com.cloud.annotation.AdminDrawUser;
import com.cloud.domain.request.CreateOrderReqDTO;
import com.cloud.result.Result;
import com.cloud.service.OrderService;
import com.cloud.utils.AdminUserUtils;
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
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/create")
    public Result<Void> create(@RequestBody @Validated CreateOrderReqDTO reqDTO) {
        orderService.create(AdminUserUtils.getUserId(),AdminUserUtils.getUserName(),reqDTO);
        return Result.success();
    }

}

