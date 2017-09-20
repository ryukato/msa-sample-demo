package com.msa.cqrs.commandservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;

@ConditionalOnExpression("'${spring.profiles.active}' != 'dev'")
@Configuration
@EnableEurekaClient
public class EurekaConfiguration {
}
