package org.pileka.notification_service.service;

import org.pileka.notification_service.dto.CreateSubscriptionDTO;
import org.pileka.notification_service.dto.QuerySubscriptionDTO;
import org.pileka.notification_service.dto.ResponseSubscriptionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

public interface SubscriptionService {

    @PreAuthorize("authentication.name == #createDto.getUserId().toString() || hasRole('ADMIN')")
    ResponseSubscriptionDTO create(CreateSubscriptionDTO createDto);

    ResponseSubscriptionDTO getById(long id);

    Page<ResponseSubscriptionDTO> get(QuerySubscriptionDTO queryDto, Pageable pageable);

    @PreAuthorize("authentication.name == #id.toString() || hasRole('ADMIN')")
    ResponseSubscriptionDTO delete(long id);
    
}
