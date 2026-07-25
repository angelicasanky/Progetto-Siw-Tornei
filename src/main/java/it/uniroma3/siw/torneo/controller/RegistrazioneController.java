package it.uniroma3.siw.torneo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.torneo.model.Ruolo;
import it.uniroma3.siw.torneo.model.Utente;
import it.uniroma3.siw.torneo.service.UtenteService;

@Controller
public class RegistrazioneController {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registrazione")
    public String formRegistrazione(Model model) {
        model.addAttribute("utente", new Utente());
        return "registrazione";
    }

    @PostMapping("/registrazione")
    public String registraUtente(@ModelAttribute("utente") Utente utente, Model model) {
        utente.setPassword(this.passwordEncoder.encode(utente.getPassword()));
        utente.setRuolo(Ruolo.USER); // sempre USER alla registrazione pubblica
        this.utenteService.salvaUtente(utente);
        return "redirect:/login";
    }
}
