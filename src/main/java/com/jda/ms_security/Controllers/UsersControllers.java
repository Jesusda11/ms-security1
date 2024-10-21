package com.jda.ms_security.Controllers;

import com.jda.ms_security.Models.User;
import com.jda.ms_security.Repositories.UserRepository;
import com.jda.ms_security.services.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


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
    public ResponseEntity<Object> create(@RequestBody User newUser) {
        // Revisa si el correo ya existe con ayuda de optional
        Optional<User> existingUser = this.theUserRepository.getUserByEmail(newUser.getEmail());

        if (existingUser.isPresent()) {
            // Retorna un 400 con mensaje de error personalizado
            return new ResponseEntity<>("Usuario con correo " + newUser.getEmail() + " ya existe", HttpStatus.BAD_REQUEST);
        }

        // Encripta la contraseña y guarda el nuevo usuario
        newUser.setPassword(this.theEncryptionService.convertSHA256(newUser.getPassword()));
        User savedUser = this.theUserRepository.save(newUser);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
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
