package com.cloud.fegin;

import com.alibaba.fastjson.JSONObject;
import com.cloud.annotation.ExcludeFromGlobalException;
import com.cloud.domain.request.TccReduceBalanceDTO;
import com.cloud.remote.ApiUserTccReduceBalanceService;
import com.cloud.service.TccReduceBalanceUserService;
import io.seata.rm.tcc.api.BusinessActionContext;
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
public class ApiUserTccReduceBalanceServiceImpl implements ApiUserTccReduceBalanceService {

    @Autowired
    private TccReduceBalanceUserService tccReduceBalanceUserService;

    @Override
    @ExcludeFromGlobalException
    @PostMapping(value = "/prepare")
    public boolean prepare(@RequestBody TccReduceBalanceDTO dto) {
        tccReduceBalanceUserService.prepare(dto.getUserId(), dto.getAmount(), dto.getOrderId());
        return true;
    }

    @Override
    @ExcludeFromGlobalException
    @PostMapping(value = "/commit")
    public boolean commit(@RequestBody BusinessActionContext actionContext) {
        TccReduceBalanceDTO dto = JSONObject.parseObject(JSONObject.toJSONString(actionContext.getActionContext("params")), TccReduceBalanceDTO.class);
        tccReduceBalanceUserService.commit(dto.getUserId(), dto.getAmount(), dto.getOrderId());
        return true;
    }

    @Override
    @ExcludeFromGlobalException
    @PostMapping(value = "/rollback")
    public boolean rollback(@RequestBody BusinessActionContext actionContext) {
        TccReduceBalanceDTO dto = JSONObject.parseObject(JSONObject.toJSONString(actionContext.getActionContext("params")), TccReduceBalanceDTO.class);
        tccReduceBalanceUserService.rollback(dto.getUserId(), dto.getAmount(), dto.getOrderId());
        return true;
    }
}
