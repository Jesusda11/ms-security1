package com.jda.ms_security.Controllers;

import com.jda.ms_security.Models.EmailContent;
import com.jda.ms_security.services.RequestService;
import com.jda.ms_security.services.oauth2Service;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class authController {
    @Autowired
    private oauth2Service oauth2Service;

    @Autowired
    private RequestService requestService;

    //creamos el endPoint para redireccionar a google

    @GetMapping("/google")
    public RedirectView authenticateWithGoogle(HttpSession session){ //Recibo la sesion del navegador, en este caso de google
        String state = UUID.randomUUID().toString(); //Redireccion en el navegador
        session.setAttribute("oauth_state", state); //Creo URL a la que redirecciono el usuario
        String authUrl = oauth2Service.getGoogleAuthUrl(state);
        return new RedirectView(authUrl); //Url a la que quiero red
    }

    @GetMapping("/github")
    public RedirectView authenticateWithGitHub(HttpSession session){ //Recibo la sesion del navegador, en este caso de google
        String state = UUID.randomUUID().toString(); //Identificador universal unico
        session.setAttribute("oauth_state", state); //Autenticidad a la solicitud
        String authUrl = oauth2Service.getGitHubAuthUrl(state); //Genero la url
        return new RedirectView(authUrl); //Url a la que quiero redireccionar
    }

    //manejamos la redirección cuando se haya logueado
    @GetMapping("/callback/{provider}")
    public ResponseEntity<?> callback(@PathVariable String provider, @RequestParam String code, @RequestParam String state, HttpSession session) {
        String sessionState = (String) session.getAttribute("oauth_state");
        if (sessionState == null || !sessionState.equals(state)) {
            return ResponseEntity.badRequest().body("Estado inválido");
        }

        if ("google".equalsIgnoreCase(provider)) {
            //Procedemos a intercambiar el code por un token de acceso.
            Map<String, Object> tokenResponse = oauth2Service.getGoogleAccessToken(code);
            String accessToken = (String) tokenResponse.get("access_token");

            Map<String, Object> userInfo = oauth2Service.getGoogleUserInfo(accessToken);
            // Aquí puedes manejar la lógica de tu aplicación

            return ResponseEntity.ok(userInfo);
        } else if ("github".equalsIgnoreCase(provider)) {
            //Procedemos a intercambiar el code por un token de acceso.
            Map<String, Object> tokenResponse = oauth2Service.getGitHubAccessToken(code);
            String accessToken = (String) tokenResponse.get("access_token");

            Map<String, Object> userInfo = oauth2Service.getGithubUserInfo(accessToken);
            return ResponseEntity.ok(userInfo);
        } else {
            return null;
        }

    }

    //Endpoint para enviar correo de confirmación de login
    @PostMapping("/login")
    public ResponseEntity<String> sendEmail(@RequestBody EmailContent emailContent) {
        requestService.sendEmail(emailContent);
        return new ResponseEntity<>("Email sent", HttpStatus.OK);
    }

}

