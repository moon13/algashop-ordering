package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages;

import java.time.LocalDate;
import java.util.IllformedLocaleException;
import java.util.Objects;
import java.time.Period;

public record Birthdate(LocalDate value) {


    public Birthdate(LocalDate value) {
        Objects.requireNonNull(value);
        if (value.isAfter(LocalDate.now())){
            throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
        }
        this.value = value;
    }

    public Integer age(){
        LocalDate today = LocalDate.now();

        int age = Period.between(this.value(), today).getYears();

        return age;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
