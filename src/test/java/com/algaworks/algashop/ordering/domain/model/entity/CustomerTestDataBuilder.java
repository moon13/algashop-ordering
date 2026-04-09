package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTestDataBuilder {

     public static final CustomerId DEFAULT_CUSTOMER_ID = new CustomerId();

    private CustomerTestDataBuilder(){

    }

    public static Customer.BrandNewCustomerBuild brandNewCustomer(){

        return Customer.brandNew().document(new Document("255-08-0578"))
                .phone(new Phone("478-256-2504"))
                .email(new Email("john.doe@gmail.com"))
                .fullName(new FullName("John","Doe"))
                .birthDate(new Birthdate(LocalDate.of(1991, 7, 5)))
                .promotionNotificationsAllowed(false)
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
                .id(DEFAULT_CUSTOMER_ID)
                .registeredAt(OffsetDateTime.now())
                .promotionNotificationsAllowed(true)
                .archived(false)
                .archivedAt(null)
                .loyaltyPoints(new LoyaltyPoints(0))
                .phone(new Phone("478-256-2504"))
                .email(new Email("john.doe@gmail.com"))
                .fullName(new FullName("John","Doe"))
                .birthDate(new Birthdate(LocalDate.of(1991, 7, 5)))
                .document(new Document("111-111-111"))
                //.promotionNotificationsAllowed(false)
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
                .fullName(new FullName("Anonymous", "Anonymous"))
                .birthDate(null)
                .email(new Email("anonymous@anonymous.com"))
                .phone(new Phone("000-000-0000"))
                .document(new Document("000-00-0000"))
                .promotionNotificationsAllowed(false)
                .archived(true)
                .registeredAt(OffsetDateTime.now())
                .archivedAt(OffsetDateTime.now())
                .loyaltyPoints(new LoyaltyPoints(10))
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
