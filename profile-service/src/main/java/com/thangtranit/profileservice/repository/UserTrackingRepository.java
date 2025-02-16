package com.thangtranit.profileservice.repository;

import com.thangtranit.profileservice.entity.UserTracking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTrackingRepository extends MongoRepository<UserTracking, String> {
}
