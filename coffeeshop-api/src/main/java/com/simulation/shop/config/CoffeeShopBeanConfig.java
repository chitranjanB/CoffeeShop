package com.simulation.shop.config;

import com.simulation.shop.service.AuditLogService;
import com.simulation.shop.util.CoffeeUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class CoffeeShopBeanConfig {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private CoffeeUtility utility;

    @Autowired
    private CoffeeShopPropConfig config;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(5000))
                .setReadTimeout(Duration.ofMillis(5000))
                .build();
    }
}