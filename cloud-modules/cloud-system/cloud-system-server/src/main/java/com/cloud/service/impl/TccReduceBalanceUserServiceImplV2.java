package com.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.domain.request.TccReduceBalanceDTO;
import com.cloud.entity.User;
import com.cloud.entity.UserBalanceFlow;
import com.cloud.service.TccReduceBalanceUserService;
import com.cloud.service.TccReduceBalanceUserServiceV2;
import com.cloud.service.UserBalanceFlowService;
import com.cloud.utils.AssertUtil;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


/**
 * tcc扣减金额
 *
 * @author guojianbo
 * @date 2023/6/26 17:50
 */
@Service
@Log4j2
@RefreshScope
public class TccReduceBalanceUserServiceImplV2 implements TccReduceBalanceUserServiceV2 {
    @Autowired
    private TccReduceBalanceUserService tccReduceBalanceUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean prepare(TccReduceBalanceDTO params) {
        tccReduceBalanceUserService.prepare(params.getUserId(),params.getAmount(),params.getOrderId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean commit(BusinessActionContext actionContext) {
        TccReduceBalanceDTO dto = JSONObject.parseObject(JSONObject.toJSONString(actionContext.getActionContext("params")), TccReduceBalanceDTO.class);
        tccReduceBalanceUserService.commit(dto.getUserId(),dto.getAmount(),dto.getOrderId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rollback(BusinessActionContext actionContext) {
        TccReduceBalanceDTO dto = JSONObject.parseObject(JSONObject.toJSONString(actionContext.getActionContext("params")), TccReduceBalanceDTO.class);
        tccReduceBalanceUserService.rollback(dto.getUserId(),dto.getAmount(),dto.getOrderId());
        return true;
    }
}
