package com.example.registration_service_flow.user_register.service;

import com.example.registration_service_flow.token.VerificationToken;
import com.example.registration_service_flow.user_register.user_entity.UserDbAttributes;
import com.example.registration_service_flow.user_register.user_model.RegisterNewUser;

public interface UserService {

    UserDbAttributes registerUser(RegisterNewUser newUser);

    void saveVerificationToken(String token, String username, String email_id, String uuid);

    String verifyUserEmailAddress(String token);

    boolean isUserNameExists(String username);

    boolean isEmailIdExists(String email_id);

    VerificationToken regenerateVerificationToken(String user_name, String email_id);

    boolean isUserVerified(String user_name);

    boolean isEmaiIdCorrespondsToUsername(String user_name, String email_id);
}
