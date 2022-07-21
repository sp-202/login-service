package com.example.registration_service_flow.email_send;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class VerificationEmailSend {

    @Bean
    private JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("domain.contact279@gmail.com");
        mailSender.setPassword("oupficijprpovuvr");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }

    public String sendEmail(String email_to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("domain.contact279@gmail.com");
        message.setTo(email_to);
        message.setSubject(subject);
        message.setText(body);
        getJavaMailSender().send(message);
        return "Email send";
    }
}
