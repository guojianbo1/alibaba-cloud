package com.cloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.annotation.AdminDrawUser;
import com.cloud.constant.JwtConstants;
import com.cloud.constant.TokenConstants;
import com.cloud.domain.request.LoginReq;
import com.cloud.domain.response.LoginResp;
import com.cloud.remote.ApiUserService;
import com.cloud.result.Result;
import com.cloud.utils.AdminUserUtils;
import com.cloud.utils.AssertUtil;
import com.cloud.utils.JwtUtils;
import com.cloud.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * B端鉴权
 *
 * @author guojianbo
 * @date 2023/6/19 14:04
 */
@Log4j2
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminCheckAuthController {
    @Autowired
    private ApiUserService apiUserService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @PostMapping(value = "/login")
    public Result<LoginResp> login(@RequestBody LoginReq req) {
        AssertUtil.businessInvalid(!Objects.equals(req.getPwd(),"123456"),"密码不正确");
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtConstants.USER_KEY,"key:"+req.getUserId());
        claims.put(JwtConstants.DETAILS_USER_ID,req.getUserId());
        claims.put(JwtConstants.DETAILS_USERNAME,"张三");
        claims.put(JwtConstants.TIME_KEY,System.currentTimeMillis());
        String token = JwtUtils.createToken(claims);
        Result<List<String>> result = apiUserService.getAllMenu(req.getUserId());
        AssertUtil.businessInvalid(!result.isSuccess(),"查询菜单失败");
        String key = TokenConstants.LOGIN_TOKEN_KEY+claims.get(JwtConstants.USER_KEY);
        claims.put(TokenConstants.TOKEN,token);
        redisTemplate.opsForValue().set(key,JSONObject.toJSONString(claims),TokenConstants.EXPIRATION,TimeUnit.MINUTES);
        return Result.success(new LoginResp(req.getUserId(),token,result.getData()));
    }

    @AdminDrawUser
    @PostMapping(value = "/logout")
    public Result<Void> logout() {
        String key = TokenConstants.LOGIN_TOKEN_KEY+AdminUserUtils.getUserKey();
        redisTemplate.delete(key);
        return Result.success();
    }

    @AdminDrawUser
    @PostMapping(value = "/checkMenu")
    public Result<Boolean> checkMenu(@RequestParam("menuCode") String menuCode) {
        String key = "cloud:auth:user:menu:"+AdminUserUtils.getUserId();
//        String values = redisTemplate.opsForValue().get(key);
        List<String> menuList;
//        if (StringUtils.isEmpty(values)) {
            Result<List<String>> result = apiUserService.getAllMenu(AdminUserUtils.getUserId());
            log.info("result:"+JSONObject.toJSONString(result));
            AssertUtil.businessInvalid(!result.isSuccess(),result.getCode(),result.getMsg());
            menuList = result.getData();
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(menuList),60, TimeUnit.SECONDS);
//        }else{
//            menuList = JSONObject.parseArray(values,String.class);
//        }
        return Result.success(menuList.contains(menuCode));
    }
}
