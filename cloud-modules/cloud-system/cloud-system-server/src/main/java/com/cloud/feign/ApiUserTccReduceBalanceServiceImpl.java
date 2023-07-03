package com.cloud.feign;

import com.alibaba.fastjson.JSONObject;
import com.cloud.annotation.ExcludeFromGlobalException;
import com.cloud.domain.request.TccReduceBalanceDTO;
import com.cloud.remote.ApiUserTccReduceBalanceService;
import com.cloud.service.TccReduceBalanceUserService;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = "用户TCC扣减金额")
public class ApiUserTccReduceBalanceServiceImpl implements ApiUserTccReduceBalanceService {

    @Autowired
    private TccReduceBalanceUserService tccReduceBalanceUserService;

    @Override
    @ApiOperation("try接口")
    @ExcludeFromGlobalException
    @PostMapping(value = "/prepare")
    public boolean prepare(@RequestBody TccReduceBalanceDTO dto) {
        tccReduceBalanceUserService.prepare(dto.getUserId(), dto.getAmount(), dto.getOrderId());
        return true;
    }

    @Override
    @ApiOperation("commit接口")
    @ExcludeFromGlobalException
    @PostMapping(value = "/commit")
    public boolean commit(@RequestBody BusinessActionContext actionContext) {
        TccReduceBalanceDTO dto = JSONObject.parseObject(JSONObject.toJSONString(actionContext.getActionContext("params")), TccReduceBalanceDTO.class);
        tccReduceBalanceUserService.commit(dto.getUserId(), dto.getAmount(), dto.getOrderId());
        return true;
    }

    @Override
    @ApiOperation("rollback接口")
    @ExcludeFromGlobalException
    @PostMapping(value = "/rollback")
    public boolean rollback(@RequestBody BusinessActionContext actionContext) {
        TccReduceBalanceDTO dto = JSONObject.parseObject(JSONObject.toJSONString(actionContext.getActionContext("params")), TccReduceBalanceDTO.class);
        tccReduceBalanceUserService.rollback(dto.getUserId(), dto.getAmount(), dto.getOrderId());
        return true;
    }
}
