package com.bkr.shopen.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Value("${spring.application.name}")
    private String  AppName;
    @RequestMapping("/")
    public <string> String index() {
        System.out.println("Welcome to " + AppName);
        return "index.html";
    }
}
