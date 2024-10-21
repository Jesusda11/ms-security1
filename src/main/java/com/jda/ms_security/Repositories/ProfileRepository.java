package com.jda.ms_security.Repositories;

import com.jda.ms_security.Models.Profile;
import com.jda.ms_security.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProfileRepository extends MongoRepository<Profile, String> {
}
