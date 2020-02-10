/*
package com.kakaocorp.iamguide.config;

import net.daum.tenth2.Tenth2Authentication;
import net.daum.tenth2.Tenth2Config;
import net.daum.tenth2.Tenth2Connector;
import net.daum.tenth2.exceptions.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class TenthConfiguration {
    Logger logger = LoggerFactory.getLogger(TenthConfiguration.class);
    @Value("${tenth.host}")
    private String tenthHost;

    @Value("${tenth.key.read}")
    private String readKey;

    @Value("${tenth.key.write}")
    private String writeKey;

    @Value("${tenth.serviece.name}")
    private String serviceId;

    @PostConstruct
    public void init() {
        // 사용할 서비스의 read key, write key를 먼저 등록합니다.
        try {
            Tenth2Authentication.addAccessKey(serviceId, readKey, writeKey);
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }

        try {
            // 서버 지정.
            Tenth2Connector.setHost(new Tenth2Connector.HostInfo(tenthHost));
            Tenth2Config.setConnTimeout(3000);
            Tenth2Config.setMaxConnRetry(3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("Tenth Config\n {} \n {}", tenthHost, serviceId);
    }
}

*/
