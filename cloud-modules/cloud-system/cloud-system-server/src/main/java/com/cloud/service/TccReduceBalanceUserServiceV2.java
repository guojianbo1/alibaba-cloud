package com.cloud.service;

import com.cloud.domain.request.TccReduceBalanceDTO;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

/**
 * tcc扣减金额
 *
 * @author guojianbo
 * @date 2023/6/26 17:49
 */
@LocalTCC
public interface TccReduceBalanceUserServiceV2 {

    /**
     * tcc扣减金额try
     * @param params 用户参数
     * @return 结果
     */
    @TwoPhaseBusinessAction(name = "userTccReduceBalanceService", commitMethod = "commit", rollbackMethod = "rollback")
    boolean prepare(@BusinessActionContextParameter(paramName = "params") TccReduceBalanceDTO params);

    /**
     * tcc扣减金额commit，commit如果发生异常后会一直进行重试
     * @param actionContext 上下文参数
     * @return 结果
     */
    boolean commit(BusinessActionContext actionContext);

    /**
     * tcc扣减金额rollback，rollback如果发生异常后会一直进行重试
     * @param actionContext 上下文参数
     * @return 结果
     */
    boolean rollback(BusinessActionContext actionContext);
}
