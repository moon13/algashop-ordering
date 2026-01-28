package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exceptions.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderCancelTest {

    @Test
    void givenEmptyOrder_whenCancel_shouldAllow() {
        Order order = Order.draft(new CustomerId());

        order.cancel();

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.status()).isEqualTo(OrderStatus.CANCELED),
                (i) -> Assertions.assertThat(i.isCanceled()).isTrue(),
                (i) -> Assertions.assertThat(i.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenFilledOrder_whenCancel_shouldAllow() {
        Order order = OrderTestDataBuilder.anOrder().orderStatus(OrderStatus.DRAFT).build();

        order.cancel();

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.status()).isEqualTo(OrderStatus.CANCELED),
                (i) -> Assertions.assertThat(i.isCanceled()).isTrue(),
                (i) -> Assertions.assertThat(i.canceledAt()).isNotNull()
        );
    }


    @Test
    void givenFilledOrder_whenPlacedCancel_shouldAllow() {
        Order order = OrderTestDataBuilder.anOrder().orderStatus(OrderStatus.PLACED).build();

        order.cancel();

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.status()).isEqualTo(OrderStatus.CANCELED),
                (i) -> Assertions.assertThat(i.isCanceled()).isTrue(),
                (i) -> Assertions.assertThat(i.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenFilledOrder_whenPaidCancel_shouldAllow() {
        Order order = OrderTestDataBuilder.anOrder().orderStatus(OrderStatus.PAID).build();

        order.cancel();

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.status()).isEqualTo(OrderStatus.CANCELED),
                (i) -> Assertions.assertThat(i.isCanceled()).isTrue(),
                (i) -> Assertions.assertThat(i.canceledAt()).isNotNull()
        );
    }

    @Test
    void givenFilledOrder_whenReadyCancel_shouldAllow() {
        Order order = OrderTestDataBuilder.anOrder().orderStatus(OrderStatus.READY).build();

        order.cancel();

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.status()).isEqualTo(OrderStatus.CANCELED),
                (i) -> Assertions.assertThat(i.isCanceled()).isTrue(),
                (i) -> Assertions.assertThat(i.canceledAt()).isNotNull()
        );
    }


    @Test
    void givenCanceledOrder_whenCancelAgain_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().orderStatus(OrderStatus.CANCELED).build();


        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::cancel);

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.status()).isEqualTo(OrderStatus.CANCELED),
                (i) -> Assertions.assertThat(i.isCanceled()).isTrue(),
                (i) -> Assertions.assertThat(i.canceledAt()).isNotNull()
        );
    }

}
