package com.cloud.aspect;

import com.cloud.constant.JwtConstants;
import com.cloud.utils.AssertUtil;
import com.cloud.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author guojianbo
 * @date 2020/12/2 19:56
 */
@Log4j2
@Aspect
@Component
public class AdminDrawUserAspect {

    @Pointcut("@within(com.cloud.annotation.AdminDrawUser)")
    public void annotatedClass() {
    }

    @Pointcut("@annotation(com.cloud.annotation.AdminDrawUser)")
    public void annotatedMethod() {
    }

    @Before("annotatedClass() || annotatedMethod()")
    public void beforeMethodExecution(JoinPoint joinPoint) throws UnsupportedEncodingException {
        log.info("Before method execution");
        // 在这里编写你的逻辑代码，例如记录日志或进行权限验证等
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 获取请求头信息
        String userKey = request.getHeader(JwtConstants.USER_KEY);
        String userId = request.getHeader(JwtConstants.DETAILS_USER_ID);
        String userName = request.getHeader(JwtConstants.DETAILS_USERNAME);
        if (StringUtils.isNotEmpty(userName)) {
            userName = URLDecoder.decode(userName, StandardCharsets.UTF_8.toString());
        }
        AssertUtil.businessInvalid(StringUtils.isEmpty(userId),"用户登录失效");
        MDC.put(JwtConstants.USER_KEY,userKey);
        MDC.put(JwtConstants.DETAILS_USER_ID,userId);
        MDC.put(JwtConstants.DETAILS_USERNAME,userName);
    }

    @After("annotatedClass() || annotatedMethod()")
    public void afterMethodExecution(JoinPoint joinPoint) {
        log.info("After method execution");
        // 在这里编写你的逻辑代码
        MDC.remove(JwtConstants.USER_KEY);
        MDC.remove(JwtConstants.DETAILS_USER_ID);
        MDC.remove(JwtConstants.DETAILS_USERNAME);
    }

    // 可以根据需要添加其他通知，如@AfterReturning、@AfterThrowing等

}
