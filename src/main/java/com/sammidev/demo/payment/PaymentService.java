package com.sammidev.demo.payment;

import com.sammidev.demo.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    private static final List<Currency> ACCEPTED_CURRENCIES = List.of(
         Currency.USD, Currency.RP);

    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;
    private CardPaymentCharger cardPaymentCharger;

    @Autowired
    public PaymentService(CustomerRepository customerRepository,
                          PaymentRepository paymentRepository,
                          CardPaymentCharger cardPaymentCharger) {
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    void chargeCard(UUID customerId,PaymentRequest paymentRequest) {
        // cek ada atau tidak
        boolean isCustomerFound = customerRepository.findById(customerId).isPresent();
        if (!isCustomerFound) {
            throw new IllegalStateException(String.format("customer dengan id [%s] tidak ditemukan ", customerId));
        }

        // apakah support mata uang yg kita set
        boolean isCurrencySupport = ACCEPTED_CURRENCIES.contains(paymentRequest.getPayment().getCurrency());
        if (!isCurrencySupport) {
            String message = String.format("Currency [%s] not supported", paymentRequest.getPayment().getCurrency());
            throw new IllegalStateException(message);
        }

        // charge card
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
            paymentRequest.getPayment().getSource(),
            paymentRequest.getPayment().getAmount(),
            paymentRequest.getPayment().getCurrency(),
            paymentRequest.getPayment().getDescription()
        );

        //
        if (!cardPaymentCharge.isCardDebited()) {
            throw new IllegalStateException(String.format("Card not debited for customer %s", customerId));
        }

        // 5. insert deh payment nya
        paymentRequest.getPayment().setCustomerId(customerId);
        paymentRepository.save(paymentRequest.getPayment());

    }
}