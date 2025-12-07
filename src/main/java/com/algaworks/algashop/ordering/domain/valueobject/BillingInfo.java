package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.exceptions.ErrorMessages;
import lombok.Builder;

import java.util.Objects;

@Builder
public record BillingInfo(FullName fullName, Document document,
                          Phone phone, Address address) {

    public BillingInfo {
        Objects.requireNonNull(fullName, ErrorMessages.VALIDATION_ERROR_FULLNAME_IS_NULL);
        Objects.requireNonNull(document, ErrorMessages.VALIDATION_ERROR_DOCUMENT_IS_BLANK_OR_NULL);
        Objects.requireNonNull(phone, ErrorMessages.VALIDATION_ERROR_PHONE_IS_BLANK_OR_NULL);
        Objects.requireNonNull(address, ErrorMessages.VALIDATION_ERROR_ADDRESS_IS_NULL);

    }
}