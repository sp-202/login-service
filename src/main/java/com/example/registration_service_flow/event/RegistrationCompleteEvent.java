package com.example.registration_service_flow.event;

import com.example.registration_service_flow.user_register.user_entity.UserDbAttributes;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private final UserDbAttributes user;
    private final String applicationURL;

    public RegistrationCompleteEvent(UserDbAttributes user, String applicationURL) {
        super(user);
        this.user = user;
        this.applicationURL = applicationURL;
    }
}
