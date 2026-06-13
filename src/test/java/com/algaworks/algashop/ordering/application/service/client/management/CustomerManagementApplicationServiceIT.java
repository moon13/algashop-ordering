package com.algaworks.algashop.ordering.application.service.client.management;


import com.algaworks.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.application.customer.management.CustomerInput;
import com.algaworks.algashop.ordering.application.customer.management.CustomerOutput;
import com.algaworks.algashop.ordering.application.customer.management.CustomerUpdateInput;
import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.algaworks.algashop.ordering.domain.model.customer.*;
import com.algaworks.algashop.ordering.infrastructure.listener.customer.CustomerEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@Transactional
class CustomerManagementApplicationServiceIT {


    @Autowired
     private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoSpyBean
    private CustomerNotificationService customerNotificationService;

    @Test
    public void shouldRegister() {
        CustomerInput input =CustomerInputTestDataBuilder.aCustomer().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat( customerOutput).extracting(
                CustomerOutput::getId,
                CustomerOutput::getFirstName,
                CustomerOutput::getLastName,
                CustomerOutput::getEmail,
                CustomerOutput::getBirthDate
        ).containsExactly(
                customerId,
                "John",
                "Doe",
                "johndoe@email.com",
                LocalDate.of(1991, 7,5)


        );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();

        Mockito.verify(customerEventListener)
                .listen(Mockito.any(CustomerRegisteredEvent.class));

        Mockito.verify(customerEventListener, Mockito.never())
                .listen(Mockito.any(CustomerArchivedEvent.class));

        Mockito.verify(customerNotificationService).notifyNewRegistration(Mockito.any(UUID.class));
    }


    @Test
    public void shouldUpdate() {
        CustomerInput input =CustomerInputTestDataBuilder.aCustomer().build();
        CustomerUpdateInput updateInput = CustomerUpdateInputTestDataBuilder.aCustomerUpdate().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();


        customerManagementApplicationService.update(customerId, updateInput);

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat( customerOutput).extracting(
                CustomerOutput::getId,
                CustomerOutput::getFirstName,
                CustomerOutput::getLastName,
                CustomerOutput::getEmail,
                CustomerOutput::getBirthDate
        ).containsExactly(
                customerId,
                "Matt",
                "Damon",
                "johndoe@email.com",
                LocalDate.of(1991, 7,5)


        );

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }

    @Test
    public void shouldArchive() {
        CustomerInput input =CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);

        CustomerOutput archivedCustomer = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(archivedCustomer)
                .isNotNull()
                .extracting(
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getPhone,
                        CustomerOutput::getDocument,
                        CustomerOutput::getBirthDate,
                        CustomerOutput::getPromotionNotificationsAllowed
                ).containsExactly(
                        "Anonymous",
                        "Anonymous",
                        "000-000-0000",
                        "000-00-0000",
                        null,
                        false
                );

        Assertions.assertThat(archivedCustomer.getEmail()).endsWith("@anonymous.com");
        Assertions.assertThat(archivedCustomer.getArchived()).isTrue();
        Assertions.assertThat(archivedCustomer.getArchivedAt()).isNotNull();

        Assertions.assertThat(archivedCustomer.getAddress()).isNotNull();
        Assertions.assertThat(archivedCustomer.getAddress().getNumber()).isNotNull().isEqualTo("Anonymized");
        Assertions.assertThat(archivedCustomer.getAddress().getComplement()).isNull();

    }

    @Test
    public void shouldThrowCustomerNotFoundExceptionWhenArchivingNonExistingCustomer() {
        UUID nonExistingId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                .isThrownBy(() -> customerManagementApplicationService.archive(nonExistingId));
    }

    @Test
    public void shouldThrowCustomerArchivedExceptionWhenArchivingAlreadyArchivedCustomer() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.archive(customerId);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(() -> customerManagementApplicationService.archive(customerId));
    }

    @Test
     public void shouldChangeEmail(){
         CustomerInput input = CustomerInputTestDataBuilder.aCustomer()
                 .email("validemail@email.com").build();
         UUID customerId = customerManagementApplicationService.create(input);
         Assertions.assertThat(customerId).isNotNull();
         Assertions.assertThat(input.getEmail()).isNotNull();

         CustomerOutput savedCustomer = customerManagementApplicationService.findById(customerId);
        Assertions.assertThat(savedCustomer).isNotNull();
        Assertions.assertThat(savedCustomer.getEmail()).isNotNull();

         customerManagementApplicationService.changeEmail(customerId,"newEmail@example.com");

         CustomerOutput alteredEmailCustomer = customerManagementApplicationService.findById(customerId);


          Assertions.assertThat(input.getEmail()).isNotEqualTo(alteredEmailCustomer.getEmail());
     }

     @Test
     public void shouldThrowExceptionWhenGivenInvalidCustomerId(){
         UUID nonExistingId = UUID.randomUUID();

         Assertions.assertThatExceptionOfType(CustomerNotFoundException.class)
                 .isThrownBy(() -> customerManagementApplicationService.changeEmail(nonExistingId, "newEmail@example.com"));


     }

     @Test
    public void shouldThrowExceptionWhenGivenArchivedUserWithValidEmail(){
         CustomerInput input = CustomerInputTestDataBuilder.aCustomer()
                 .email("validemail@email.com").build();
         UUID customerId = customerManagementApplicationService.create(input);
         Assertions.assertThat(customerId).isNotNull();
         Assertions.assertThat(input.getEmail()).isNotNull();

         CustomerOutput savedCustomer = customerManagementApplicationService.findById(customerId);
         Assertions.assertThat(savedCustomer).isNotNull();
         Assertions.assertThat(savedCustomer.getEmail()).isNotNull();

         customerManagementApplicationService.archive(customerId);

         Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                 .isThrownBy(() -> customerManagementApplicationService.changeEmail(customerId, "newEmail@example.com"));

     }

     @Test
     public void shouldThrowExceptionWhenGivenUserWithInvalidEmail(){
         CustomerInput input = CustomerInputTestDataBuilder.aCustomer()
                 .email("validemail@email.com").build();
         UUID customerId = customerManagementApplicationService.create(input);
         Assertions.assertThat(customerId).isNotNull();
         Assertions.assertThat(input.getEmail()).isNotNull();

         CustomerOutput savedCustomer = customerManagementApplicationService.findById(customerId);
         Assertions.assertThat(savedCustomer).isNotNull();
         Assertions.assertThat(savedCustomer.getEmail()).isNotNull();

         Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                 .isThrownBy(() -> customerManagementApplicationService.changeEmail(customerId, "invalidEmail@example"));

     }

    @Test
    public void shouldThrowExceptionWhenChangeEmailUsingNotUniqueEmail(){
        CustomerInput input1 = CustomerInputTestDataBuilder.aCustomer()
                .firstName("John").lastName("Doe")
                .email("john@email.com").build();
        UUID customerId1 = customerManagementApplicationService.create(input1);
        Assertions.assertThat(customerId1).isNotNull();
        Assertions.assertThat(input1.getEmail()).isNotNull();

        CustomerInput input2 = CustomerInputTestDataBuilder.aCustomer()
                .firstName("Jane").lastName("Smith")
                .email("jane@email.com").build();

        UUID customerId2 = customerManagementApplicationService.create(input2);
        Assertions.assertThat(customerId2).isNotNull();
        Assertions.assertThat(input2.getEmail()).isNotNull();

        Assertions.assertThatExceptionOfType(CustomerEmailIsInUseException.class)
                .isThrownBy(() ->  customerManagementApplicationService.changeEmail(customerId2,"john@email.com"));
    }

}