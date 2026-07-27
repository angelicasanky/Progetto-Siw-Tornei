package it.uniroma3.siw.torneo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.torneo.model.Commento;
import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.model.Utente;
import it.uniroma3.siw.torneo.service.CommentoService;
import it.uniroma3.siw.torneo.service.PartitaService;
import it.uniroma3.siw.torneo.service.UtenteService;

@Controller
public class CommentoController {

    @Autowired
    private CommentoService commentoService;

    @Autowired
    private PartitaService partitaService;

    @Autowired
    private UtenteService utenteService;

    /**
     * Gestisce la richiesta GET su "/partita/{partitaId}/commento/nuovo".
     * Mostra il form per aggiungere un commento a una specifica partita.
     * Accessibile solo agli utenti autenticati.
     *
     * @param partitaId l'identificativo della partita a cui si vuole aggiungere il commento
     * @param model     il Model di Spring MVC usato per passare un Commento vuoto e l'ID della partita alla vista
     * @return il nome della vista Thymeleaf "formNuovoCommento"
     */
    @GetMapping("/partita/{partitaId}/commento/nuovo")
    public String formNuovoCommento(@PathVariable("partitaId") Long partitaId, Model model) {
        model.addAttribute("commento", new Commento());
        model.addAttribute("partitaId", partitaId);

        return "formNuovoCommento";
    }

    /**
     * Gestisce la richiesta POST su "/partita/{partitaId}/commento".
     * Salva il commento scritto dall'utente autenticato per una determinata partita.
     * Associa automaticamente la partita e l'utente loggato all'oggetto Commento prima del salvataggio.
     *
     * @param partitaId l'identificativo della partita a cui associare il commento
     * @param commento  l'oggetto Commento popolato con i dati del form
     * @param principal l'utente attualmente autenticato, usato per ricavarne lo username
     * @param model     il Model di Spring MVC (disponibile per eventuali estensioni)
     * @return un redirect verso la pagina di dettaglio della partita
     */
    @PostMapping("/partita/{partitaId}/commento")
    public String salvaCommento(@PathVariable("partitaId") Long partitaId,
            @ModelAttribute("commento") Commento commento,
            Principal principal,
            Model model) {

        Partita partita = this.partitaService.trovaPerId(partitaId);
        commento.setPartita(partita);

        Utente utenteLoggato = this.utenteService.trovaPerUsername(principal.getName());
        commento.setUtente(utenteLoggato);

        this.commentoService.salvaCommento(commento);
        return "redirect:/partita/" + partitaId;
    }

    /**
     * Gestisce la richiesta GET su "/commento/delete/{id}".
     * Elimina un commento solo se l'utente corrente è il proprietario oppure ha ruolo ADMIN.
     * In caso di accesso non autorizzato (commento altrui e non admin), reindirizza alla lista tornei.
     *
     * @param id             l'identificativo del commento da eliminare
     * @param principal      l'utente attualmente autenticato
     * @param authentication l'oggetto di autenticazione Spring Security, usato per verificare i ruoli
     * @return un redirect verso la partita se l'eliminazione va a buon fine, o verso la lista tornei
     */
    @GetMapping("/commento/delete/{id}")
    public String eliminaCommento(@PathVariable("id") Long id, Principal principal,
            org.springframework.security.core.Authentication authentication) {
        Commento commento = commentoService.trovaPerId(id);

        if (commento != null) {
            Long partitaId = commento.getPartita().getId();

            // Username dell'utente corrente
            String usernameCorrente = principal.getName();

            // Verifica se l'utente è il proprietario del commento
            boolean isProprietario = commento.getUtente().getUsername().equals(usernameCorrente);

            // Verifica se l'utente ha il ruolo ADMIN
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));

            if (isProprietario || isAdmin) {
                commentoService.cancellaCommento(id);
                return "redirect:/partita/" + partitaId;
            }
        }

        return "redirect:/tornei";
    }
}