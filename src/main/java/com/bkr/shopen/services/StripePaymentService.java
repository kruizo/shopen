package com.bkr.shopen.services;

import org.springframework.stereotype.Service;

@Service
public class StripePaymentService implements PaymentService {


    @Override
    public void processPayment(String orderId, double amount) {
        System.out.println("Processing payment for order ID: " + orderId + " with amount: " + amount);
    }

    public boolean validatePaymentDetails(String cardNumber, String expiryDate) {
        System.out.println("Validating payment details for card number: " + cardNumber);
        return true;
    }
}
