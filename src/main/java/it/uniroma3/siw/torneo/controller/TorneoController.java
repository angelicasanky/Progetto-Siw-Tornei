package it.uniroma3.siw.torneo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.torneo.model.Torneo;
import it.uniroma3.siw.torneo.service.ClassificaService;
import it.uniroma3.siw.torneo.service.TorneoService;

@Controller
public class TorneoController {

	@Autowired
	private TorneoService torneoService;

	@Autowired
	private ClassificaService classificaService;

	/**
	 * Gestisce la richiesta Get all'indirizzo "/tornei"
	 * Recupera l'elenco completo dei tornei dal database tramite il Service
	 * e lo inserisce nel Model per renderlo accessibile alla pagina web
	 * 
	 * @param model utilizzato per passare i dati dal backend al frontend
	 * @return La pagina html da visualizzare
	 */
	@GetMapping("/tornei")
	public String mostraTornei(Model model) {
		model.addAttribute("elencoTornei", this.torneoService.trovaTutti());
		return "tornei";

	}

	/**
	 * Gestisce la richiesta Get all'indirizzo /admin/torneo/nuovo
	 * Mostra la pagina con il modulo per creare un nuovo torneo
	 * 
	 * @param model prepara un oggetto Torneo vuoto e lo passa alla pagina html
	 * @return la pagina html da visualizzare ("formNuovoTorneo.html")
	 */
	@GetMapping("/admin/torneo/nuovo")
	public String formNuovoTorneo(Model model) {
		model.addAttribute("torneo", new Torneo());
		return "formNuovoTorneo";
	}

	/**
	 * Metodo POST che riceve i dati inviati dal form html e li salva
	 * 
	 * @param torneo, L'oggetto creato da Spring unendo i dati che l'utente ha messo
	 *                nel form
	 * @param model,  Il Model per passare eventuali messaggi alla vista successiva
	 * @return redirect che riporta alla pagina della lista tornei dopo aver
	 *         effettuato il salvataggio
	 */
	@PostMapping("/admin/torneo")
	public String salvaTorneo(@ModelAttribute("torneo") Torneo torneo, Model model) {
		this.torneoService.salvaTorneo(torneo);
		return "redirect:/tornei";
	}

	/**
	 * Metodo GET che mostra i dettagli di un singolo torneo
	 * 
	 * @param id,    L'identificativo univoco del torneo
	 * @param model, Il contenitore per passare l'oggetto Torneo trovato alla view
	 * @return il nome del file html che mostrerà i dettagli (torneo.html)
	 */
	@GetMapping("/torneo/{id}")
	public String getTorneo(@PathVariable("id") Long id, Model model) {

		// chiedo al Service di cercare nel database il torneo con questo specifico id
		Torneo torneo = this.torneoService.trovaPerId(id);

		model.addAttribute("torneo", torneo);

		return "dettaglioTorneo";
	}

	@GetMapping("/admin/torneo/delete/{id}")
	public String cancellaTorneo(@PathVariable("id") Long id) {
		this.torneoService.cancellaTorneo(id);
		return "redirect:/tornei";
	}

	@GetMapping("/torneo/{id}/classifica")
	public String mostraClassifica(@PathVariable("id") Long id, Model model) {
		Torneo torneo = this.torneoService.trovaPerId(id);
		model.addAttribute("torneo", torneo);
		model.addAttribute("classifica", this.classificaService.calcolaClassifica(id));
		return "classificaTorneo";
	}

}
