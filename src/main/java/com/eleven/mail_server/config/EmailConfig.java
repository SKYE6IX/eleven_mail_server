package com.eleven.mail_server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Autowired
    private Environment env;

    @Value("${email.server.port}")
    private int PORT;

    @Bean
    public JavaMailSender mailSender(){

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("email.server.host"));
        mailSender.setPort(PORT);

        mailSender.setUsername(env.getProperty("email.server.username"));
        mailSender.setPassword(env.getProperty("email.server.password"));

        Properties prop = mailSender.getJavaMailProperties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
