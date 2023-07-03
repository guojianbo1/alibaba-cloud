package com.cloud.swagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.env.Environment;

/**
 * @author guojianbo
 * @date 2023/6/21 17:30
 */
@ConditionalOnBean(Environment.class)
public class PrintSwaggerRunner implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) {
        if (checkOpenSwagger(environment)){
            log.info("swagger3访问链接: {}/doc.html#/", getLocalHost());
        }else{
            log.info("swagger:spring.mvc.pathmatch.matching-strategy不是ant-path-matcher,无法开启swagger");
        }
    }

    public static boolean checkOpenSwagger(Environment environment){
        String property = environment.getProperty("spring.mvc.pathmatch.matching-strategy");
        return "ant_path_matcher".equalsIgnoreCase(property);
    }

    private String getLocalHost() {
        String ip = environment.getProperty("spring.cloud.client.ip-address");
        String port = environment.getProperty("server.port");
        String contextPath = environment.getProperty("server.servlet.context-path");
        if (contextPath==null){
            contextPath = "";
        }
        return "http://" + ip + ":" + port + contextPath;
    }
}
