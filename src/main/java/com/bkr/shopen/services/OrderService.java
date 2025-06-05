package com.bkr.shopen.services;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public boolean placeOrder(String productId, int quantity) {

    paymentService.processPayment("100", 200);


        return false;
    }

}
