package com.example.registration_service_flow.user_register.user_entity;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class RegenerateTokenPayLoad {

    @Pattern(regexp = "^[A-z][A-z0-9-_]{3,23}$", message = "Please enter a valid username")
    private String username;

    @Pattern(regexp = "^[a-zA-Z0-9.! #$%&'*+/=?^_`{|}~-]{1,20}+@[a-zA-Z0-9-]{3,20}+(?:\\.[a-zA-Z0-9-]{3,8}+)*$",
            message = "Please enter a valid email address")
    private String email_id;
}
