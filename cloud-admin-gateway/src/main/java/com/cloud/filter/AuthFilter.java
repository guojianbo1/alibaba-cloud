package com.cloud.filter;

import com.alibaba.fastjson.JSONObject;
import com.cloud.config.IgnoreWhiteConfig;
import com.cloud.constant.JwtConstants;
import com.cloud.constant.TokenConstants;
import com.cloud.utils.GatewayUtils;
import com.cloud.utils.JwtUtils;
import com.cloud.utils.StringUtils;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * 鉴权过滤器
 *
 * @author guojianbo
 * @date 2023/6/16 10:56
 */
@Component
@Log4j2
public class AuthFilter implements GlobalFilter, Ordered {
    /**
     * redis操作
     */
    private final StringRedisTemplate redisTemplate;
    /**
     * 白名单
     */
    private final IgnoreWhiteConfig ignoreWhite;
    public AuthFilter(StringRedisTemplate redisTemplate, IgnoreWhiteConfig ignoreWhite) {
        this.redisTemplate = redisTemplate;
        this.ignoreWhite = ignoreWhite;
    }

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 请求作用域
        ServerHttpRequest request = exchange.getRequest();
        // 请求头
        HttpHeaders headers = request.getHeaders();
        // 请求方式
        HttpMethod method = request.getMethod();
        // header操作对象
        ServerHttpRequest.Builder mutate = request.mutate();
        String uri = request.getURI().getPath();
        log.info("请求日志：uri:[{}] , 请求方式:[{}]", uri, method);
        // 跳过不需要验证的路径
        if (StringUtils.matches(uri, ignoreWhite.getWhites())) {
            return chain.filter(exchange);
        }
        String token = headers.getFirst(TokenConstants.TOKEN);
        if (StringUtils.isEmpty(token)) {
            return GatewayUtils.errorResponse(exchange, "令牌不能为空");
        }
        Claims claims = JwtUtils.parseToken(token);
        if (claims == null) {
            return GatewayUtils.errorResponse(exchange, "令牌已过期或验证不正确！");
        }
        String userKey = JwtUtils.getUserKey(claims);
        String values = redisTemplate.opsForValue().get(TokenConstants.LOGIN_TOKEN_KEY + userKey);
        if (StringUtils.isEmpty(values)) {
            return GatewayUtils.errorResponse(exchange, "登录状态已过期");
        }
        Map<String,String> map = JSONObject.parseObject(values, Map.class);
        String redisToken = map.get(TokenConstants.TOKEN);
        if (!Objects.equals(redisToken,token)){
            return GatewayUtils.errorResponse(exchange, "登录状态已过期");
        }
        String userid = JwtUtils.getUserId(claims);
        String username = JwtUtils.getUserName(claims);
        // 设置用户信息到请求
        GatewayUtils.addHeader(mutate, JwtConstants.USER_KEY, userKey);
        GatewayUtils.addHeader(mutate, JwtConstants.DETAILS_USER_ID, userid);
        GatewayUtils.addHeader(mutate, JwtConstants.DETAILS_USERNAME, URLEncoder.encode(username, StandardCharsets.UTF_8.toString()));
        // 内部请求来源参数清除
        GatewayUtils.removeHeader(mutate, TokenConstants.TOKEN);
        return chain.filter(exchange.mutate().request(mutate.build()).build());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
