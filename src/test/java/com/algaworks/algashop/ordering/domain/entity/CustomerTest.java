package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.utility.IdGenerator;

import com.algaworks.algashop.ordering.exceptions.CustomerArchivedException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void given_invalidEmail__whenTryCreateCustomer_shouldGenerateException(){

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> {
                    new Customer(
                            IdGenerator.generateTimeBasedUUID(),
                            "255-08-0578",
                            "478-256-2504",
                            "invalid",
                            "John Doe",
                            LocalDate.of(1991, 7, 5),
                            false,
                            OffsetDateTime.now());
                });

    }



    @Test
    void given_invalidEmail__whenTryUpdateCustomerEmail_shouldGenerateException(){

        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "255-08-0578",
                "478-256-2504",
                "john.doe@gmail.com",
                "John Doe",
                LocalDate.of(1991, 7, 5),
                false,
                OffsetDateTime.now());

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(
                () -> {
                        customer.changeEmail("invalid");
                });

    }

    @Test
    public void given_unarchivedCustomer_whenArchive_shouldAnonymize(){
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "255-08-0578",
                "478-256-2504",
                "john.doe@gmail.com",
                "John Doe",
                LocalDate.of(1991, 7, 5),
                false,
                OffsetDateTime.now());


         customer.archive();

         Assertions.assertWith(customer,
                 c->Assertions.assertThat(c.fullNAme()).isEqualTo("Anonymous"),
                 c->Assertions.assertThat(c.email()).isNotEqualTo("john.doe@gmail.com"),
                 c->Assertions.assertThat(c.phone()).isEqualTo("000-000-0000"),
                 c->Assertions.assertThat(c.document()).isEqualTo("000-00-0000"),
                 c->Assertions.assertThat(c.birthdate()).isNull(),
                 c->Assertions.assertThat(c.isPromotionNotificationAllowed()).isFalse()
                 );
    }

    @Test
    public void given_archivedCustomer_whenTryToUpdate_shouldGenerateException(){
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "Anonymous",
                null,
                "anonymous@anonymous.com",
                "000-000-0000",
                "000-00-0000",
                false,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                10
                );

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.archive());

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changeEmail("email@email.com"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.changePhone("123-123-1111"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.enablePromotionNotifications());

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customer.disablePromotionNotifications());




    }


    @Test
    public void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints(){
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "255-08-0578",
                "478-256-2504",
                "john.doe@gmail.com",
                "John Doe",
                LocalDate.of(1991, 7, 5),
                false,
                OffsetDateTime.now());


        customer.addLoyaltyPoints(10);
        customer.addLoyaltyPoints(20);

        Assertions.assertThat(customer.loyaltyPointd()).isEqualTo(30);
    }

    @Test
    public void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException(){
        Customer customer = new Customer(
                IdGenerator.generateTimeBasedUUID(),
                "255-08-0578",
                "478-256-2504",
                "john.doe@gmail.com",
                "John Doe",
                LocalDate.of(1991, 7, 5),
                false,
                OffsetDateTime.now());


           Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                   .isThrownBy(() -> customer.addLoyaltyPoints(0));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> customer.addLoyaltyPoints(-10));
    }


}