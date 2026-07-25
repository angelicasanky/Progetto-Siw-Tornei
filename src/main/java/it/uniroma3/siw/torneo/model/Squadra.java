package it.uniroma3.siw.torneo.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.util.List;
import java.util.ArrayList;
import jakarta.persistence.OneToMany;

@Entity
public class Squadra {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nome;
	private Integer annoDiFondazione;
	private String citta;
	private String logo;

	// Aggiungo la relazione con Torneo
	@ManyToMany
	@JoinTable( // dice a Hibernate di creare una terza tabella chiamata squadra_torneo
			name = "squadra_torneo", joinColumns = @JoinColumn(name = "squadra_id"), inverseJoinColumns = @JoinColumn(name = "torneo_id"))

	// uso Set invece di List per relazioni @ManyToMany perchè evita duplicati
	private Set<Torneo> tornei = new HashSet<>();

	// Aggiungo la relazione con Giocatore
	@OneToMany(mappedBy = "squadra")
	@JsonIgnore
	private List<Giocatore> giocatori = new ArrayList<>();

	public Squadra() {
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the annoDiFondazione
	 */
	public Integer getAnnoDiFondazione() {
		return annoDiFondazione;
	}

	/**
	 * @param annoDiFondazione the annoDiFondazione to set
	 */
	public void setAnnoDiFondazione(Integer annoDiFondazione) {
		this.annoDiFondazione = annoDiFondazione;
	}

	/**
	 * @return the città
	 */
	public String getCitta() {
		return citta;
	}

	/**
	 * @param città the città to set
	 */
	public void setCitta(String citta) {
		this.citta = citta;
	}

	/**
	 * @return the tornei
	 */
	public Set<Torneo> getTornei() {
		return tornei;
	}

	/**
	 * @param tornei the tornei to set
	 */
	public void setTornei(Set<Torneo> tornei) {
		this.tornei = tornei;
	}

	/**
	 * @return the giocatori
	 */
	public List<Giocatore> getGiocatori() {
		return giocatori;
	}

	/**
	 * @param giocatori the giocatori to set
	 */
	public void setGiocatori(List<Giocatore> giocatori) {
		this.giocatori = giocatori;
	}

	/**
	 * @return the logo
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

}
