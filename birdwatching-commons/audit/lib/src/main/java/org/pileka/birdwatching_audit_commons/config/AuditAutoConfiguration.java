package org.pileka.birdwatching_audit_commons.config;

import org.pileka.birdwatching_audit_commons.audit.AuditAccessInterceptor;
import org.pileka.birdwatching_audit_commons.audit.AuditLogger;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({SecurityContextHolder.class, WebMvcConfigurer.class})
public class AuditAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AuditLogger auditLogger() {
        return new AuditLogger();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuditAccessInterceptor auditAccessInterceptor(AuditLogger auditLogger) {
        return new AuditAccessInterceptor(auditLogger);
    }

    @Bean
    public WebMvcConfigurer auditWebMvcConfigurer(AuditAccessInterceptor auditAccessInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(auditAccessInterceptor);
            }
        };
    }
}
