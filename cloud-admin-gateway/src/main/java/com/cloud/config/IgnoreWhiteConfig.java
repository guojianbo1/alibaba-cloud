package com.cloud.config;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 放行白名单配置
 *
 * @author guojianbo
 * @date 2023/6/16 10:46
 */
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "ignore")
@Data
@Log4j2
public class IgnoreWhiteConfig {

    /**
     * 放行白名单配置，网关不校验此处的白名单
     */
    private List<String> whites = new ArrayList<>();
    public void setWhites(List<String> whites) {
        log.info("加载网关路径白名单:{}", JSONObject.toJSONString(whites));
        this.whites = whites;
    }
}
