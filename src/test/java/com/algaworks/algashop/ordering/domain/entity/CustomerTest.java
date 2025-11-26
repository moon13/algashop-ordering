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
                    new Customer(
                            new CustomerId(),
                            new Document("255-08-0578"),
                            new Phone("478-256-2504"),
                            new Email("invalid"),
                            new FullName("John","Doe"),
                            new Birthdate(LocalDate.of(1991, 7, 5)),
                            false,
                            OffsetDateTime.now());
                });

    }



    @Test
    void given_invalidEmail__whenTryUpdateCustomerEmail_shouldGenerateException(){

        Customer customer = new Customer(
                new CustomerId(),
                new Document("255-08-0578"),
                new Phone("478-256-2504"),
                new Email("john.doe@gmail.com"),
                new FullName("John","Doe"),
                new Birthdate(LocalDate.of(1991, 7, 5)),
                false,
                OffsetDateTime.now());

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> {
                        customer.changeEmail(new Email("invalid"));
                });

    }

    @Test
    public void given_unarchivedCustomer_whenArchive_shouldAnonymize(){
        Customer customer = new Customer(
                new CustomerId(),
                new Document("255-08-0578"),
                new Phone("478-256-2504"),
                new Email("john.doe@gmail.com"),
                new FullName("John","Doe"),
                new Birthdate(LocalDate.of(1991, 7, 5)),
                false,
                OffsetDateTime.now());


         customer.archive();

         Assertions.assertWith(customer,
                 c->Assertions.assertThat(c.fullNAme()).isEqualTo(new FullName("Anonymous","Anonymous")),
                 c->Assertions.assertThat(c.email()).isNotEqualTo(new Email("john.doe@gmail.com")),
                 c->Assertions.assertThat(c.phone()).isEqualTo(new Phone("000-000-0000")),
                 c->Assertions.assertThat(c.document()).isEqualTo(new Document("000-00-0000")),
                 c->Assertions.assertThat(c.birthdate()).isNull(),
                 c->Assertions.assertThat(c.isPromotionNotificationAllowed()).isFalse()
                 );
    }

    @Test
    public void given_archivedCustomer_whenTryToUpdate_shouldGenerateException(){
        Customer customer = new Customer(
                new CustomerId(),
                new FullName("Anonymous","Anonymous"),
                new Birthdate(null),
                new Email("anonymous@anonymous.com"),
                new Phone("000-000-0000"),
                new Document("000-00-0000"),
                false,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                new LoyaltyPoints(10)
                );

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
        Customer customer = new Customer(
                new CustomerId(),
                new Document("255-08-0578"),
                new Phone("478-256-2504"),
                new Email("john.doe@gmail.com"),
                new FullName("John","Doe"),
                new Birthdate(LocalDate.of(1991, 7, 5)),
                false,
                OffsetDateTime.now());


        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(20));

        Assertions.assertThat(customer.loyaltyPointd()).isEqualTo(new LoyaltyPoints(30));
    }

    @Test
    public void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException(){
        Customer customer = new Customer(
                new CustomerId(),
                new Document("255-08-0578"),
                new Phone("478-256-2504"),
                new Email("john.doe@gmail.com"),
                new FullName("John","Doe"),
                new Birthdate(LocalDate.of(1991, 7, 5)),
                false,
                OffsetDateTime.now());


           Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                   .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(0)));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(new LoyaltyPoints(-10)));
    }


}