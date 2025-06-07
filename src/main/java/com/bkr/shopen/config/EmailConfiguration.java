package com.bkr.shopen.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfiguration {

    @Value("${spring.mail.username}")
    private String emailUsername;

    @Value("${spring.mail.host}")
    private String emailHost;

    @Value("${spring.mail.port}")
    private int emailPort;

    @Value("${spring.mail.password}")
    private String emailPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean emailAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean emailStartTlsEnable;

    @Value("${spring.mail.properties.mail.debug}")
    private boolean emailDebug;

    @Value("${spring.mail.protocol}")
    private String emailTransportProtocol;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(emailHost);
        mailSender.setPort(emailPort);
        mailSender.setUsername(emailUsername);
        mailSender.setPassword(emailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", emailTransportProtocol);
        props.put("mail.smtp.auth", emailAuth);
        props.put("mail.smtp.starttls.enable", emailStartTlsEnable);
        props.put("mail.debug", emailDebug);

        return mailSender;
    }
}