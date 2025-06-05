package com.bkr.shopen.services;


import org.springframework.stereotype.Component;

@Component
public interface PaymentService {
    void processPayment(String orderId, double amount);
}
