package com.example.registration_service_flow.controller;

import com.example.registration_service_flow.email_send.VerificationEmailSend;
import com.example.registration_service_flow.event.RegistrationCompleteEvent;
import com.example.registration_service_flow.token.VerificationToken;
import com.example.registration_service_flow.user_register.service.UserService;
import com.example.registration_service_flow.user_register.user_entity.RegenerateTokenPayLoad;
import com.example.registration_service_flow.user_register.user_entity.UserDbAttributes;
import com.example.registration_service_flow.user_register.user_model.RegisterNewUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/v1")
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private VerificationEmailSend sendEmail;

    @PostMapping("/register")
    public String registerNewUser(@RequestBody @Valid RegisterNewUser newUser,
                                  final HttpServletRequest request) {
        if (userService.isUserNameExists(newUser.getUsername()) &&
                userService.isEmailIdExists(newUser.getEmail_id())) {
            UserDbAttributes user = userService.registerUser(newUser);
            publisher.publishEvent(new RegistrationCompleteEvent(
                    user,
                    applicationURL(request)
            ));
            return "Successfully registered";
        } else if (!userService.isEmailIdExists(newUser.getEmail_id())) {
            return "Email id is already exists !!!";
        } else if (!userService.isUserNameExists(newUser.getUsername())) {
            return "Username is already taken !!!";
        } else {
            return "Some thing wrong!, Please try again later";
        }

    }


    @GetMapping("/verifyregistration")
    public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token) {
        String result = userService.verifyUserEmailAddress(token);
        if (result.equalsIgnoreCase("verified")) {
            return new ResponseEntity<>("Your email-id is successfully verified", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/regenerateverification_token")
    public ResponseEntity<String>
    regenerateVerification_token(@RequestBody @Valid RegenerateTokenPayLoad newUser,
                                 HttpServletRequest request) {
        String user_name = newUser.getUsername();
        String email_id = newUser.getEmail_id();
        log.info("Body: {}", newUser);
        if (!userService.isUserNameExists(user_name) &&
                !userService.isEmailIdExists(email_id) &&
                !userService.isUserVerified(user_name) &&
                userService.isEmaiIdCorrespondsToUsername(user_name, email_id)) {

            VerificationToken newToken = userService.regenerateVerificationToken(user_name, email_id);
            String link = regenerateToken(applicationURL(request), newToken.getToken());
            sendEmail.sendEmail(email_id, "Please verify your email", link);
            return new ResponseEntity<>("Successfully regenerated verification token", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }
    }

    private String regenerateToken(String applicationURL, String token) {
        String url = applicationURL
                + "/v1/regenerateverification_token?token="
                + token;
        log.info("Click the link to verify your account: {}", url);
        return url;
    }


    private String applicationURL(HttpServletRequest request) {
//        log.info("Request uri: {}", request.getRequestURI());
//        log.info("Request url: {}", request.getRequestURL());

        String url = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        log.info("URL: {}", url);

        return url + request.getContextPath();
    }
}
