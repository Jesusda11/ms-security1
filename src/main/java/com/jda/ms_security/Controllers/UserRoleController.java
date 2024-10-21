package com.jda.ms_security.Controllers;

import com.jda.ms_security.Models.Role;
import com.jda.ms_security.Models.User;
import com.jda.ms_security.Models.UserRole;
import com.jda.ms_security.Repositories.RoleRepository;
import com.jda.ms_security.Repositories.UserRepository;
import com.jda.ms_security.Repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/user_role")

public class UserRoleController {
    @Autowired
    UserRepository theUserRepository;

    @Autowired
    RoleRepository theRoleRepository;

    @Autowired
    UserRoleRepository theUserRoleRepository;

    @PostMapping("user/{userId}/role/{roleId}")
    public UserRole create(@PathVariable String userId, @PathVariable String roleId){
        User theUser = this.theUserRepository.findById (userId).orElse(null);
        Role theRole = this.theRoleRepository.findById(roleId).orElse(null);

        if (theUser!=null && theRole!= null){
            UserRole newUserRole = new UserRole();
            newUserRole.setUser(theUser);
            newUserRole.setRole(theRole);
            this.theUserRoleRepository.save(newUserRole);
            return newUserRole;
        }else{
            return  null;
        }

    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        User theUser = this.theUserRepository.findById(id).orElse(null);
        if (theUser!= null){
         this.theUserRepository.delete(theUser);
        }
    }

    @GetMapping("user/{userId}")
    public List<UserRole> getRolesByUser(@PathVariable String userId){
        return this.theUserRoleRepository.getRolesByUser(userId);
    }

    @GetMapping("role/{roleId}")
    public List<UserRole> getUserByRole(@PathVariable String roleId) {
        return this.theUserRoleRepository.getUsersByRol(roleId);
    }
}
