package com.example.registration_service_flow.user_register.user_entity;

import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;

@Data
@Document(collection = "user_db")
@NoArgsConstructor
public class UserDbAttributes {

    @Id
    @NonNull
    private String uuid;

    @Pattern(regexp = "^[A-z][A-z0-9-_]{3,23}$", message = "Please enter a valid username")
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$",
            message = "please input a valid password")
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9.! #$%&'*+/=?^_`{|}~-]{1,20}+@[a-zA-Z0-9-]{3,20}+(?:\\.[a-zA-Z0-9-]{3,8}+)*$",
            message = "Please enter a valid email address")
    private String email_id;


    private UserDetails userDetails;
    private RegistrationDetails registrationDetails;
}
