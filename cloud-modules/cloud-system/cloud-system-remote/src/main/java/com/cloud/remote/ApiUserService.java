package com.cloud.remote;

import com.cloud.domain.request.TccReduceBalanceDTO;
import com.cloud.result.Result;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户接口
 *
 * @author guojianbo
 * @date 2023/6/19 13:53
 */
@FeignClient(name = "cloud-system", path = "/system/api/user")
public interface ApiUserService {

    /**
     * 获取用户拥有权限的所有菜单
     * @param userId 用户id
     * @return 结果
     */
    @RequestMapping(value = "/getAllMenu", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<List<String>> getAllMenu(@RequestParam("userId") String userId ) throws InterruptedException;

    /**
     * XA扣减用户余额
     * XA模式存在一个问题：
     *      既必须保证修改资源要获取该资源的全局锁，否则在二阶段回滚前如果发生资源数据被其它线程修改，则
     *      导致undo_log备份数据与数据库数据不一致所以无法回滚，从而无限次进行回滚。
     *      seata在XA模式下对SELECT FOR UPDATE代理进行全局锁控制
     * @param userId 用户id
     * @param balance 扣减金额
     * @return 结果
     */
    @RequestMapping(value = "/reduceBalance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> reduceBalance(@RequestParam("userId") String userId, @RequestParam("balance") BigDecimal balance);

    /**
     * tcc扣减用户余额
     * 这里使用tcc可以有两种方式：
     * 1.就是当前接口的方式，提供一个开启全局事务的接口，接口在调tcc的try方法，
     *   这样无论调用方是否开启全局事务tcc都能完整执行try-->commit/rollback
     *
     * 2.ApiUserTccReduceBalanceService直接feign提供try,commit,rollback三个方法，调用方如果开启了全局事务则只需要调try即可；
     *   如果调用方没开启全局事务则需要手动调用try--->commit/rollback。
     *   这种存在问题：try完后，try的方法会按XA方式在undo_log创建，如果try后面发生异常，它会先按XA模式走回滚try的数据，
     *   在走rollback，如果期间try的数据被修改，则XA的回滚会失败然后无限重试。
     * 综上所述：建议使用第1钟方式
     * @param dto 参数
     * @return 结果
     */
    @RequestMapping(value = "/tccReduceBalance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Result<Void> tccReduceBalance(@RequestBody @Validated TccReduceBalanceDTO dto);
}
