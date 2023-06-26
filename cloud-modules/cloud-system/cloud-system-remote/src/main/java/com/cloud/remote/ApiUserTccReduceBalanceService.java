package com.cloud.remote;

import com.cloud.result.Result;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户接口
 *
 * @author guojianbo
 * @date 2023/6/19 13:53
 */
@FeignClient(name = "cloud-system", path = "/system/api/user/tcc/reduce/balance")
@LocalTCC
public interface ApiUserTccReduceBalanceService {

    /**
     * 扣减账户余额
     * BusinessActionContextParameter注解 传递参数到二阶段中
     * @return
     */
    @TwoPhaseBusinessAction(name = "userTccReduceBalanceService", commitMethod = "commit", rollbackMethod = "rollback")
    @RequestMapping(value = "/prepare", method = RequestMethod.POST)
    Result<Void> prepare(@RequestBody @BusinessActionContextParameter(paramName = "params") Map<String, String> params);

    /**
     * Commit boolean.
     *
     * @param actionContext save xid
     * @return the boolean
     */
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    boolean commit(@RequestBody BusinessActionContext actionContext);

    /**
     * Rollback boolean.
     * TCC在失败时它会自动回滚commit阶段已经执行的操作
     *
     * 接口中定义的rollbackMethod方法实际上是作为一个备用方法存在的。
     * 如果在Cancel阶段执行失败或发生异常时，Seata会回滚事务，并尝试执行rollbackMethod方法作为最后的回滚操作。
     * 这样可以确保即使Confirm阶段的回滚发生异常，也有一个回滚方法可供使用，以保证事务的最终一致性。
     * @param actionContext save xid
     * @return the boolean
     */
    @RequestMapping(value = "/rollback", method = RequestMethod.POST)
    boolean rollback(@RequestBody BusinessActionContext actionContext);
}
