package it.uniroma3.siw.torneo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.torneo.model.RigaClassifica;
import it.uniroma3.siw.torneo.model.Torneo;
import it.uniroma3.siw.torneo.service.ClassificaService;
import it.uniroma3.siw.torneo.service.PartitaService;
import it.uniroma3.siw.torneo.service.TorneoService;

@Controller
public class TorneoController {

	@Autowired
	private TorneoService torneoService;

	@Autowired
	private ClassificaService classificaService;

	@Autowired
	private PartitaService partitaService;

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
	public String salvaTorneo(@ModelAttribute("torneo") Torneo torneo,
			@RequestParam(value = "fileImmagine", required = false) MultipartFile fileImmagine,
			Model model) {

		try {
			if (fileImmagine != null && !fileImmagine.isEmpty()) {
				String fileName = StringUtils.cleanPath(fileImmagine.getOriginalFilename());
				torneo.setImmagine(fileName);

				// Salvo il torneo nel database
				this.torneoService.salvaTorneo(torneo); // o il nome del metodo che usi nel tuo service

				// Creo la cartella uploads/tornei/ se non esiste
				String uploadDir = "uploads/tornei/";
				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				// Salvo il file fisicamente
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(fileImmagine.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			} else {
				// Se non c'è nessuna immagine, salvo semplicemente il torneo
				this.torneoService.salvaTorneo(torneo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		Torneo torneo = this.torneoService.trovaPerId(id);
		model.addAttribute("torneo", torneo);
		model.addAttribute("partiteTorneo", this.partitaService.trovaPerTorneo(id));
		return "dettaglioTorneo";
	}

	/**
	 * Gestisce la richiesta GET su "/admin/torneo/delete/{id}" (solo ADMIN).
	 * Elimina il torneo con l'ID specificato, scollegando prima le squadre associate,
	 * e reindirizza alla lista dei tornei.
	 *
	 * @param id l'identificativo univoco del torneo da eliminare
	 * @return un redirect verso la lista dei tornei
	 */
	@GetMapping("/admin/torneo/delete/{id}")
	public String cancellaTorneo(@PathVariable("id") Long id) {
		this.torneoService.eliminaTorneo(id);
		return "redirect:/tornei";
	}

	/**
	 * Gestisce la richiesta GET su "/torneo/{id}/classifica".
	 * Calcola la classifica del torneo specificato e la visualizza nella vista Thymeleaf.
	 * La classifica è ordinata per punti decrescenti e, a parità, per differenza reti.
	 *
	 * @param id    l'identificativo univoco del torneo
	 * @param model il Model di Spring MVC utilizzato per trasferire torneo e classifica alla vista
	 * @return il nome della vista Thymeleaf "classificaTorneo"
	 */
	@GetMapping("/torneo/{id}/classifica")
	public String mostraClassifica(@PathVariable("id") Long id, Model model) {
		Torneo torneo = this.torneoService.trovaPerId(id);
		model.addAttribute("torneo", torneo);
		model.addAttribute("classifica", this.classificaService.calcolaClassifica(id));
		return "classificaTorneo";
	}

	/**
	 * Gestisce la richiesta GET su "/torneo/{id}/classifica-react".
	 * Prepara la pagina che ospita il componente React per la visualizzazione dinamica
	 * della classifica. L'ID del torneo viene passato alla pagina affinché il componente
	 * React possa recuperare i dati tramite l'endpoint REST {@code /api/torneo/{id}/classifica}.
	 *
	 * @param id    l'identificativo univoco del torneo
	 * @param model il Model di Spring MVC utilizzato per passare l'ID del torneo alla vista
	 * @return il nome della vista Thymeleaf "classificaReact"
	 */
	@GetMapping("/torneo/{id}/classifica-react")
	public String mostraClassificaReact(@PathVariable("id") Long id, Model model) {
		model.addAttribute("torneoId", id);
		return "classificaReact";
	}

	/**
	 * Gestisce la richiesta GET su "/admin/torneo/edit/{id}" (solo ADMIN).
	 * Carica i dati del torneo esistente per prepopolare il form di modifica.
	 *
	 * @param id    l'identificativo univoco del torneo da modificare
	 * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
	 * @return il nome della vista Thymeleaf "formNuovoTorneo" (riutilizzata per la modifica)
	 */
	@GetMapping("/admin/torneo/edit/{id}")
	public String editTorneo(@PathVariable("id") Long id, Model model) {
		model.addAttribute("torneo", torneoService.trovaPerId(id));
		return "formNuovoTorneo";
	}

	/**
	 * Gestisce la richiesta POST su "/admin/torneo/edit/{id}" (solo ADMIN).
	 * Aggiorna i dati del torneo esistente con quelli provenienti dal form e salva le modifiche.
	 *
	 * @param id              l'identificativo univoco del torneo da aggiornare
	 * @param torneoDettagli  l'oggetto con i nuovi dati provenienti dal form
	 * @return un redirect verso la pagina di dettaglio del torneo aggiornato
	 */
	@PostMapping("/admin/torneo/edit/{id}")
	public String updateTorneo(@PathVariable("id") Long id, @ModelAttribute("torneo") Torneo torneoDettagli) {
		Torneo torneoEsistente = torneoService.trovaPerId(id);
		if (torneoEsistente != null) {
			torneoEsistente.setNome(torneoDettagli.getNome());
			// setta gli altri campi del torneo...
			torneoService.salvaTorneo(torneoEsistente);
		}
		return "redirect:/torneo/" + id;
	}

}
