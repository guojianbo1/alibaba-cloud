package com.cloud.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.cloud.constant.Constants;
import com.cloud.result.Result;
import com.cloud.sentinel.MyUrlBlockHandler;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 统一异常处理
 * @author Guoqing.Lee
 * @date 2019年1月17日 上午9:50:06
 *
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Result<Void> exceptionHandler(Exception e) {
		log.error("Exception:", e);
		return Result.error();
	}
	
	@ExceptionHandler(value = BusinessException.class)
	@ResponseBody
	public Result<Void> businessExceptionHandler(BusinessException e) {
		log.info("business error : {}",e.getMessage(),e);
		return Result.error(e.getCode(),e.getMessage());
	}
}
