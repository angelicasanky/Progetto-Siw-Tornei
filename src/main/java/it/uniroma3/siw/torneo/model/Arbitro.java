package it.uniroma3.siw.torneo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Entità JPA che rappresenta un arbitro di una partita.
 * Viene mappata sulla relativa tabella del database tramite {@code @Entity}.
 */
@Entity
public class Arbitro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;
    private String cognome;
    private String codiceArbitrale;

    /** Costruttore vuoto richiesto da JPA per istanziare l'entità durante la lettura dal database. */
    public Arbitro() {
    }

    /**
     * Restituisce l'identificativo univoco dell'arbitro.
     *
     * @return l'ID generato dal database
     */
    public Long getId() {
        return id;
    }

    /**
     * Imposta l'identificativo univoco dell'arbitro.
     *
     * @param id l'ID da assegnare
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce il nome dell'arbitro.
     *
     * @return il nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome dell'arbitro.
     *
     * @param nome il nome da assegnare
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome dell'arbitro.
     *
     * @return il cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome dell'arbitro.
     *
     * @param cognome il cognome da assegnare
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce il codice arbitrale dell'arbitro (identificativo federale).
     *
     * @return il codice arbitrale
     */
    public String getCodiceArbitrale() {
        return codiceArbitrale;
    }

    /**
     * Imposta il codice arbitrale dell'arbitro.
     *
     * @param codiceArbitrale il codice arbitrale da assegnare
     */
    public void setCodiceArbitrale(String codiceArbitrale) {
        this.codiceArbitrale = codiceArbitrale;
    }
}