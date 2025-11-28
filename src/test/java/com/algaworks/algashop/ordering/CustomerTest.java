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
        Customer customer = Customer.brandNew()
               .document(new Document("255-08-0578"))
                .phone(new Phone("478-256-2504"))
                .email(new Email("john.doe@gmail.com"))
                .fullNAme(new FullName("John","Doe"))
                .birthdate(new Birthdate(LocalDate.of(1991, 7, 5)))
                .promotionNotificationAllowed(true)
                //OffsetDateTime.now(),
                .address(Address.builder()
                        .street("Borboun street")
                        .number("1134")
                        .neightborhood("North Vile")
                        .city("York")
                        .state("South Carlifornia")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apto. 114")
                        .build()).build();


        System.out.println(customer.id());
        System.out.println(IdGenerator.generateTimeBasedUUID());


              customer.addLoyaltyPoints(new LoyaltyPoints(10));


    }
}
