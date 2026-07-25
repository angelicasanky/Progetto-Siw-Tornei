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

	@GetMapping("/squadre")
	public String mostraSquadre(Model model) {
		model.addAttribute("elencoSquadre", this.squadraService.trovaTutti());
		return "squadre";
	}

	@GetMapping("/admin/squadra/nuova")
	public String formNuovaSquadra(Model model) {
		model.addAttribute("squadra", new Squadra());

		// passo anche tutti i tornei esistenti, per farli scegliere nel form
		model.addAttribute("elencoTornei", this.torneoService.trovaTutti());
		return "formNuovaSquadra";
	}

	@PostMapping("/admin/squadra")
	public String salvaSquadra(@ModelAttribute("squadra") Squadra squadra,
			@RequestParam(value = "torneiIds", required = false) List<Long> torneiIds,
			@RequestParam(value = "fileLogo", required = false) MultipartFile fileLogo,
			Model model) {

		// recupero manualmente i tornei selezionati e li assegno alla squadra
		if (torneiIds != null) {
			Set<Torneo> tornei = new HashSet<>();
			for (Long torneoId : torneiIds) {
				Torneo torneo = this.torneoService.trovaPerId(torneoId);
				tornei.add(torneo);
			}
			squadra.setTornei(tornei);
		}

		try {
			// Controllo se è stato caricato un file
			if (fileLogo != null && !fileLogo.isEmpty()) {
				// Pulisco il nome del file per sicurezza
				String fileName = StringUtils.cleanPath(fileLogo.getOriginalFilename());

				// Imposto il nome del file nell'oggetto squadra
				squadra.setLogo(fileName);

				// Salvo la squadra nel database
				this.squadraService.salvaSquadra(squadra);

				// Definisco la cartella di destinazione e la creo se non esiste
				String uploadDir = "uploads/squadre/";
				Path uploadPath = Paths.get(uploadDir);
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}

				// Copio il file fisico nella cartella
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(fileLogo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			} else {
				// Se nessun file è stato caricato, salvo la squadra
				this.squadraService.salvaSquadra(squadra);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/squadre";
	}

	// dettaglio di una singola squadra
	@GetMapping("/squadra/{id}")
	public String getSquadra(@PathVariable("id") Long id, Model model) {
		Squadra squadra = this.squadraService.trovaPerId(id);
		model.addAttribute("squadra", squadra);
		return "dettaglioSquadra";
	}

	@GetMapping("/admin/squadra/delete/{id}")
	public String eliminaSquadra(@PathVariable("id") Long id) {
		this.squadraService.eliminaSquadra(id);
		return "redirect:/squadre";
	}

}
