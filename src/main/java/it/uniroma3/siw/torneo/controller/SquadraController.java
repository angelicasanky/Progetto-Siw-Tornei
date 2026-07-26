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
		model.addAttribute("elencoTornei", this.torneoService.trovaTutti());
		return "formNuovaSquadra";
	}

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

	@GetMapping("/admin/squadra/edit/{id}")
	public String editSquadra(@PathVariable("id") Long id, Model model) {
		Squadra squadra = squadraService.trovaPerId(id);
		model.addAttribute("squadra", squadra);
		model.addAttribute("elencoTornei", this.torneoService.trovaTutti());
		return "formNuovaSquadra";
	}

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

	private void squadresistenteTorneiClear(Squadra s) {
		if (s.getTornei() != null) {
			s.getTornei().clear();
		}
	}
}