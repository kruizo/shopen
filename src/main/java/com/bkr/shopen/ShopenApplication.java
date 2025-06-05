package com.bkr.shopen;

import com.bkr.shopen.services.OrderService;
import com.bkr.shopen.services.StripePaymentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ShopenApplication {

	public static void main(String[] args) {
//		OrderService orderService = new OrderService(new StripePaymentService());
//		System.out.println(orderService.placeOrder("21", 100));



		ApplicationContext context = SpringApplication.run(ShopenApplication.class, args);
		var orderService = context.getBean(OrderService.class);


	}
}
