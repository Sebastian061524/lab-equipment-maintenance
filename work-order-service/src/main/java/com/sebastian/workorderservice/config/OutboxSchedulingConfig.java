package com.sebastian.workorderservice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "outbox.dispatch.enabled", havingValue = "true")
public class OutboxSchedulingConfig {
}
