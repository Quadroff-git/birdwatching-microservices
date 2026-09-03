package org.pileka.notification_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Getter
public class Subscription {

    @Id
    private final Long id;

    private final Long userId;

    private final Long birdId;
}
