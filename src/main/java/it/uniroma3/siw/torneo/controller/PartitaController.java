package it.uniroma3.siw.torneo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import it.uniroma3.siw.torneo.model.Arbitro;
import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.model.Torneo;
import it.uniroma3.siw.torneo.service.ArbitroService;
import it.uniroma3.siw.torneo.service.PartitaService;
import it.uniroma3.siw.torneo.service.SquadraService;
import it.uniroma3.siw.torneo.service.TorneoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PartitaController {

    @Autowired
    private PartitaService partitaService;

    @Autowired
    private TorneoService torneoService;

    @Autowired
    private SquadraService squadraService;

    @Autowired
    private ArbitroService arbitroService;

    @GetMapping("/partite")
    public String mostraPartite(Model model) {
        model.addAttribute("elencoPartite", this.partitaService.trovaTutte());
        return "partite";
    }

    @GetMapping("/admin/partita/nuova")
    public String formNuovaPartita(Model model) {
        model.addAttribute("partita", new Partita());
        model.addAttribute("elencoTornei", this.torneoService.trovaTutti());
        model.addAttribute("elencoSquadre", this.squadraService.trovaTutti());
        model.addAttribute("elencoArbitri", this.arbitroService.trovaTutti());
        return "formNuovaPartita";
    }

    @PostMapping("/admin/partita")
    public String salvaPartita(@ModelAttribute("partita") Partita partita,
            @RequestParam(value = "torneoId", required = false) Long torneoId,
            @RequestParam(value = "squadraCasaId", required = false) Long squadraCasaId,
            @RequestParam(value = "squadraOspiteId", required = false) Long squadraOspiteId,
            @RequestParam(value = "arbitroId", required = false) Long arbitroId, Model model) {

        if (torneoId != null) {
            Torneo torneo = this.torneoService.trovaPerId(torneoId);
            partita.setTorneo(torneo);
        }
        if (squadraCasaId != null) {
            Squadra squadraCasa = this.squadraService.trovaPerId(squadraCasaId);
            partita.setSquadraCasa(squadraCasa);
        }
        if (squadraOspiteId != null) {
            Squadra squadraOspite = this.squadraService.trovaPerId(squadraOspiteId);
            partita.setSquadraOspite(squadraOspite);
        }
        if (arbitroId != null) {
            Arbitro arbitro = this.arbitroService.trovaPerId(arbitroId);
            partita.setArbitro(arbitro);
        }

        this.partitaService.salvaPartita(partita);
        return "redirect:/partite";
    }

    @GetMapping("/partita/{id}")
    public String getPartita(@PathVariable("id") Long id, Model model) {
        Partita partita = this.partitaService.trovaPerId(id);
        model.addAttribute("partita", partita);
        return "dettaglioPartita";
    }

    @GetMapping("/admin/partita/delete/{id}")
    public String cancellaPartita(@PathVariable("id") Long id) {
        this.partitaService.eliminaPartita(id);
        return "redirect:/partite";
    }

}
