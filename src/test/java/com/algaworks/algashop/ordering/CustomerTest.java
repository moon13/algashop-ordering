package com.algaworks.algashop.ordering;

import com.algaworks.algashop.ordering.domain.entity.Customer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerTest {

    @Test
    public void testingCustomer(){
        Customer customer = new Customer(
               UUID.randomUUID(),
               "478-256-2504",
               "teste",
                "John.doe@email.com",
                "John Doe",
                LocalDate.of(1991,7,5),
                true,
                OffsetDateTime.now());

              customer.addLoyaltyPoints(10);


    }
}
