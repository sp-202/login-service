package com.example.registration_service_flow.user_register.user_repository;

import com.example.registration_service_flow.user_register.user_entity.UserDbAttributes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegisterRepository extends MongoRepository<UserDbAttributes, String> {
    UserDbAttributes findByUuid(String user_uuid);

    @Query("{'email_id':?0}")
    UserDbAttributes findByEmail_id(String user_uuid);

    UserDbAttributes findByUsername(String user_uuid);
}
