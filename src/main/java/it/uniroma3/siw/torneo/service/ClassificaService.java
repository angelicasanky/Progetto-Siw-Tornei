package it.uniroma3.siw.torneo.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.model.RigaClassifica;
import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.model.StatoPartita;
import it.uniroma3.siw.torneo.repository.PartitaRepository;
import jakarta.transaction.Transactional;

@Service
public class ClassificaService {
    @Autowired
    private PartitaRepository partitaRepository;

    /**
     * Calcola la classifica di un torneo considerando solo le partite già giocate
     * (con stato {@link StatoPartita#PLAYED}).
     * <p>
     * Per ogni partita vengono aggiornati vittorie, sconfitte, pareggi, gol fatti e subiti
     * per entrambe le squadre. Il punteggio segue la regola standard:
     * 3 punti per la vittoria, 1 per il pareggio, 0 per la sconfitta.
     * </p>
     * La classifica risultante è ordinata per punti decrescenti; a parità di punti
     * viene usata la differenza reti come criterio di spareggio.
     *
     * @param torneoId l'identificativo univoco del torneo di cui calcolare la classifica
     * @return la lista di {@link RigaClassifica} ordinata dalla prima all'ultima posizione
     */
    @Transactional
    public List<RigaClassifica> calcolaClassifica(Long torneoId) {
        List<Partita> partiteGiocate = this.partitaRepository.findByTorneoIdAndStato(torneoId, StatoPartita.PLAYED);

        /* Mappa temporanea. Creo una riga di classifica per ogni squadra incontrata */
        Map<Squadra, RigaClassifica> mappa = new HashMap<>();

        for (Partita partita : partiteGiocate) {
            if (partita.getGoalsHome() == null || partita.getGoalsAway() == null) {
                continue;
            }

            Squadra casa = partita.getSquadraCasa();
            Squadra ospite = partita.getSquadraOspite();
            int golCasa = partita.getGoalsHome();
            int golOspite = partita.getGoalsAway();

            // recupero (o creo se non esiste ancora) la riga di classifica per ciascuna
            // squadra
            RigaClassifica rigaCasa = mappa.computeIfAbsent(casa, RigaClassifica::new);
            RigaClassifica rigaOspite = mappa.computeIfAbsent(ospite, RigaClassifica::new);

            rigaCasa.setPartiteGiocate(rigaCasa.getPartiteGiocate() + 1);
            rigaOspite.setPartiteGiocate(rigaOspite.getPartiteGiocate() + 1);

            rigaCasa.setGolFatti(rigaCasa.getGolFatti() + golCasa);
            rigaCasa.setGolSubiti(rigaCasa.getGolSubiti() + golOspite);
            rigaOspite.setGolFatti(rigaOspite.getGolFatti() + golOspite);
            rigaOspite.setGolSubiti(rigaOspite.getGolSubiti() + golCasa);

            if (golCasa > golOspite) {
                // vittoria squadra di casa
                rigaCasa.setVittorie(rigaCasa.getVittorie() + 1);
                rigaCasa.setPunti(rigaCasa.getPunti() + 3);
                rigaOspite.setSconfitte(rigaOspite.getSconfitte() + 1);
            } else if (golCasa < golOspite) {
                // vittoria squadra ospite
                rigaOspite.setVittorie(rigaOspite.getVittorie() + 1);
                rigaOspite.setPunti(rigaOspite.getPunti() + 3);
                rigaCasa.setSconfitte(rigaCasa.getSconfitte() + 1);
            } else {
                // pareggio
                rigaCasa.setPareggi(rigaCasa.getPareggi() + 1);
                rigaCasa.setPunti(rigaCasa.getPunti() + 1);
                rigaOspite.setPareggi(rigaOspite.getPareggi() + 1);
                rigaOspite.setPunti(rigaOspite.getPunti() + 1);
            }
        }

        // trasformo la mappa in lista e ordino per punti decrescenti,
        // a parità di punti per differenza reti decrescente
        List<RigaClassifica> classifica = new ArrayList<>(mappa.values());
        classifica.sort(
                Comparator.comparingInt(RigaClassifica::getPunti).reversed()
                        .thenComparing(Comparator.comparingInt(RigaClassifica::getDifferenzaReti).reversed()));

        return classifica;
    }
}
