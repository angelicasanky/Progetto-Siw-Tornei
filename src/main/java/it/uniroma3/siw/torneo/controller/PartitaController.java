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

    /**
     * Gestisce la richiesta GET su "/partite".
     * Recupera l'elenco di tutte le partite e lo passa alla vista.
     *
     * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
     * @return il nome della vista Thymeleaf "partite"
     */
    @GetMapping("/partite")
    public String mostraPartite(Model model) {
        model.addAttribute("elencoPartite", this.partitaService.trovaTutte());
        return "partite";
    }

    /**
     * Gestisce la richiesta GET su "/admin/partita/nuova" (solo ADMIN).
     * Prepara un oggetto Partita vuoto e carica le liste di tornei, squadre e arbitri
     * necessarie per compilare il form di creazione.
     *
     * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
     * @return il nome della vista Thymeleaf "formNuovaPartita"
     */
    @GetMapping("/admin/partita/nuova")
    public String formNuovaPartita(Model model) {
        model.addAttribute("partita", new Partita());
        model.addAttribute("elencoTornei", this.torneoService.trovaTutti());
        model.addAttribute("elencoSquadre", this.squadraService.trovaTutti());
        model.addAttribute("elencoArbitri", this.arbitroService.trovaTutti());
        return "formNuovaPartita";
    }

    /**
     * Gestisce la richiesta POST su "/admin/partita" (solo ADMIN).
     * Riceve i dati del form, risolve le entità associate (torneo, squadre, arbitro)
     * tramite i rispettivi service e salva la nuova partita nel database.
     *
     * @param partita       l'oggetto Partita popolato con i campi base del form
     * @param torneoId      l'ID del torneo a cui appartiene la partita (opzionale)
     * @param squadraCasaId l'ID della squadra di casa (opzionale)
     * @param squadraOspiteId l'ID della squadra ospite (opzionale)
     * @param arbitroId     l'ID dell'arbitro designato (opzionale)
     * @param model         il Model di Spring MVC (disponibile per eventuali estensioni)
     * @return un redirect verso la lista delle partite
     */
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

    /**
     * Gestisce la richiesta GET su "/partita/{id}".
     * Recupera il dettaglio di una singola partita tramite il suo ID e lo passa alla vista.
     *
     * @param id    l'identificativo univoco della partita da visualizzare
     * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
     * @return il nome della vista Thymeleaf "dettaglioPartita"
     */
    @GetMapping("/partita/{id}")
    public String getPartita(@PathVariable("id") Long id, Model model) {
        Partita partita = this.partitaService.trovaPerId(id);
        model.addAttribute("partita", partita);
        return "dettaglioPartita";
    }

    /**
     * Gestisce la richiesta GET su "/admin/partita/delete/{id}" (solo ADMIN).
     * Elimina la partita con l'ID specificato e reindirizza alla lista.
     *
     * @param id l'identificativo univoco della partita da eliminare
     * @return un redirect verso la lista delle partite
     */
    @GetMapping("/admin/partita/delete/{id}")
    public String cancellaPartita(@PathVariable("id") Long id) {
        this.partitaService.eliminaPartita(id);
        return "redirect:/partite";
    }

    /**
     * Gestisce la richiesta GET su "/admin/partita/edit/{id}" (solo ADMIN).
     * Carica i dati della partita esistente e l'elenco delle squadre disponibili per prepopolare il form.
     *
     * @param id    l'identificativo univoco della partita da modificare
     * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
     * @return il nome della vista Thymeleaf "formNuovaPartita" (riutilizzata per la modifica)
     */
    @GetMapping("/admin/partita/edit/{id}")
    public String editPartita(@PathVariable("id") Long id, Model model) {
        model.addAttribute("partita", partitaService.trovaPerId(id));
        model.addAttribute("squadre", squadraService.trovaTutti());

        return "formNuovaPartita";
    }

    /**
     * Gestisce la richiesta POST su "/admin/partita/edit/{id}" (solo ADMIN).
     * Aggiorna i campi di una partita esistente (data, luogo, gol, stato e le entità correlate)
     * e salva le modifiche nel database.
     *
     * @param id              l'identificativo univoco della partita da aggiornare
     * @param partitaDettagli l'oggetto con i nuovi dati provenienti dal form
     * @return un redirect verso la pagina di dettaglio della partita aggiornata
     */
    @PostMapping("/admin/partita/edit/{id}")
    public String updatePartita(@PathVariable("id") Long id, @ModelAttribute("partita") Partita partitaDettagli) {

        // Sostituisci "trovaPerId" con il nome effettivo del metodo nel tuo
        // partitaService (es. findById)
        Partita partitaEsistente = partitaService.trovaPerId(id);

        if (partitaEsistente != null) {

            partitaEsistente.setDataOra(partitaDettagli.getDataOra());
            partitaEsistente.setLuogo(partitaDettagli.getLuogo());
            partitaEsistente.setGoalsHome(partitaDettagli.getGoalsHome());
            partitaEsistente.setGoalsAway(partitaDettagli.getGoalsAway());

            partitaEsistente.setStato(partitaDettagli.getStato());

            partitaEsistente.setTorneo(partitaDettagli.getTorneo());
            partitaEsistente.setSquadraCasa(partitaDettagli.getSquadraCasa());
            partitaEsistente.setSquadraOspite(partitaDettagli.getSquadraOspite());
            partitaEsistente.setArbitro(partitaDettagli.getArbitro());

            partitaService.salvaPartita(partitaEsistente);
        }

        return "redirect:/partita/" + id;
    }

}
