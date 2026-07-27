package it.uniroma3.siw.torneo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.torneo.model.Squadra;
import it.uniroma3.siw.torneo.model.Torneo;
import it.uniroma3.siw.torneo.repository.TorneoRepository;
import jakarta.transaction.Transactional;

/*@Service segnala a Spring che questa classe fa parte del Service Layer*/
@Service
public class TorneoService {

	@Autowired
	private TorneoRepository torneoRepository;

	/**
	 * Persiste un torneo nel database (inserimento o aggiornamento).
	 *
	 * @param torneo l'entità Torneo da salvare
	 * @return l'entità Torneo salvata, con l'ID generato dal database
	 */
	@Transactional
	public Torneo salvaTorneo(Torneo torneo) {
		return this.torneoRepository.save(torneo);
	}

	/**
	 * Recupera un torneo dal database tramite il suo ID.
	 *
	 * @param id l'identificativo univoco del torneo
	 * @return il torneo trovato, oppure {@code null} se non esiste
	 */
	@Transactional
	public Torneo trovaPerId(Long id) {
		return this.torneoRepository.findById(id).orElse(null);
	}

	/**
	 * Recupera l'elenco completo di tutti i tornei presenti nel database.
	 *
	 * @return un {@link Iterable} contenente tutti i tornei
	 */
	@Transactional
	public Iterable<Torneo> trovaTutti() {
		return this.torneoRepository.findAll();
	}

	/**
	 * Elimina un torneo dal database in modo sicuro:
	 * prima rimuove il torneo dalla lista dei tornei di ogni squadra partecipante
	 * (per mantenere la coerenza della relazione ManyToMany), poi lo cancella.
	 *
	 * @param id l'identificativo univoco del torneo da eliminare
	 */
	@Transactional
	public void eliminaTorneo(Long id) {
		Torneo torneo = this.torneoRepository.findById(id).orElse(null);
		if (torneo != null) {
			for (Squadra squadra : torneo.getSquadre()) {
				squadra.getTornei().remove(torneo);
			}
			this.torneoRepository.delete(torneo);
		}
	}

}
