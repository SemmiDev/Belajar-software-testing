package com.sammidev.demo.customer;

import com.sammidev.demo.utils.PhoneNumberValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

public class CustomerRegistrationServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PhoneNumberValidator phoneNumberValidator;
    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;
    private CustomerRegistrationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository, phoneNumberValidator);
    }

    @Test
    void iniAkanMenyimpanCustomerBaru() {
        // given a phone numbernya bro
        String phoneNumber = "+6234";
        Customer customer = new Customer(UUID.randomUUID(), "Sammidev", phoneNumber);
        // request dulu yekan
        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(customer);
        // tidak ada customer dengan no telp
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.empty());
        // no hp nya valid dung
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);
        // ketika kita registerin
        underTest.registerNewCustomer(registrationRequest);
        // makaa complete :)
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualTo(customer);
    }

    @Test
    void customerTidakAkanDisimpanKarenaNomorTelponYgTidakValid() {
        String phoneNumber = "000099";
        Customer customer = new Customer(UUID.randomUUID(), "ayatullah", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
        given(phoneNumberValidator.test(phoneNumber)).willReturn(false);
        // When
        assertThatThrownBy(() -> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Phone Number " + phoneNumber + " is not valid");

        // Then
        then(customerRepository).shouldHaveNoInteractions();
    }

    @Test
    void ketikaIdNullMakaAkanTetapDisimpan() {
        String phoneNumber = "+6282";
        Customer customer = new Customer(null, "sammidev",phoneNumber);

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(customer);
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);

        underTest.registerNewCustomer(customerRegistrationRequest);
        then(customerRepository).should().save(customerArgumentCaptor.capture());

        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualToIgnoringGivenFields(customer, "id");
        assertThat(customerArgumentCaptorValue.getId()).isNotNull();
    }

    @Test
    void customersTidakAkanDiSimpanKetikaCustomernyaAdaYgSama() {
        String phoneNumber = "+6234";
        Customer customer = new Customer(UUID.randomUUID(), "Sammidev", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer));
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);
        underTest.registerNewCustomer(request);
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void akanDiThrowKetikaNomorTelpSudahAdaAtauAdaYgSama() {
        String phoneNumber = "+6234";
        Customer customer = new Customer(UUID.randomUUID(), "Sammidev", phoneNumber);
        Customer customer2 = new Customer(UUID.randomUUID(), "Sammidev2", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
        given(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .willReturn(Optional.of(customer2));
        given(phoneNumberValidator.test(phoneNumber)).willReturn(true);
        assertThatThrownBy(() -> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("phone number [%s] is taken", phoneNumber));

        // Finally
        then(customerRepository).should(never()).save(any(Customer.class));
    }
}