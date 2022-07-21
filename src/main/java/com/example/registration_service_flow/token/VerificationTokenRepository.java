package com.example.registration_service_flow.token;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends
        MongoRepository<VerificationToken, String> {
    VerificationToken findByToken(String token);

    @Query("{'email_id':?0}")
    VerificationToken findByEmail_id(String email_id);
}
