package org.pileka.notification_service.repository;

import org.pileka.notification_service.model.Subscription;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    Iterable<Subscription> findByUser(Long userId);

    Iterable<Subscription> findByBird(Long birdId);
}
