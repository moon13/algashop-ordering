package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages;

import java.util.Objects;

public record Quantity(Integer value) implements Comparable<Quantity> {

    public static final Quantity ZERO = new Quantity(0);

    public Quantity (Integer value){
        Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_QUANTITY_IS_NULL);

        if (value < 0 ) {
            throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_QUANTITY_NEGATIVE);
        }

        this.value = value;
    }

    public Quantity add(Quantity other) {
        Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_QUANTITY_IS_NULL);
        return new Quantity(this.value + other.value);
    }

    @Override
    public int compareTo(Quantity other) {
        Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_QUANTITY_IS_NULL);
        return this.value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
