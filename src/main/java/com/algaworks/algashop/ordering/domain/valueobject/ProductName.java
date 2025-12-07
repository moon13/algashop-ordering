package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages;
import com.algaworks.algashop.ordering.domain.validator.FieldValidations;

public record ProductName(String value) {

    public ProductName(String value) {
        FieldValidations.requiresNonBlank(value,
                ErrorMessages.VALIDATION_ERROR_PRODUCT_NAME_IS_BLANK_OR_NULL);

        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}