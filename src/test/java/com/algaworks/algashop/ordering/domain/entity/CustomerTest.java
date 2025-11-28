package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.utility.IdGenerator;

import com.algaworks.algashop.ordering.domain.exceptions.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

class CustomerTest {

    @Test
    void given_invalidEmail__whenTryCreateCustomer_shouldGenerateException(){

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> {
                    Customer.brandNew()
                            .document(new Document("255-08-0578"))
                            .phone(new Phone("478-256-2504"))
                            .email(new Email("invalid"))
                                    .fullNAme(new FullName("John","Doe"))
                                    .birthdate(new Birthdate(LocalDate.of(1991, 7, 5)))
                            .promotionNotificationAllowed(false)
                            //OffsetDateTime.now(),
                            .address(Address.builder()
                                    .street("Borboun street")
                                    .number("1134")
                                    .neightborhood("North Vile")
                                    .city("York")
                                    .state("South Carlifornia")
                                    .zipCode(new ZipCode("12345"))
                                    .complement("Apto. 114")
                                    .build())
                            .build();
                });

    }



    @Test
    void given_invalidEmail__whenTryUpdateCustomerEmail_shouldGenerateException(){

        Customer customer = Customer.brandNew()
                .document(new Document("255-08-0578"))
                 .phone(new Phone("478-256-2504"))
                .email(new Email("john.doe@gmail.com"))
                .fullNAme(new FullName("John","Doe"))
                .birthdate(new Birthdate(LocalDate.of(1991, 7, 5)))
                .promotionNotificationAllowed(false)
                //OffsetDateTime.now(),
                .address( Address.builder()
                .street("Borboun street")
                .number("1134")
                .neightborhood("North Vile")
                .city("York")
                .state("South Carlifornia")
                .zipCode(new ZipCode("12345"))
                .complement("Apto. 114")
                .build()).build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> {
                        customer.changeEmail(new Email("invalid"));
                });

    }

    @Test
    public void given_unarchivedCustomer_whenArchive_shouldAnonymize(){
        Customer customer = Customer.brandNew()
                .document(new Document("255-08-0578"))
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
                        .build()).build();


         customer.archive();

         Assertions.assertWith(customer,
                 c->Assertions.assertThat(c.fullNAme()).isEqualTo(new FullName("Anonymous","Anonymous")),
                 c->Assertions.assertThat(c.email()).isNotEqualTo(new Email("john.doe@gmail.com")),
                 c->Assertions.assertThat(c.phone()).isEqualTo(new Phone("000-000-0000")),
                 c->Assertions.assertThat(c.document()).isEqualTo(new Document("000-00-0000")),
                 c->Assertions.assertThat(c.birthdate()).isNull(),
                 c->Assertions.assertThat(c.isPromotionNotificationAllowed()).isFalse(),
                 c->Assertions.assertThat(c.address()).isEqualTo(
                         Address.builder()
                                 .street("Borboun street")
                                 .number("Anonymized")
                                 .neightborhood("North Vile")
                                 .city("York")
                                 .state("South Carlifornia")
                                 .zipCode(new ZipCode("12345"))
                                 .complement(null)
                                 .build()

                 )
                 );
    }

    @Test
    public void given_archivedCustomer_whenTryToUpdate_shouldGenerateException(){
        Customer customer = Customer.existing()
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
                        .build())
                .build();


                /*Customer.existing(
                new CustomerId(),
                new FullName("Anonymous","Anonymous"),
                null,
                new Email("anonymous@anonymous.com"),
                new Phone("000-000-0000"),
                new Document("000-00-0000"),
                false,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                new LoyaltyPoints(10),
                Address.builder()
                        .street("Borboun street")
                        .number("1134")
                        .neightborhood("North Vile")
                        .city("York")
                        .state("South Carlifornia")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apto. 114")
                        .build()

                );*/

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.archive());

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail(new Email("email@email.com")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone(new Phone("123-123-1111")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.enablePromotionNotifications());

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.disablePromotionNotifications());




    }


    @Test
    public void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints(){
        Customer customer =  Customer.brandNew()
                .document(new Document("255-08-0578"))
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
                        .build()).build();



        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(20));

        Assertions.assertThat(customer.loyaltyPointd()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    public void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException(){
        Customer customer = Customer.brandNew()
                .document(new Document("255-08-0578"))
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
                        .build()).build();


           Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                   .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(0)));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));
    }


}