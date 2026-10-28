package org.pileka.notification_service.repository;

import org.pileka.notification_service.model.Subscription;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long>, QuerydslPredicateExecutor<Subscription> { }
