package com.example.registration_service_flow.token;

import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
@Document(collection = "verification_tokenDb")
public class VerificationToken {

    //Expiration time
    private static final int EXPIRATION_TIME = 10;

    @Id
    @NonNull
    private String token_id;
    @NonNull
    private String token;
    @NonNull
    private Date expiration_time;
    @NonNull
    private String email_id;

    @NonNull
    private String username;
    @NonNull
    private String user_uuid;

    public VerificationToken(@NonNull String token, @NonNull String email_id,
                             @NonNull String username, @NonNull String user_uuid) {
        super();
        this.token_id = UUID.randomUUID().toString();
        this.token = token;
        this.email_id = email_id;
        this.username = username;
        this.user_uuid = user_uuid;
        this.expiration_time = calculateExpirationTime();
    }

    public VerificationToken(@NonNull String token) {
        super();
        this.token = token;
        this.expiration_time = calculateExpirationTime();
    }

    private Date calculateExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, VerificationToken.EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
