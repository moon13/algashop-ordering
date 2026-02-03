package com.algaworks.algashop.ordering.domain.model.exceptions;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderId;

import static com.algaworks.algashop.ordering.domain.model.exceptions.ErrorMessages.ERROR_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST;

public class OrderInvalidShippingDeliveryDateException extends DomainException{
    public OrderInvalidShippingDeliveryDateException(OrderId id) {
        super(String.format(ERROR_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST,id));
    }
}
