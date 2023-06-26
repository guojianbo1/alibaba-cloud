package com.cloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.annotation.ExcludeFromGlobalException;
import com.cloud.result.Result;
import com.cloud.service.TccReduceBalanceUserService;
import com.cloud.service.UserService;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户接口
 *
 * @author guojianbo
 * @date 2023/6/19 13:53
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping("/api/user/tcc/reduce/balance")
public class ApiUserTccReduceBalanceController {

    @Autowired
    private TccReduceBalanceUserService tccReduceBalanceUserService;

    @PostMapping(value = "/prepare")
    public Result<Void> prepare(@RequestBody Map<String, String> params) {
        String userId = params.get("userId");
        String amount = params.get("amount");
        tccReduceBalanceUserService.prepare(userId, new BigDecimal(amount));
        return Result.success();
    }

    @ExcludeFromGlobalException
    @PostMapping(value = "/commit")
    public boolean commit(@RequestBody BusinessActionContext actionContext) {
        Map<String,String> params = JSONObject.parseObject(JSONObject.toJSONString(actionContext.getActionContext("params")), Map.class);
        String userId = params.get("userId");
        String amount = params.get("amount");
        tccReduceBalanceUserService.commit(userId, new BigDecimal(amount));
        return true;
    }

    @ExcludeFromGlobalException
    @PostMapping(value = "/rollback")
    public boolean rollback(@RequestBody BusinessActionContext actionContext) {
        Map<String,String> params = JSONObject.parseObject(JSONObject.toJSONString(actionContext.getActionContext("params")), Map.class);
        String userId = params.get("userId");
        String amount = params.get("amount");
        tccReduceBalanceUserService.rollback(userId, new BigDecimal(amount));
        return true;
    }
}
