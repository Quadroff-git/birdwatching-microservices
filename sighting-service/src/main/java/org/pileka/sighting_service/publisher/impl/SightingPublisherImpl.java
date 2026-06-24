package org.pileka.sighting_service.publisher.impl;

import org.pileka.sighting_service.dto.ResponseSightingDTO;
import org.pileka.sighting_service.publisher.SightingPublisher;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SightingPublisherImpl implements SightingPublisher {

    private final AmqpTemplate template;

    private final String queueName;

    public SightingPublisherImpl(AmqpTemplate template, @Value("${api.queue.name}") String queueName) {
        this.template = template;
        this.queueName = queueName;
    }

    @Override
    public void publish(ResponseSightingDTO dto) {
        template.convertAndSend(queueName, dto);
    }
}
