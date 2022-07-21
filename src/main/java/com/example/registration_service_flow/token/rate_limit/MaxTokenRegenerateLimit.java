package com.example.registration_service_flow.token.rate_limit;

import com.mongodb.lang.NonNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;

@Data
@Document(collection = "token_limit_db")
public class MaxTokenRegenerateLimit {

    private static final int LOCK_TIME = 120;

    @Id
    @NonNull
    private String id;
    @NonNull
    private String user_uuid;
    @NonNull
    private int issueCount;
    @NonNull
    private Date lockedTime;


    public MaxTokenRegenerateLimit(@NonNull String user_uuid, @NonNull int issueCount) {
        this.user_uuid = user_uuid;
        this.issueCount = issueCount;
        this.lockedTime = calculateExpirationTime();
    }

    private Date calculateExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, LOCK_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
