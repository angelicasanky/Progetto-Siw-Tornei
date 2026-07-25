package it.uniroma3.siw.torneo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Giocatore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private String cognome;
    private LocalDate dataDiNascita;
    private String ruolo;
    private Integer altezza;
    private String foto;

    @ManyToOne
    private Squadra squadra;

    public Giocatore() {
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
     * @return the cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * @param cognome the cognome to set
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * @return the dataDiNascita
     */
    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    /**
     * @param dataDiNascita the dataDiNascita to set
     */
    public void setDataDiNascita(LocalDate dataDiNascita) {
        this.dataDiNascita = dataDiNascita;
    }

    /**
     * @return the ruolo
     */
    public String getRuolo() {
        return ruolo;
    }

    /**
     * @param ruolo the ruolo to set
     */
    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }

    /**
     * @return the altezza
     */
    public Integer getAltezza() {
        return altezza;
    }

    /**
     * @param altezza the altezza to set
     */
    public void setAltezza(Integer altezza) {
        this.altezza = altezza;
    }

    /**
     * @return the squadra
     */
    public Squadra getSquadra() {
        return squadra;
    }

    /**
     * @param squadra the squadra to set
     */
    public void setSquadra(Squadra squadra) {
        this.squadra = squadra;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

}
