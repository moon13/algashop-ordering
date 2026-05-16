package com.algaworks.algashop.ordering.application.service;

import com.algaworks.algashop.ordering.application.model.CustomerInput;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerRegistrationService;
import com.algaworks.algashop.ordering.domain.model.customer.Customers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerManagementApplicationService {
    private final CustomerRegistrationService customerRegistrationService;
    private final Customers customers;

    //PAREI NO TEMPO 6:22
    @Transactional
    public UUID create(CustomerInput input) {
        Objects.requireNonNull(input);

    }

}
