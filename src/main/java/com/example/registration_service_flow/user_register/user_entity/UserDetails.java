package com.example.registration_service_flow.user_register.user_entity;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class UserDetails {

    @Pattern(regexp = "^[A-Za-z\\s]{1,15}[\\.]{0,1}[A-Za-z\\s]{0,10}$",
            message = "Please enter a valid name")
    private String first_name;

    @Pattern(regexp = "^[A-Za-z\\s]{1,15}[\\.]{0,1}[A-Za-z\\s]{0,10}$",
            message = "Please enter a valid name")
    private String last_name;

    private String phone_number;
}
