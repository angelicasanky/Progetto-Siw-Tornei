package it.uniroma3.siw.torneo.controller;

import it.uniroma3.siw.torneo.service.TorneoService;

import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.model.Torneo;
import it.uniroma3.siw.torneo.service.SquadraService;

@Controller
public class SquadraController {

	@Autowired
	private SquadraService squadraService;

	@Autowired
	private TorneoService torneoService;

	/**
	 * Gestisce la richiesta GET su "/squadre".
	 * Recupera l'elenco di tutte le squadre e lo passa alla vista.
	 *
	 * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
	 * @return il nome della vista Thymeleaf "squadre"
	 */
	@GetMapping("/squadre")
	public String mostraSquadre(Model model) {
		model.addAttribute("elencoSquadre", this.squadraService.trovaTutti());
		return "squadre";
	}

	/**
	 * Gestisce la richiesta GET su "/admin/squadra/nuova" (solo ADMIN).
	 * Prepara un oggetto Squadra vuoto e l'elenco dei tornei disponibili per il form di creazione.
	 *
	 * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
	 * @return il nome della vista Thymeleaf "formNuovaSquadra"
	 */
	@GetMapping("/admin/squadra/nuova")
	public String formNuovaSquadra(Model model) {
		model.addAttribute("squadra", new Squadra());
		model.addAttribute("elencoTornei", this.torneoService.trovaTutti());
		return "formNuovaSquadra";
	}

