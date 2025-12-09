package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    private static final RoundingMode roundingMode = RoundingMode.HALF_EVEN;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money(BigDecimal value) {
        Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_MONEY_IS_NULL);
        this.value = value.setScale(2, roundingMode);

        if (this.value.signum() == -1) {
            throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_MONEY_NEGATIVE);
        }
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity, ErrorMessages.VALIDATION_ERROR_QUANTITY_IS_NULL);
        if (quantity.value() < 1) {
            throw new IllegalArgumentException(ErrorMessages.VALIDATION_ERROR_MONEY_QUANTITY_INVALID);
        }
        BigDecimal multiplied = this.value.multiply(new BigDecimal(quantity.value()));
        return new Money(multiplied);
    }

    public Money add(Money other) {
        Objects.requireNonNull(other, ErrorMessages.VALIDATION_ERROR_MONEY_IS_NULL);
        Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_MONEY_IS_NULL);
        return new Money(this.value.add(other.value));
    }

    public Money divide(Money other) {
        Objects.requireNonNull(other, ErrorMessages.VALIDATION_ERROR_MONEY_IS_NULL);
        Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_MONEY_IS_NULL);
        return new Money(this.value.divide(other.value, roundingMode));
    }

    @Override
    public int compareTo(Money o) {
        Objects.requireNonNull(o, ErrorMessages.VALIDATION_ERROR_MONEY_IS_NULL);
        Objects.requireNonNull(value, ErrorMessages.VALIDATION_ERROR_MONEY_IS_NULL);
        return this.value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
