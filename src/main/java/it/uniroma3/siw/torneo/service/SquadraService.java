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

	// salva per ID
	@Transactional
	public Squadra salvaSquadra(Squadra squadra) {
		return this.squadraRepository.save(squadra);
	}

	// trova per ID
	@Transactional
	public Squadra trovaPerId(Long id) {
		return this.squadraRepository.findById(id).orElse(null);
	}

	// trovaTutti
	@Transactional
	public Iterable<Squadra> trovaTutti() {
		return this.squadraRepository.findAll();
	}

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
