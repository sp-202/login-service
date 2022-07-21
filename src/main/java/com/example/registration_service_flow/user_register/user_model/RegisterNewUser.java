package com.example.registration_service_flow.user_register.user_model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class RegisterNewUser {

    @Pattern(regexp = "^[A-z][A-z0-9-_]{3,23}$", message = "Please enter a valid username")
    private String username;


    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$",
            message = "please input a valid password")
    private String password;


    @Pattern(regexp = "^[a-zA-Z0-9.! #$%&'*+/=?^_`{|}~-]{1,20}+@[a-zA-Z0-9-]{3,20}+(?:\\.[a-zA-Z0-9-]{3,8}+)*$",
            message = "Please enter a valid email address")
    private String email_id;


    @Pattern(regexp = "^[A-Za-z\\s]{1,15}[\\.]{0,1}[A-Za-z\\s]{0,10}$",
            message = "Please enter a valid name")
    private String first_name;


    @Pattern(regexp = "^[A-Za-z\\s]{1,15}[\\.]{0,1}[A-Za-z\\s]{0,10}$",
            message = "Please enter a valid name")
    private String last_name;
}
