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

    @GetMapping("/arbitri")
    public String mostraArbitri(Model model) {
        model.addAttribute("elencoArbitri", this.arbitroService.trovaTutti());
        return "arbitri";
    }

    @GetMapping("/admin/arbitro/nuovo")
    public String formNuovoArbitro(Model model) {
        model.addAttribute("arbitro", new Arbitro());
        return "formNuovoArbitro";
    }

    @PostMapping("/admin/arbitro")
    public String salvaArbitro(@ModelAttribute("arbitro") Arbitro arbitro, Model model) {
        this.arbitroService.salvaArbitro(arbitro);
        return "redirect:/arbitri";
    }

    @GetMapping("/arbitro/{id}")
    public String getArbitro(@PathVariable("id") Long id, Model model) {
        Arbitro arbitro = this.arbitroService.trovaPerId(id);
        model.addAttribute("arbitro", arbitro);
        return "dettaglioArbitro";
    }

    @GetMapping("/admin/arbitro/delete/{id}")
    public String cancellaArbitro(@PathVariable("id") Long id) {
        this.arbitroService.cancellaArbitro(id);
        return "redirect:/arbitri";
    }
}