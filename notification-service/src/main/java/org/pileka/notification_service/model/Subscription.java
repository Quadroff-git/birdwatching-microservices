package org.pileka.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@AllArgsConstructor
@Getter
public class Subscription {

    @Id
    private final Long id;

    private final Long userId;

    private final Long birdId;

    @CreatedDate
    private final LocalDateTime createdAt;
}
