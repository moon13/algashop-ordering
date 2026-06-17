package com.algaworks.algashop.ordering;

import com.algaworks.algashop.ordering.domain.model.commons.*;
import com.algaworks.algashop.ordering.domain.model.customer.BirthDate;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.IdGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class CustomerTest {




    @Test
    public void testingCustomer(){
        Customer customer = Customer.brandNew()
               .document(new Document("255-08-0578"))
                .phone(new Phone("478-256-2504"))
                .email(new Email("john.doe@gmail.com"))
                .fullName(new FullName("John","Doe"))
                .birthDate(new BirthDate(LocalDate.of(1991, 7, 5)))
                .promotionNotificationsAllowed(true)
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
