package it.uniroma3.siw.torneo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.torneo.model.Ruolo;
import it.uniroma3.siw.torneo.model.Utente;
import it.uniroma3.siw.torneo.service.UtenteService;

@Controller
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/utenti")
    public String mostraUtenti(Model model) {
        model.addAttribute("elencoUtenti", this.utenteService.trovaTutti());
        return "utenti";
    }

    @GetMapping("/admin/utente/nuovo")
    public String formNuovoUtente(Model model) {
        model.addAttribute("utente", new Utente());
        return "formNuovoUtente";
    }

    @PostMapping("/admin/utente")
    public String salvaUtente(@ModelAttribute("utente") Utente utente, Model model) {
        utente.setPassword(this.passwordEncoder.encode(utente.getPassword()));
        utente.setRuolo(Ruolo.USER); // sempre USER per chi si registra dal form
        this.utenteService.salvaUtente(utente);
        return "redirect:/utenti";
    }

    @GetMapping("/utente/{id}")
    public String getUtente(@PathVariable("id") Long id, Model model) {
        Utente utente = this.utenteService.trovaPerId(id);
        model.addAttribute("utente", utente);
        return "dettaglioUtente";
    }

    @GetMapping("/admin/utente/delete/{id}")
    public String cancellaUtente(@PathVariable("id") Long id) {
        this.utenteService.cancellaUtente(id);
        return "redirect:/utenti";
    }
}