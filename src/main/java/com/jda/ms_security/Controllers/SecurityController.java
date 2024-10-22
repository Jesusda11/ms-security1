package com.jda.ms_security.Controllers;


import com.jda.ms_security.Models.EmailContent;
import com.jda.ms_security.Models.User;
import com.jda.ms_security.Repositories.UserRepository;
import com.jda.ms_security.services.EncryptionService;
import com.jda.ms_security.services.JwtService;
import com.jda.ms_security.services.RequestService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/public/security")
public class SecurityController {
    @Autowired
    private UserRepository theUserRepository;
    @Autowired
    private EncryptionService theEncryptionService;
    @Autowired
    private JwtService theJwtService;
    @Autowired
    private RequestService requestService;

    @PostMapping("/login")
    public HashMap<String,Object> login(@RequestBody User theNewUser,
                                        final HttpServletResponse response)throws IOException {
        HashMap<String,Object> theResponse=new HashMap<>();
        String token="";

        //Sacar el usuario de optional
        Optional<User> optionalUser = this.theUserRepository.getUserByEmail(theNewUser.getEmail());

        if (optionalUser.isPresent()) {
            User theActualUser = optionalUser.get();
            if (theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))) {
                token = theJwtService.generateToken(theActualUser);
                theActualUser.setPassword("");  // Esconde la contraseña antes de mandar la data
                theResponse.put("token", token);
                theResponse.put("user", theActualUser);  // Comentar linea si no quiero que se vea el usuario

                EmailContent emailContent = new EmailContent();
                emailContent.setRecipient(theActualUser.getEmail());
                emailContent.setSubject("Login Exitoso");
                emailContent.setContent("Hola " + theActualUser.getName() + ", tu login fue exitoso!");

                sendEmail(emailContent);
                return theResponse;
            }
        }

        // Send unauthorized response if login fails
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return theResponse;

    }

    public ResponseEntity<String> sendEmail(@RequestBody EmailContent emailContent) {
        requestService.sendEmail(emailContent);
        return new ResponseEntity<>("Email sent", HttpStatus.OK);
    }

}
