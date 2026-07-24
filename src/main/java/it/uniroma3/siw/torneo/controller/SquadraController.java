package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.service.SquadraService;

@Controller
public class SquadraController {
	
	
	@Autowired 
	private SquadraService squadraService;
	
	@Autowired
	private TorneoService torneoService;
	
	@GetMapping("/squadre")
	public String mostraSquadre(Model model) {
		model.addAttribute("elencoSquadre", this.squadraService.trovaTutti());
		return "squadre.html";
	}
	
	@GetMapping("/admin/squadra/nuova")
	public String formNuovaSquadra(Model model) {
		model.addAttribute("squadra", new Squadra());
		
		//passo anche tutti i tornei esistenti, per farli scegliere nel form
		model.addAttribute("elencoTornei", this.torneoService.trovaTutti());
		return "formNuovaSquadra.html";
	}
	
	@PostMapping("/admin/squadra")
	public String salvaSquadra(@ModelAttribute("squadra") Squadra squadra, Model model) {
		this.squadraService.salvaSquadra(squadra);
		return "redirect:/squadre";
	}
	
	//dettaglio di una singola squadra
	@GetMapping("/squadra/{id}")
	public String getSquadra(@PathVariable("id") Long id, Model model) {
		Squadra squadra = this.squadraService.trovaPerId(id);
		model.addAttribute("squadra", squadra);
		return "dettaglioSquadra.html";
	}
	
	@GetMapping("/admin/squadra/delete/{id}")
	public String cancellaSquadra(@PathVariable("id") Long id) {
		this.squadraService.cancellaSquadra(id);
		return "redirect:/squadre";
	}
	

}
