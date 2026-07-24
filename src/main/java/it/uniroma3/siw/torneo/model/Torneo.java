package it.uniroma3.siw.torneo.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

// @Entity comunica a JPA e Hibernate che è una tabella sul mio database
@Entity
public class Torneo {

	//@Id Indica che id è la Primary Key
	/*@GeneratedValue(strategy = GenerationType.AUTO) spiega al database come generare l'ID univoco 
		appena dichiarato*/
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String nome;
	private Integer anno;
	private String descrizione;
	
	//creo il collegamento tra tornei e squadra
	@ManyToMany(mappedBy = "tornei")
	private Set<Squadra> squadre = new HashSet<>();
	
	
	/**
	 * @return the squadre
	 */
	public Set<Squadra> getSquadre() {
		return squadre;
	}

	/**
	 * @param squadre the squadre to set
	 */
	public void setSquadre(Set<Squadra> squadre) {
		this.squadre = squadre;
	}

	/*Costruttore vuoto. Quando l'applicazione legge i dati dal database, JPA ha bisogno di creare un 
	oggetto vuoto prima di riempirlo con i dati trovati*/
	public Torneo() {
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
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}
	/**
	 * @param anno the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}
	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
}