	/**
	 * Gestisce la richiesta POST su "/admin/squadra" (solo ADMIN).
	 * Salva una nuova squadra nel database, associandola ai tornei selezionati.
	 * Se viene fornito un file logo, lo salva fisicamente nella cartella "uploads/squadre/".
	 *
	 * @param squadra   l'oggetto Squadra popolato con i dati del form
	 * @param torneiIds la lista degli ID dei tornei a cui associare la squadra (opzionale)
	 * @param fileLogo  il file immagine del logo della squadra caricato dall'utente (opzionale)
	 * @param model     il Model di Spring MVC (disponibile per eventuali estensioni)
	 * @return un redirect verso la lista delle squadre
	 */
	@PostMapping("/admin/squadra")
	public String salvaSquadra(@ModelAttribute("squadra") Squadra squadra,
			@RequestParam(value = "torneiIds", required = false) List<Long> torneiIds,
			@RequestParam(value = "fileLogo", required = false) MultipartFile fileLogo,
			Model model) {

		if (torneiIds != null) {
			Set<Torneo> tornei = new HashSet<>();
			for (Long torneoId : torneiIds) {
				Torneo torneo = this.torneoService.trovaPerId(torneoId);
				tornei.add(torneo);
			}
			squadra.setTornei(tornei);
		}

		try {
			if (fileLogo != null && !fileLogo.isEmpty()) {
				String fileName = StringUtils.cleanPath(fileLogo.getOriginalFilename());
				squadra.setLogo(fileName);
				this.squadraService.salvaSquadra(squadra);

				String uploadDir = "uploads/squadre/";
				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				Path filePath = uploadPath.resolve(fileName);
				Files.copy(fileLogo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			} else {
				this.squadraService.salvaSquadra(squadra);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/squadre";
	}

	/**
	 * Gestisce la richiesta GET su "/squadra/{id}".
	 * Recupera il dettaglio di una singola squadra tramite il suo ID e lo passa alla vista.
	 *
	 * @param id    l'identificativo univoco della squadra da visualizzare
	 * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
	 * @return il nome della vista Thymeleaf "dettaglioSquadra"
	 */
	@GetMapping("/squadra/{id}")
	public String getSquadra(@PathVariable("id") Long id, Model model) {
		Squadra squadra = this.squadraService.trovaPerId(id);
		model.addAttribute("squadra", squadra);
		return "dettaglioSquadra";
	}

	/**
	 * Gestisce la richiesta GET su "/admin/squadra/delete/{id}" (solo ADMIN).
	 * Elimina la squadra con l'ID specificato e reindirizza alla lista.
	 *
	 * @param id l'identificativo univoco della squadra da eliminare
	 * @return un redirect verso la lista delle squadre
	 */
	@GetMapping("/admin/squadra/delete/{id}")
	public String eliminaSquadra(@PathVariable("id") Long id) {
		this.squadraService.eliminaSquadra(id);
		return "redirect:/squadre";
	}

	/**
	 * Gestisce la richiesta GET su "/admin/squadra/edit/{id}" (solo ADMIN).
	 * Carica i dati della squadra esistente e l'elenco dei tornei per prepopolare il form di modifica.
	 *
	 * @param id    l'identificativo univoco della squadra da modificare
	 * @param model il Model di Spring MVC utilizzato per trasferire i dati alla vista
	 * @return il nome della vista Thymeleaf "formNuovaSquadra" (riutilizzata per la modifica)
	 */
	@GetMapping("/admin/squadra/edit/{id}")
	public String editSquadra(@PathVariable("id") Long id, Model model) {
		Squadra squadra = squadraService.trovaPerId(id);
		model.addAttribute("squadra", squadra);
		model.addAttribute("elencoTornei", this.torneoService.trovaTutti());
		return "formNuovaSquadra";
	}

	/**
	 * Gestisce la richiesta POST su "/admin/squadra/edit/{id}" (solo ADMIN).
	 * Aggiorna nome, anno di fondazione, città, tornei associati e logo di una squadra esistente.
	 * Se viene caricato un nuovo logo, sovrascrive quello precedente su disco.
	 *
	 * @param id             l'identificativo univoco della squadra da aggiornare
	 * @param squadraDettagli l'oggetto con i nuovi dati provenienti dal form
	 * @param torneiIds      la lista degli ID dei tornei aggiornati (opzionale; null per rimuovere tutti)
	 * @param fileLogo       il nuovo file logo da caricare (opzionale; se assente si mantiene il logo attuale)
	 * @param model          il Model di Spring MVC (disponibile per eventuali estensioni)
	 * @return un redirect verso la pagina di dettaglio della squadra aggiornata
	 */
	@PostMapping("/admin/squadra/edit/{id}")
	public String updateSquadra(@PathVariable("id") Long id,
			@ModelAttribute("squadra") Squadra squadraDettagli,
			@RequestParam(value = "torneiIds", required = false) List<Long> torneiIds,
			@RequestParam(value = "fileLogo", required = false) MultipartFile fileLogo,
			Model model) {

		Squadra squadraEsistente = squadraService.trovaPerId(id);

		if (squadraEsistente != null) {
			squadraEsistente.setNome(squadraDettagli.getNome());
			squadraEsistente.setAnnoDiFondazione(squadraDettagli.getAnnoDiFondazione());
			squadraEsistente.setCitta(squadraDettagli.getCitta());

			// Aggiorna i tornei selezionati
			if (torneiIds != null) {
				Set<Torneo> tornei = new HashSet<>();
				for (Long torneoId : torneiIds) {
					Torneo torneo = this.torneoService.trovaPerId(torneoId);
					tornei.add(torneo);
				}
				squadraEsistente.setTornei(tornei);
			} else {
				squadresistenteTorneiClear(squadraEsistente);
			}

			try {
				// Se viene caricata una nuova foto, la aggiorniamo. Altrimenti teniamo la
				// precedente!
				if (fileLogo != null && !fileLogo.isEmpty()) {
					String fileName = StringUtils.cleanPath(fileLogo.getOriginalFilename());
					squadraEsistente.setLogo(fileName);

					String uploadDir = "uploads/squadre/";
					Path uploadPath = Paths.get(uploadDir);
					if (!Files.exists(uploadPath)) {
						Files.createDirectories(uploadPath);
					}

					Path filePath = uploadPath.resolve(fileName);
					Files.copy(fileLogo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
				}

				squadraService.salvaSquadra(squadraEsistente);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "redirect:/squadra/" + id;
	}

	/**
	 * Metodo privato di utilità.
	 * Svuota la lista dei tornei associati alla squadra, se presente.
	 * Utilizzato quando durante la modifica non viene selezionato nessun torneo.
	 *
	 * @param s la squadra di cui cancellare le associazioni con i tornei
	 */
	private void squadresistenteTorneiClear(Squadra s) {
		if (s.getTornei() != null) {
			s.getTornei().clear();
		}
	}
}