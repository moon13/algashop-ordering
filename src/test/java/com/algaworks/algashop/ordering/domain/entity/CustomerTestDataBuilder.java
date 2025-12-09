package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTestDataBuilder {

    private CustomerTestDataBuilder(){

    }

    public static Customer.BrandNewCustomerBuild brandNewCustomer(){

        return Customer.brandNew().document(new Document("255-08-0578"))
                .phone(new Phone("478-256-2504"))
                .email(new Email("john.doe@gmail.com"))
                .fullNAme(new FullName("John","Doe"))
                .birthdate(new Birthdate(LocalDate.of(1991, 7, 5)))
                .promotionNotificationAllowed(false)
                // OffsetDateTime.now(),
                .address(Address.builder()
                        .street("Borboun street")
                        .number("1134")
                        .neightborhood("North Vile")
                        .city("York")
                        .state("South Carlifornia")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apto. 114")
                        .build());
    }

    public static Customer.ExistingCustomerBuild existingCustomer(){
        return  Customer.existing()
                .id(new CustomerId())
                .registeredAt(OffsetDateTime.now())
                .promotionNotificationAllowed(true)
                .archived(false)
                .archivedAt(null)
                .loyaltyPointd(new LoyaltyPoints(0))
                .phone(new Phone("478-256-2504"))
                .email(new Email("john.doe@gmail.com"))
                .fullNAme(new FullName("John","Doe"))
                .birthdate(new Birthdate(LocalDate.of(1991, 7, 5)))
               // .promotionNotificationAllowed(false)
                // OffsetDateTime.now(),
                .address(Address.builder()
                        .street("Borboun street")
                        .number("1134")
                        .neightborhood("North Vile")
                        .city("York")
                        .state("South Carlifornia")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apto. 114")
                        .build());

    }

    public static Customer.ExistingCustomerBuild existingAnonymizedCustomer(){
        return  Customer.existing()
                .id(new CustomerId())
                .fullNAme(new FullName("Anonymous", "Anonymous"))
                .birthdate(null)
                .email(new Email("anonymous@anonymous.com"))
                .phone(new Phone("000-000-0000"))
                .document(new Document("000-00-0000"))
                .promotionNotificationAllowed(false)
                .archived(true)
                .registeredAt(OffsetDateTime.now())
                .archivedAt(OffsetDateTime.now())
                .loyaltyPointd(new LoyaltyPoints(10))
                .address(Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neightborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apt. 114")
                        .build());
    }
}
