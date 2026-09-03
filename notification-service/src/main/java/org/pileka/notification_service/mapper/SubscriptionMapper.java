package org.pileka.notification_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.pileka.notification_service.dto.CreateSubscriptionDTO;
import org.pileka.notification_service.dto.ResponseSubscriptionDTO;
import org.pileka.notification_service.model.Subscription;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SubscriptionMapper {

    ResponseSubscriptionDTO toDto(Subscription model);

    Subscription toModel(CreateSubscriptionDTO dto);

}
