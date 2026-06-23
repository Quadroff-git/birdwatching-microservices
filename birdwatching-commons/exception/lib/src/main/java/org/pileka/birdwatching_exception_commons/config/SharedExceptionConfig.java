package org.pileka.birdwatching_exception_commons.config;

import org.pileka.birdwatching_exception_commons.handler.CommonAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnWebApplication
public class SharedExceptionConfig {

    @Bean
    CommonAdvice defaultControllerAdvice() {
        return new CommonAdvice();
    }

}
