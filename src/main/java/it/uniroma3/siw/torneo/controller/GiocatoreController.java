package it.uniroma3.siw.torneo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

import it.uniroma3.siw.torneo.model.Giocatore;
import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.service.GiocatoreService;
import it.uniroma3.siw.torneo.service.SquadraService;

@Controller
public class GiocatoreController {

    @Autowired
    private GiocatoreService giocatoreService;

    @Autowired
    private SquadraService squadraService;

    @GetMapping("/giocatori")
    public String mostraGiocatori(Model model) {
        model.addAttribute("elencoGiocatori", giocatoreService.trovaTutti());
        return "giocatori";
    }

    @GetMapping("/admin/giocatore/nuovo")
    public String formNuovoGiocatore(Model model) {
        model.addAttribute("giocatore", new Giocatore());
        model.addAttribute("elencoSquadre", this.squadraService.trovaTutti());
        return "formNuovoGiocatore";
    }

    @PostMapping("/admin/giocatore")
    public String salvaGiocatore(@ModelAttribute("giocatore") Giocatore giocatore,
            @RequestParam(value = "squadraId", required = false) Long squadraId,
            @RequestParam(value = "fileImmagine", required = false) MultipartFile fileImmagine,
            Model model) {

        if (squadraId != null) {
            Squadra squadra = this.squadraService.trovaPerId(squadraId);
            giocatore.setSquadra(squadra);
        }

        try {
            if (fileImmagine != null && !fileImmagine.isEmpty()) {
                String fileName = StringUtils.cleanPath(fileImmagine.getOriginalFilename());
                giocatore.setFoto(fileName);

                this.giocatoreService.salvaGiocatore(giocatore);

                String uploadDir = "uploads/giocatori/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(fileImmagine.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                this.giocatoreService.salvaGiocatore(giocatore);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/giocatori";
    }

    @GetMapping("/giocatore/{id}")
    public String getGiocatore(@PathVariable("id") Long id, Model model) {
        Giocatore giocatore = this.giocatoreService.trovaPerId(id);
        model.addAttribute("giocatore", giocatore);
        return "dettaglioGiocatore";
    }

    @GetMapping("/admin/giocatore/delete/{id}")
    public String cancellaGiocatore(@PathVariable("id") Long id) {
        this.giocatoreService.cancellaGiocatore(id);
        return "redirect:/giocatori";
    }

    @GetMapping("/admin/giocatore/edit/{id}")
    public String editGiocatore(@PathVariable("id") Long id, Model model) {
        model.addAttribute("giocatore", giocatoreService.trovaPerId(id));
        model.addAttribute("elencoSquadre", squadraService.trovaTutti());
        return "formNuovoGiocatore";
    }

    @PostMapping("/admin/giocatore/edit/{id}")
    public String updateGiocatore(@PathVariable("id") Long id,
            @ModelAttribute("giocatore") Giocatore giocatoreDettagli,
            @RequestParam(value = "squadraId", required = false) Long squadraId,
            @RequestParam(value = "fileImmagine", required = false) MultipartFile fileImmagine) {

        Giocatore giocatoreEsistente = giocatoreService.trovaPerId(id);

        if (giocatoreEsistente != null) {
            giocatoreEsistente.setNome(giocatoreDettagli.getNome());
            giocatoreEsistente.setCognome(giocatoreDettagli.getCognome());
            giocatoreEsistente.setDataDiNascita(giocatoreDettagli.getDataDiNascita());
            giocatoreEsistente.setRuolo(giocatoreDettagli.getRuolo());
            giocatoreEsistente.setAltezza(giocatoreDettagli.getAltezza());

            // Gestione della squadra associata
            if (squadraId != null) {
                Squadra squadra = this.squadraService.trovaPerId(squadraId);
                giocatoreEsistente.setSquadra(squadra);
            } else {
                giocatoreEsistente.setSquadra(null);
            }

            try {
                // Se viene caricata una nuova foto, aggiorniamo il file e il nome. Altrimenti
                // teniamo la precedente!
                if (fileImmagine != null && !fileImmagine.isEmpty()) {
                    String fileName = StringUtils.cleanPath(fileImmagine.getOriginalFilename());
                    giocatoreEsistente.setFoto(fileName);

                    String uploadDir = "uploads/giocatori/";
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(fileImmagine.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                giocatoreService.salvaGiocatore(giocatoreEsistente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/giocatore/" + id;
    }
}