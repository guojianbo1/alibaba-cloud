package com.cloud.remote;

import com.cloud.domain.request.TccReduceBalanceDTO;
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
     * 特別说明：无论是默认的XA模式，还是TCC模式，都无法解决以下问题：
     * 由于一个资源的数据同时被A、B两种场景做修改，导致A场景修改完成未二阶段提交后，
     * B场景也进行修改并且执行了二阶段提交，此时A由于后面操作失败需要回滚，
     * 但是数据已被B修改导致A无法回滚成功从而一直在重试回滚：PhaseTwo_RollbackFailed_Retryable
     *
     * 所以如果存在多种场景同时修改某个资源数据时，可以考虑以下解决方案：
     * 1.优化事务范围：尽量缩小事务的范围，只将必要的操作包含在一个事务中。
     *   这样可以降低不同场景之间的并发冲突概率，减少数据回滚的可能性。
     *
     * 2.乐观锁机制：在涉及到并发修改的场景下，使用乐观锁来解决冲突。
     *   乐观锁通常是通过记录数据的版本号或时间戳，并在更新操作时检查版本号或时间戳是否匹配来判断是否存在冲突。
     *   如果冲突发生，可以回滚事务或重试。
     *
     * 3.串行化处理：对于无法避免并发修改的场景，可以使用串行化处理方式来确保数据的一致性。
     *   通过将并发访问同一资源的操作串行化，可以避免并发冲突，保证数据的正确性。但是这种方式会牺牲并发性能，需要权衡使用。
     *   (例如:1.在try时获取该资源全局锁[分布式锁实现]，直到commit\rollback时才进行锁的释放。
     *   优点：无需调用方配合，自己服务实现即可；
     *   缺点：阻塞操作，被调用方需要等待锁的释放后才能执行try.
     *   2.案例正在使用的：通过 SELECT FOR UPDATE 语句，Seata 在通过ELECT FOR UPDATE 语句的执行会向TC申请全局锁，如果全局锁
     *   被其它的seata事务持有，则会获取失败抛出io.seata.rm.datasource.exec.LockWaitTimeoutException: Global lock wait timeout异常。)
     *
     * 4.手动修复：如果以上方法都无法解决问题，可以考虑手动修复数据。通过人工介入，
     *   对数据进行修复操作，使其回到一致的状态。这种方法需要谨慎操作，并且可能需要额外的业务逻辑支持。
     *
     * @return
     */
    @TwoPhaseBusinessAction(name = "userTccReduceBalanceService", commitMethod = "commit", rollbackMethod = "rollback")
    @RequestMapping(value = "/prepare", method = RequestMethod.POST)
    Result<Void> prepare(@RequestBody @BusinessActionContextParameter(paramName = "params")  TccReduceBalanceDTO params);

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
     * TCC在失败时它会自动回滚try\commit阶段已经执行的操作,也就是在rollback前它会尝试进行PhaseTwo_Rollback
     *
     * 接口中定义的rollbackMethod方法实际上是作为一个备用方法存在的。
     * 如果在Cancel阶段执行失败或发生异常时，Seata会回滚事务，并尝试执行rollbackMethod方法作为最后的回滚操作。
     * 这样可以确保即使Confirm阶段的回滚发生异常，也有一个回滚方法可供使用，以保证事务的最终一致性。
     *
     * 所以在rollback方法中，需要判断资源是否已经由于PhaseTwo_Rollback操作进行了回滚，从而避免进行重复的补偿操作
     * @param actionContext save xid
     * @return the boolean
     */
    @RequestMapping(value = "/rollback", method = RequestMethod.POST)
    boolean rollback(@RequestBody BusinessActionContext actionContext);
}
