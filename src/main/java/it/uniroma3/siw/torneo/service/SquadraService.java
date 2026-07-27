package it.uniroma3.siw.torneo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Giocatore;
import it.uniroma3.siw.torneo.model.Partita;
import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.repository.SquadraRepository;
import jakarta.transaction.Transactional;

@Service
public class SquadraService {

	@Autowired
	private SquadraRepository squadraRepository;

	/**
	 * Persiste una squadra nel database (inserimento o aggiornamento).
	 *
	 * @param squadra l'entità Squadra da salvare
	 * @return l'entità Squadra salvata, con l'ID generato dal database
	 */
	// salva per ID
	@Transactional
	public Squadra salvaSquadra(Squadra squadra) {
		return this.squadraRepository.save(squadra);
	}

	/**
	 * Recupera una squadra dal database tramite il suo ID.
	 *
	 * @param id l'identificativo univoco della squadra
	 * @return la squadra trovata, oppure {@code null} se non esiste
	 */
	// trova per ID
	@Transactional
	public Squadra trovaPerId(Long id) {
		return this.squadraRepository.findById(id).orElse(null);
	}

	/**
	 * Recupera l'elenco completo di tutte le squadre presenti nel database.
	 *
	 * @return un {@link Iterable} contenente tutte le squadre
	 */
	// trovaTutti
	@Transactional
	public Iterable<Squadra> trovaTutti() {
		return this.squadraRepository.findAll();
	}

	/**
	 * Elimina una squadra dal database in modo sicuro:
	 * prima scollega tutti i giocatori associati (impostando la loro squadra a {@code null}),
	 * poi pulisce la tabella ponte dei tornei e infine cancella la squadra.
	 * In questo modo si evitano violazioni di integrità referenziale.
	 *
	 * @param id l'identificativo univoco della squadra da eliminare
	 */
	@Transactional
	public void eliminaSquadra(Long id) {
		Squadra squadra = this.squadraRepository.findById(id).orElse(null);

		if (squadra != null) {
			// 1. Scollega i giocatori associati
			if (squadra.getGiocatori() != null) {
				for (Giocatore g : squadra.getGiocatori()) {
					g.setSquadra(null);
				}
			}

			// 2. Pulisce la tabella ponte dei tornei
			if (squadra.getTornei() != null) {
				squadra.getTornei().clear();
			}

			// 3. Elimina la squadra in sicurezza
			this.squadraRepository.delete(squadra);
		}
	}
}
