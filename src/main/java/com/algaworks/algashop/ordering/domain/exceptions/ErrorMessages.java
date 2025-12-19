package com.algaworks.algashop.ordering.domain.exceptions;

public class ErrorMessages {

    public static final String VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST = "BirthDate must be a past date";

    public static final String VALIDATION_ERROR_FULLNAME_IS_NULL = "FullName cannot be null";
    public static final String VALIDATION_ERROR_FULLNAME_IS_BLANK = "FullName cannot be blank";

    public static final String VALIDATION_ERROR_EMAIL_IS_INVALID = "Email is invalid";

    public static final String ERROR_CUSTOMER_ARCHIVED = "Customer is archived cannot be changed";

    public static final String VALIDATION_ERROR_MONEY_IS_NULL = "Money cannot be null";
    public static final String VALIDATION_ERROR_MONEY_NEGATIVE = "Money must be 0 or higher";
    public static final String VALIDATION_ERROR_MONEY_QUANTITY_INVALID = "Quantity must be 1 or higher";

    public static final String VALIDATION_ERROR_QUANTITY_IS_NULL = "Quantity cannot be null";
    public static final String VALIDATION_ERROR_QUANTITY_NEGATIVE = "Quantity must be 0 or higher";

    public static final String VALIDATION_ERROR_PRODUCT_NAME_IS_BLANK_OR_NULL = "Product Name cannot be blank or null";
    public static final String VALIDATION_ERROR_DOCUMENT_IS_BLANK_OR_NULL = "Document cannot be blank or null";
    public static final String VALIDATION_ERROR_PHONE_IS_BLANK_OR_NULL = "Phone cannot be blank or null";
    public static final String VALIDATION_ERROR_ADDRESS_IS_NULL = "ZipCode cannot be null";

    public static final String ERROR_ORDER_STATUS_CANNOT_BE_CHANGED = "Cannot change order %s status from %s to %s";


}
