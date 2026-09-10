package org.pileka.notification_service.dto;

import java.time.LocalDateTime;

public class QuerySubscriptionDTO {

    private Long userId;

    private Long birdId;

    private LocalDateTime createdAfter;

    private LocalDateTime createdBefore;

}
