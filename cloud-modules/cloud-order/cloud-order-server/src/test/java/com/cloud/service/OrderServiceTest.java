package com.cloud.service;

import com.cloud.domain.request.CreateOrderReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

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
    public void create2(){
        CreateOrderReqDTO reqDTO = new CreateOrderReqDTO();
        reqDTO.setOrderAmount(new BigDecimal("4"));
        orderService.create2("123","张三",reqDTO);
        System.out.println("create2:end");
    }
}
