package com.example.registration_service_flow.user_register.service;

import com.example.registration_service_flow.token.VerificationToken;
import com.example.registration_service_flow.token.VerificationTokenRepository;
import com.example.registration_service_flow.token.rate_limit.MaxTokenRegenerateLimit;
import com.example.registration_service_flow.token.rate_limit.MaxTokenRegenerateLimitRepository;
import com.example.registration_service_flow.user_register.user_entity.RegistrationDetails;
import com.example.registration_service_flow.user_register.user_entity.UserDbAttributes;
import com.example.registration_service_flow.user_register.user_entity.UserDetails;
import com.example.registration_service_flow.user_register.user_model.RegisterNewUser;
import com.example.registration_service_flow.user_register.user_repository.UserRegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRegisterRepository registerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private MaxTokenRegenerateLimitRepository regenerateLimitRepository;

    @Override
    public UserDbAttributes registerUser(RegisterNewUser newUser) {

        UserDbAttributes user = new UserDbAttributes();
        UserDetails userDetails = new UserDetails();
        RegistrationDetails registrationDetails = new RegistrationDetails();

        //Set user details
        userDetails.setFirst_name(newUser.getFirst_name());
        userDetails.setLast_name(newUser.getLast_name());

        //Set registration details
        registrationDetails.setActivated(false);
        registrationDetails.setLocked(false);
        registrationDetails.setVerified(false);
        registrationDetails.setTime_stamp(LocalDateTime.now().toString());
        registrationDetails.setIp_address("192.168.49.77");


        //Set userDbAttributes
        user.setEmail_id(newUser.getEmail_id());
        user.setUsername(newUser.getUsername());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));

        user.setUserDetails(userDetails);
        user.setRegistrationDetails(registrationDetails);
        registerRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationToken(String token, String username, String email_id, String uuid) {
        VerificationToken verificationToken = new
                VerificationToken(token, email_id, username, uuid);
        MaxTokenRegenerateLimit tokenRegenerateLimit =
                new MaxTokenRegenerateLimit(uuid, 0);
        regenerateLimitRepository.save(tokenRegenerateLimit);
        tokenRepository.save(verificationToken);
    }

    @Override
    public String verifyUserEmailAddress(String token) {

        VerificationToken verificationToken = tokenRepository.findByToken(token);
        log.info("Verification token is: {}", verificationToken);

        if (verificationToken == null) {
            return "invalid";
        } else {
            Calendar calendar = Calendar.getInstance();
            if ((verificationToken.getExpiration_time().getTime()
                    - calendar.getTime().getTime()) <= 0) {
                tokenRepository.delete(verificationToken);
                log.info("expired");
                return "expired";
            } else {
                String user_uuid = verificationToken.getUser_uuid();
                String user_emailId = verificationToken.getEmail_id();
                String user_name = verificationToken.getUsername();

                log.info("Extracted username: {}", user_name);
                log.info("Extracted email-id: {}", user_emailId);
                log.info("Extracted user_uuid: {}", user_uuid);

                if (registerRepository.findByUuid(user_uuid) != null &&
                        registerRepository.findByEmail_id(user_emailId) != null &&
                        registerRepository.findByUsername(user_name) != null) {
                    UserDbAttributes verifiedUser = registerRepository.findByUuid(user_uuid);

                    log.info("User details: {}", verifiedUser);

                    RegistrationDetails verifiedDetails = verifiedUser.getRegistrationDetails();

                    if (verifiedDetails.isVerified()) {
                        return "Email-id is already verified";
                    } else {
                        verifiedDetails.setVerified(true);
                        verifiedDetails.setActivated(true);
                        verifiedUser.setRegistrationDetails(verifiedDetails);
                        registerRepository.save(verifiedUser);
                        tokenRepository.delete(verificationToken);
                        return "verified";
                    }
                }
            }

        }
        return "something wrong";
    }

    @Override
    public boolean isUserNameExists(String username) {
        return registerRepository.findByUsername(username) == null;
    }

    @Override
    public boolean isEmailIdExists(String email_id) {
        return registerRepository.findByEmail_id(email_id) == null;
    }

    @Override
    public VerificationToken regenerateVerificationToken(String user_name, String email_id) {
        VerificationToken verificationToken = tokenRepository.findByEmail_id(email_id);
        log.info("Verification token: {}", verificationToken);
        if (verificationToken != null) {
            if (verificationToken.getUsername().equals(user_name)) {
                String user_uuid = verificationToken.getUser_uuid();

                MaxTokenRegenerateLimit rateLimit =
                        regenerateLimitRepository.findByUser_uuid(user_uuid);
                rateLimit.setIssueCount(rateLimit.getIssueCount() + 1);
                regenerateLimitRepository.save(rateLimit);

                tokenRepository.delete(verificationToken);
                VerificationToken newToken = new VerificationToken(UUID.randomUUID().toString()
                        , email_id, user_name, user_uuid);
                tokenRepository.save(newToken);
                return newToken;
            }
        }

        if (verificationToken == null) {
            String user_uuid = registerRepository.findByUsername(user_name).getUuid();
            VerificationToken newToken = new VerificationToken(UUID.randomUUID().toString()
                    , email_id, user_name, user_uuid);
            tokenRepository.save(newToken);

            MaxTokenRegenerateLimit rateLimit =
                    regenerateLimitRepository.findByUser_uuid(user_uuid);
            rateLimit.setIssueCount(rateLimit.getIssueCount() + 1);
            regenerateLimitRepository.save(rateLimit);
            return newToken;
        } else {
            throw new UsernameNotFoundException("Username or email-id doesn't exists");
        }
    }

    @Override
    public boolean isUserVerified(String user_name) {
        return registerRepository.findByUsername(user_name)
                .getRegistrationDetails().isVerified();
    }

    @Override
    public boolean isEmaiIdCorrespondsToUsername(String user_name, String email_id) {
        UserDbAttributes user1 = registerRepository.findByUsername(user_name);
        UserDbAttributes user2 = registerRepository.findByEmail_id(email_id);
        return user1.equals(user2);
    }
}
