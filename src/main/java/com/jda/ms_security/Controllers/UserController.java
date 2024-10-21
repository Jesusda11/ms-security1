package com.jda.ms_security.Controllers;

import com.jda.ms_security.Models.User;
import com.jda.ms_security.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/info")
    public ResponseEntity<User> getUserInfo(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) principal;
            String name = token.getPrincipal().getAttribute("name"); // Obtener nombre
            String email = token.getPrincipal().getAttribute("email"); // Obtener email
            String birthdate = token.getPrincipal().getAttribute("birthdate"); // Fecha de nacimiento


            // Crear un objeto para devolverlo
            User user = new User();
            user.setName(name);
            user.setEmail(email);

            // Imprime en consola
            System.out.println("Nombre: " + name);
            System.out.println("Email: " + email);
            System.out.println("Fecha Nacimiento: " + birthdate);

            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

