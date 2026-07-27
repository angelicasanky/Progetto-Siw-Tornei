package it.uniroma3.siw.torneo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.repository.SquadraRepository;

/**
 * Analisi sperimentale (Sezione 8.2 del progetto).
 *
 * Caso d'uso: caricare tutte le squadre e, per ciascuna, i giocatori.
 *
 * Confronta due strategie di accesso ai dati:
 * - LAZY (default per @OneToMany): quando accedo a squadra.getGiocatori()
 * Hibernate emette una query in più per ogni squadra -> problema N+1.
 * - JOIN FETCH: un'unica query che carica insieme squadre e giocatori.
 */

@Controller
public class AnalisiFetchController {

    @Autowired
    private SquadraRepository squadraRepository;

    @GetMapping("/admin/analisi-fetch")
    @Transactional(readOnly = true)
    public String analisi(Model model) {

        // ============ STRATEGIA 1: LAZY (default) ============
        // findAll() non fa nessun join: ogni accesso a getGiocatori() di
        // una squadra diversa scatena una query SQL aggiuntiva -> N+1.
        long t0 = System.nanoTime();
        List<Squadra> listaLazy = (List<Squadra>) this.squadraRepository.findAll();
        int giocatoriContatiLazy = 0;
        for (Squadra s : listaLazy) {
            // Questo accesso forza il caricamento lazy della collezione:
            // ogni squadra diversa genera una query aggiuntiva al database.
            giocatoriContatiLazy += s.getGiocatori().size();
        }
        long t1 = System.nanoTime();
        double lazyMs = (t1 - t0) / 1_000_000.0;

        // ============ STRATEGIA 2: JOIN FETCH ============
        // Un'unica query SQL carica squadre e giocatori insieme.
        long t2 = System.nanoTime();
        List<Squadra> listaJoin = this.squadraRepository.trovaTutteConGiocatoriJoinFetch();
        int giocatoriContatiJoin = 0;
        for (Squadra s : listaJoin) {
            giocatoriContatiJoin += s.getGiocatori().size();
        }
        long t3 = System.nanoTime();
        double joinMs = (t3 - t2) / 1_000_000.0;

        // ============ RIEPILOGO ============
        int numSquadre = listaLazy.size();
        model.addAttribute("numSquadre", numSquadre);
        model.addAttribute("giocatoriContatiLazy", giocatoriContatiLazy);
        model.addAttribute("giocatoriContatiJoin", giocatoriContatiJoin);
        model.addAttribute("lazyMs", String.format("%.2f", lazyMs));
        model.addAttribute("joinMs", String.format("%.2f", joinMs));
        model.addAttribute("lazyQueryStimate", 1 + numSquadre);
        model.addAttribute("joinQueryStimate", 1);

        return "analisiFetch";
    }
}
