package com.example.registration_service_flow.user_register.user_entity;

import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegistrationDetails {

    @NonNull
    private String ip_address;
    @NonNull
    private String time_stamp;
    @NonNull
    private boolean isActivated = false;
    @NonNull
    private boolean isLocked = false;
    @NonNull
    private boolean isVerified = false;
}
