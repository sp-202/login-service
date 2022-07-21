package com.example.registration_service_flow.token.rate_limit;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MaxTokenRegenerateLimitRepository
        extends MongoRepository<MaxTokenRegenerateLimit, Long> {

    @Query("{'user_uuid':?0}")
    MaxTokenRegenerateLimit findByUser_uuid(String user_uuid);
}
