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

        // recupero manualmente la squadra scelta e la assegno al giocatore
        if (squadraId != null) {
            Squadra squadra = this.squadraService.trovaPerId(squadraId);
            giocatore.setSquadra(squadra);
        }

        try {
            // Controllo se è stata caricata un'immagine
            if (fileImmagine != null && !fileImmagine.isEmpty()) {
                String fileName = StringUtils.cleanPath(fileImmagine.getOriginalFilename());

                // Imposto il nome del file
                giocatore.setFoto(fileName);

                // Salvo il giocatore nel database
                this.giocatoreService.salvaGiocatore(giocatore);

                // Creo la cartella uploads/giocatori/ se non esiste
                String uploadDir = "uploads/giocatori/";
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Salvo il file fisicamente
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(fileImmagine.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            } else {
                // Se non c'è nessuna immagine, salvo semplicemente il giocatore
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
}