package com.jda.ms_security.Controllers;

import com.jda.ms_security.Models.User;
import com.jda.ms_security.Repositories.UserRepository;
import com.jda.ms_security.services.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@CrossOrigin
@RestController


@RequestMapping("/api/users")

public class UsersControllers {
    @Autowired
    UserRepository theUserRepository;
    @Autowired
    EncryptionService theEncryptionService;

    @GetMapping("")
    public List<User> find (){
        return this.theUserRepository.findAll();
    }

    @GetMapping("{id}")
    public User findById(@PathVariable String id){
        User theUser= this.theUserRepository.findById(id).orElse( null);
        return theUser;
    }

    @PostMapping
    public User create(@RequestBody User newUser){
        newUser.setPassword(this.theEncryptionService.convertSHA256(newUser.getPassword()));
        return this.theUserRepository.save(newUser);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        User theUser = this.theUserRepository.findById(id).orElse(null);
        if (theUser!= null){
         this.theUserRepository.delete(theUser);
        }
    }

    @PutMapping("{id}")
    public User update(@PathVariable String id, @RequestBody User newUser){
        User actualUser = this.theUserRepository.findById(id).orElse(null);
        if (actualUser != null){
            actualUser.setName(newUser.getName());
            actualUser.setEmail(newUser.getEmail());
            actualUser.setPassword(this.theEncryptionService.convertSHA256(actualUser.getPassword()));
            this.theUserRepository.save(actualUser);
            return actualUser;
        }

        else {
           return null;
        }
    }



}
