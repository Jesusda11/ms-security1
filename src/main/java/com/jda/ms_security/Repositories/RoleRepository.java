package com.jda.ms_security.Repositories;

import com.jda.ms_security.Models.Role;
import com.jda.ms_security.Models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

}
