package com.jda.ms_security.Controllers;
import com.jda.ms_security.Models.EmailContent;
import com.jda.ms_security.Models.Session;
import com.jda.ms_security.Models.TwoFactorRequest;
import com.jda.ms_security.Models.User;
import com.jda.ms_security.Repositories.SessionRepository;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    @Autowired
    private SessionRepository theSessionRepository;

    @PostMapping("/login")
    public HashMap<String,Object> login(@RequestBody User theNewUser,
                                        final HttpServletResponse response) throws IOException {
        HashMap<String,Object> theResponse = new HashMap<>();

        // Sacar el usuario de optional
        Optional<User> optionalUser = this.theUserRepository.getUserByEmail(theNewUser.getEmail());

        if (optionalUser.isPresent()) {
            User theActualUser = optionalUser.get();
            if (theActualUser.getPassword().equals(theEncryptionService.convertSHA256(theNewUser.getPassword()))) {
                // Generar código 2FA de seis dígitos
                int twoFactorCode = (int) (Math.random() * 900000) + 100000;

                // Generar token JWT
                String token = theJwtService.generateToken(theActualUser);

                // Calcular la fecha de expiración (ejemplo: 10 minutos)
                String expiration = LocalDateTime.now().plusMinutes(10).toString();

                // Crear una nueva sesión con el token y el código 2FA
                Session session = new Session(token, expiration);
                session.setUser(theActualUser);
                session.setTwoFactorCode(twoFactorCode);  // Aquí guardas el código 2FA
                theSessionRepository.save(session);  // Guardar la sesión en la base de datos

                // Enviar el código 2FA por correo
                EmailContent emailContent = new EmailContent();
                emailContent.setRecipient(theActualUser.getEmail());
                emailContent.setSubject("Código de autenticación");
                emailContent.setContent(String.valueOf(twoFactorCode));

                sendEmail(emailContent);

                theResponse.put("message", "Se ha enviado un código de verificación a tu correo.");
                return theResponse;
            }
        }

        // Si falla el login
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return theResponse;
    }

    @PostMapping("/verify-2fa")
    public HashMap<String,Object> verifyTwoFactor(@RequestBody TwoFactorRequest twoFactorRequest,
                                                  final HttpServletResponse response) throws IOException {
        HashMap<String,Object> theResponse = new HashMap<>();

        // Obtener el email del request y buscar al usuario por email
        Optional<User> user = this.theUserRepository.getUserByEmail(twoFactorRequest.getEmail());

        if (user.isPresent()) {
            User theActualUser = user.get();

            // Buscar todas las sesiones del usuario a partir del usuario encontrado
            List<Session> sessions = this.theSessionRepository.getSessionsByUser(theActualUser.get_id());

            if (!sessions.isEmpty()) {
                // Iterar sobre las sesiones para verificar el código de 2FA
                for (Session session : sessions) {
                    if (session.getTwoFactorCode() == (twoFactorRequest.getTwoFactorCode())) {
                        // Si el código es correcto, ocultar la contraseña y devolver los detalles de la sesión
                        theActualUser.setPassword("");  // Esconde la contraseña antes de enviar la data
                        theResponse.put("token", session.getToken());  // Aquí ya tienes el token listo
                        theResponse.put("user", theActualUser);
                        return theResponse;
                    }
                }
                // Ninguna sesión tiene el código correcto
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Código de verificación incorrecto.");
            } else {
                // No se encontraron sesiones para el usuario
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sesión no encontrada.");
            }
        } else {
            // Usuario no encontrado
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado.");
        }

        return theResponse;
    }

    public ResponseEntity<String> sendEmail(@RequestBody EmailContent emailContent) {
        requestService.sendEmail(emailContent);
        return new ResponseEntity<>("Email sent", HttpStatus.OK);
    }

    @PostMapping("/resetpassword/{userId}")
    public String resetPassword(@PathVariable String userId, final HttpServletResponse response) throws IOException {
        User theActualUser = this.theUserRepository.findById(userId).orElse(null);
        if (theActualUser != null) {
            // Generar una nueva contraseña
            String newPassword = this.generateRandomPassword();

            // Encriptar la nueva contraseña
            String encryptedPassword = theEncryptionService.convertSHA256(newPassword);

            // Guardar la contraseña encriptada en la base de datos
            theActualUser.setPassword(encryptedPassword);
            this.theUserRepository.save(theActualUser);

            // Enviar la contraseña al correo del usuario
            EmailContent emailContent = new EmailContent();
            emailContent.setRecipient(theActualUser.getEmail());
            emailContent.setSubject("Nueva contraseña");
            emailContent.setContent(newPassword);

            sendEmail(emailContent);

            return "message: Password reset and sent to email";
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return "message: User not found";
        }
    }

    // Método para generar una contraseña aleatoria
    private String generateRandomPassword() {
        // Puedes generar una contraseña con la lógica que prefieras, aquí un ejemplo simple
        return UUID.randomUUID().toString().substring(0, 8); // Por ejemplo, generar 8 caracteres aleatorios
    }

}
