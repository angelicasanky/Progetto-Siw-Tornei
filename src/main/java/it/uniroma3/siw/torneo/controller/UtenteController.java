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

    /**
     * Gestisce la richiesta GET su "/utenti".
     * Recupera l'elenco di tutti gli utenti registrati e lo passa alla vista.
     *
     * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
     * @return il nome della vista Thymeleaf "utenti"
     */
    @GetMapping("/utenti")
    public String mostraUtenti(Model model) {
        model.addAttribute("elencoUtenti", this.utenteService.trovaTutti());
        return "utenti";
    }

    /**
     * Gestisce la richiesta GET su "/admin/utente/nuovo" (solo ADMIN).
     * Prepara un oggetto Utente vuoto e lo invia alla vista del form di creazione.
     *
     * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
     * @return il nome della vista Thymeleaf "formNuovoUtente"
     */
    @GetMapping("/admin/utente/nuovo")
    public String formNuovoUtente(Model model) {
        model.addAttribute("utente", new Utente());
        return "formNuovoUtente";
    }

    /**
     * Gestisce la richiesta POST su "/admin/utente" (solo ADMIN).
     * Codifica la password con BCrypt, assegna il ruolo USER e salva il nuovo utente nel database.
     *
     * @param utente l'oggetto Utente popolato con i dati del form
     * @param model  il Model di Spring MVC (disponibile per eventuali estensioni)
     * @return un redirect verso la lista degli utenti
     */
    @PostMapping("/admin/utente")
    public String salvaUtente(@ModelAttribute("utente") Utente utente, Model model) {
        utente.setPassword(this.passwordEncoder.encode(utente.getPassword()));
        utente.setRuolo(Ruolo.USER); // sempre USER per chi si registra dal form
        this.utenteService.salvaUtente(utente);
        return "redirect:/utenti";
    }

    /**
     * Gestisce la richiesta GET su "/utente/{id}".
     * Recupera il dettaglio di un singolo utente tramite il suo ID e lo passa alla vista.
     *
     * @param id    l'identificativo univoco dell'utente da visualizzare
     * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
     * @return il nome della vista Thymeleaf "dettaglioUtente"
     */
    @GetMapping("/utente/{id}")
    public String getUtente(@PathVariable("id") Long id, Model model) {
        Utente utente = this.utenteService.trovaPerId(id);
        model.addAttribute("utente", utente);
        return "dettaglioUtente";
    }

    /**
     * Gestisce la richiesta GET su "/admin/utente/delete/{id}" (solo ADMIN).
     * Elimina dal database l'utente con l'ID specificato e reindirizza alla lista.
     *
     * @param id l'identificativo univoco dell'utente da eliminare
     * @return un redirect verso la lista degli utenti
     */
    @GetMapping("/admin/utente/delete/{id}")
    public String cancellaUtente(@PathVariable("id") Long id) {
        this.utenteService.cancellaUtente(id);
        return "redirect:/utenti";
    }
}