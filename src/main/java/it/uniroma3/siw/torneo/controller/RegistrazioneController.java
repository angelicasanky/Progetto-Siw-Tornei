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

    /**
     * Gestisce la richiesta GET su "/registrazione".
     * Mostra il form di registrazione per i nuovi utenti,
     * passando alla vista un oggetto Utente vuoto da popolare.
     *
     * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
     * @return il nome della vista Thymeleaf "registrazione"
     */
    @GetMapping("/registrazione")
    public String formRegistrazione(Model model) {
        model.addAttribute("utente", new Utente());
        return "registrazione";
    }

    /**
     * Gestisce la richiesta POST su "/registrazione".
     * Riceve i dati del form, codifica la password con BCrypt, assegna il ruolo USER
     * (quello di default per la registrazione pubblica) e salva il nuovo utente nel database.
     *
     * @param utente l'oggetto Utente popolato con i dati inseriti nel form
     * @param model  il Model di Spring MVC (disponibile per eventuali estensioni future)
     * @return un redirect verso la pagina di login dopo la registrazione avvenuta con successo
     */
    @PostMapping("/registrazione")
    public String registraUtente(@ModelAttribute("utente") Utente utente, Model model) {
        utente.setPassword(this.passwordEncoder.encode(utente.getPassword()));
        utente.setRuolo(Ruolo.USER); // sempre USER alla registrazione pubblica
        this.utenteService.salvaUtente(utente);
        return "redirect:/login";
    }
}
