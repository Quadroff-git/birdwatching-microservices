package org.pileka.bird_service.logging;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PerformanceConfig {

    @Bean
    public PerformanceMonitor performanceMonitor(
            @Value("${performance.slow-threshold-ms:1000}") long slowThresholdMs) {
        return new PerformanceMonitor(
                LoggerFactory.getLogger(PerformanceMonitor.LOGGER_NAME),
                slowThresholdMs
        );
    }
}
