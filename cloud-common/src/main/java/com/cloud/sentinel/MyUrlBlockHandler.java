package com.cloud.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.cloud.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义sentinel返回值
 *
 * @author guojianbo
 * @date 2023/6/20 15:51
 */
@Component
public class MyUrlBlockHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {
        Result<Void> result = getResult(ex);
        // http状态码
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setContentType("application/json;charset=utf-8");
        new ObjectMapper().writeValue(response.getWriter(), result);
    }

    public static Result<Void> getResult(BlockException ex) {
        Result<Void> result = null;
        if (ex instanceof FlowException) {
            String resource = ex.getRule().getResource();
            result = Result.error(100, resource+"限流了");
        } else if (ex instanceof DegradeException) {
            String resource = ex.getRule().getResource();
            result = Result.error(101, resource+"降级了");
        } else if (ex instanceof ParamFlowException) {
            String resource = ex.getRule().getResource();
            result = Result.error(102, resource+"热点参数限流");
        } else if (ex instanceof SystemBlockException) {
            String ruleLimitApp = ex.getRuleLimitApp();
            result = Result.error(103, "系统规则（负载/"+ruleLimitApp+"不满足要求）");
        } else if (ex instanceof AuthorityException) {
            String ruleLimitApp = ex.getRuleLimitApp();
            result = Result.error(104, ruleLimitApp+"授权规则不通过");
        }
        return result;
    }
}
