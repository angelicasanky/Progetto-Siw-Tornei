package it.uniroma3.siw.torneo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    /**
     * Gestisce la richiesta GET su "/login".
     * Mostra la pagina di login dell'applicazione.
     * Spring Security intercetta automaticamente il form di questa pagina per autenticare l'utente.
     *
     * @return il nome della vista Thymeleaf "login"
     */
    @GetMapping("/login")
    public String mostraLogin() {
        return "login";
    }
}