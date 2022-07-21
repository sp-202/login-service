package com.example.registration_service_flow.event.listener;

import com.example.registration_service_flow.email_send.VerificationEmailSend;
import com.example.registration_service_flow.event.RegistrationCompleteEvent;
import com.example.registration_service_flow.user_register.service.UserService;
import com.example.registration_service_flow.user_register.user_entity.UserDbAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationEmailSend sendEmail;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //Create the verification token
        UserDbAttributes user = event.getUser();
        String token = UUID.randomUUID().toString();

        //save toke to toke_db
        userService.saveVerificationToken(token, user.getUsername(),
                user.getEmail_id(), user.getUuid());
        //Send mail to the user
        String url = event.getApplicationURL()
                + "/v1/verifyregistration?token="
                + token;
        //Send Verification Email
        sendEmail.sendEmail(user.getEmail_id(), "Please verify your email", url);
        log.info("Click the link to verify your account: {}", url);
    }
}
