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

    /* form per aggiungere un commento a una specifica partita */
    @GetMapping("/partita/{partitaId}/commento/nuovo")
    public String formNuovoCommento(@PathVariable("partitaId") Long partitaId, Model model) {
        model.addAttribute("commento", new Commento());
        model.addAttribute("partitaId", partitaId);

        return "formNuovoCommento";
    }

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