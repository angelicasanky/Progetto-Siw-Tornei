package it.uniroma3.siw.torneo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.torneo.model.Arbitro;
import it.uniroma3.siw.torneo.service.ArbitroService;

@Controller
public class ArbitroController {

    @Autowired
    private ArbitroService arbitroService;

    /**
     * Gestisce la richiesta GET su "/arbitri".
     * Recupera l'elenco completo degli arbitri dal service e lo passa alla vista.
     *
     * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
     * @return il nome della vista Thymeleaf "arbitri"
     */
    @GetMapping("/arbitri")
    public String mostraArbitri(Model model) {
        model.addAttribute("elencoArbitri", this.arbitroService.trovaTutti());
        return "arbitri";
    }

    /**
     * Gestisce la richiesta GET su "/admin/arbitro/nuovo" (solo ADMIN).
     * Prepara un oggetto Arbitro vuoto e lo invia alla vista del form di creazione.
     *
     * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
     * @return il nome della vista Thymeleaf "formNuovoArbitro"
     */
    @GetMapping("/admin/arbitro/nuovo")
    public String formNuovoArbitro(Model model) {
        model.addAttribute("arbitro", new Arbitro());
        return "formNuovoArbitro";
    }

    /**
     * Gestisce la richiesta POST su "/admin/arbitro" (solo ADMIN).
     * Riceve i dati del form, salva il nuovo arbitro nel database e reindirizza alla lista.
     *
     * @param arbitro l'oggetto Arbitro popolato automaticamente da Spring con i dati del form
     * @param model   il Model di Spring MVC (non utilizzato direttamente, disponibile per estensioni)
     * @return un redirect verso la lista degli arbitri
     */
    @PostMapping("/admin/arbitro")
    public String salvaArbitro(@ModelAttribute("arbitro") Arbitro arbitro, Model model) {
        this.arbitroService.salvaArbitro(arbitro);
        return "redirect:/arbitri";
    }

    /**
     * Gestisce la richiesta GET su "/arbitro/{id}".
     * Recupera un singolo arbitro tramite il suo ID e lo passa alla vista di dettaglio.
     *
     * @param id    l'identificativo univoco dell'arbitro da visualizzare
     * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
     * @return il nome della vista Thymeleaf "dettaglioArbitro"
     */
    @GetMapping("/arbitro/{id}")
    public String getArbitro(@PathVariable("id") Long id, Model model) {
        Arbitro arbitro = this.arbitroService.trovaPerId(id);
        model.addAttribute("arbitro", arbitro);
        return "dettaglioArbitro";
    }

    /**
     * Gestisce la richiesta GET su "/admin/arbitro/delete/{id}" (solo ADMIN).
     * Elimina dal database l'arbitro con l'ID specificato e reindirizza alla lista.
     *
     * @param id l'identificativo univoco dell'arbitro da eliminare
     * @return un redirect verso la lista degli arbitri
     */
    @GetMapping("/admin/arbitro/delete/{id}")
    public String cancellaArbitro(@PathVariable("id") Long id) {
        this.arbitroService.cancellaArbitro(id);
        return "redirect:/arbitri";
    }
}