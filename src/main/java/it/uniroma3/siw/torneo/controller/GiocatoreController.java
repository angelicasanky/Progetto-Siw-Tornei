package it.uniroma3.siw.torneo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.torneo.model.Giocatore;
import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.service.GiocatoreService;
import it.uniroma3.siw.torneo.service.SquadraService;

@Controller
public class GiocatoreController {

    @Autowired
    private GiocatoreService giocatoreService;

    @Autowired
    private SquadraService squadraService;

    @GetMapping("/giocatori")
    public String mostraGiocatori(Model model) {
        model.addAttribute("elencoGiocatori", giocatoreService.trovaTutti());
        return "giocatori";
    }

    @GetMapping("/admin/giocatore/nuovo")
    public String formNuovoGiocatore(Model model) {
        model.addAttribute("giocatore", new Giocatore());
        model.addAttribute("elencoSquadre", this.squadraService.trovaTutti());
        return "formNuovoGiocatore";
    }

    @PostMapping("/admin/giocatore")
    public String salvaGiocatore(@ModelAttribute("giocatore") Giocatore giocatore,
            @RequestParam(value = "squadraId", required = false) Long squadraId,
            Model model) {

        // recupero manualmente la squadra scelta e la assegno al giocatore
        if (squadraId != null) {
            Squadra squadra = this.squadraService.trovaPerId(squadraId);
            giocatore.setSquadra(squadra);
        }

        this.giocatoreService.salvaGiocatore(giocatore);
        return "redirect:/giocatori";
    }

    @GetMapping("/giocatore/{id}")
    public String getGiocatore(@PathVariable("id") Long id, Model model) {
        Giocatore giocatore = this.giocatoreService.trovaPerId(id);
        model.addAttribute("giocatore", giocatore);
        return "dettaglioGiocatore";
    }

    @GetMapping("/admin/giocatore/delete/{id}")
    public String cancellaGiocatore(@PathVariable("id") Long id) {
        this.giocatoreService.cancellaGiocatore(id);
        return "redirect:/giocatori";
    }
}