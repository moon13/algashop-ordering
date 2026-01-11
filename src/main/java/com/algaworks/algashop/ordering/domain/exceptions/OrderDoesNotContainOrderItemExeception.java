package com.algaworks.algashop.ordering.domain.exceptions;

import com.algaworks.algashop.ordering.domain.valueobject.id.OrderId;
import com.algaworks.algashop.ordering.domain.valueobject.id.OrderItemId;

public class OrderDoesNotContainOrderItemExeception extends DomainException{
    public OrderDoesNotContainOrderItemExeception(OrderId id, OrderItemId orderItemId){
        super(String.format(ErrorMessages.ERROR_ORDER_DOES_NOT_CONTAIN_ITEM,id,orderItemId));
    }
}
