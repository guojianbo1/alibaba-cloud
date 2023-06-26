package com.cloud.remote;

import com.cloud.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户接口
 *
 * @author guojianbo
 * @date 2023/6/19 13:53
 */
@FeignClient(name = "cloud-system", path = "/system/api/integralRecord")
public interface ApiIntegralRecordService {

    /**
     * 保存积分记录事务
     * @param userId 用户id
     * @param orderId 订单id
     * @param amount 积分
     * @return 结果
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> save(@RequestParam("userId") String userId,
                             @RequestParam("orderId") Long orderId,
                             @RequestParam("amount") BigDecimal amount);

}
