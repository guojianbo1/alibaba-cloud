package com.cloud.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.domain.request.CreateOrderReqDTO;
import com.cloud.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * test
 *
 * @author guojianbo
 * @date 2023/6/27 15:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test
    public void create2() {
        CreateOrderReqDTO reqDTO = new CreateOrderReqDTO();
        reqDTO.setOrderAmount(new BigDecimal("4"));
        orderService.create("123", "张三", reqDTO);
        System.out.println("create2:end");
    }

    @Test
    public void save() {


        Order order = new Order();
        order.setOrderId(System.currentTimeMillis());
        order.setUserId("123");
        order.setOrderAmount(new BigDecimal("10"));
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        orderService.save(order);
    }

    @Test
    public void get() {
        QueryWrapper<Order> query = new QueryWrapper<Order>()
                .eq(Order.USER_ID, "123")
                .and(wrapper -> wrapper
                        .eq(Order.CREATE_TIME, new Date())
                        .or()
                        .isNotNull(Order.CREATE_TIME)
                );
        List<Order> list = orderService.list(query);
        System.out.println(JSONObject.toJSONString(list));
    }
}
