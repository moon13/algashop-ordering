package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.utility.IdGenerator;

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
                 c->Assertions.assertThat(c.birthdate()).isNull()
                 );
    }

}