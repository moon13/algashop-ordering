package com.algaworks.algashop.ordering.infrastructure.notification.customer;

import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerNotificationServiceFakeImpl implements CustomerNotificationService {




    @Override
    public void notifyNewRegistration(NotifyNewRegistrationInput input) {


        log.info("Welcome {}", input.firstName());
        log.info("Use your email to access your account {}", input.email());

    }
}
