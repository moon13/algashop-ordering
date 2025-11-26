package com.algaworks.algashop.ordering;

import com.algaworks.algashop.ordering.domain.entity.Customer;
import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public class CustomerTest {

    @Test
    public void testingCustomer(){
        Customer customer = new Customer(
                new CustomerId(),
               new Document("478-256-2504"),
               new Phone("teste"),
                new Email("John.doe@email.com"),
                new FullName("John","Doe"),
                new Birthdate(LocalDate.of(1991,7,5)),
                true,
                OffsetDateTime.now());

        System.out.println(customer.id());
        System.out.println(IdGenerator.generateTimeBasedUUID());


              customer.addLoyaltyPoints(new LoyaltyPoints(10));


    }
}
