package org.pileka.sighting_service.publisher;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SightingQueueConfig {

    // Bound directly to the default exchange, which suits my simple goals
    @Bean
    public Queue sightingQueue(@Value("${api.queue.name}") String queueName) {
        return new Queue(queueName);
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
