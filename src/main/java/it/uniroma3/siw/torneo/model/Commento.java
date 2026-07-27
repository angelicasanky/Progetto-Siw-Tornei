package it.uniroma3.siw.torneo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

/**
 * Entità JPA che rappresenta un commento lasciato da un utente su una partita.
 * Un commento è associato a un singolo {@link Utente} (molti-a-uno) e a una singola {@link Partita} (molti-a-uno).
 */
@Entity
public class Commento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String testo;

    @ManyToOne
    private Utente utente;

    @ManyToOne
    private Partita partita;

    /** Costruttore vuoto richiesto da JPA per istanziare l'entità durante la lettura dal database. */
    public Commento() {

    }

    /**
     * Restituisce l'identificativo univoco del commento.
     *
     * @return l'ID generato dal database
     */
    public Long getId() {
        return id;
    }

    /**
     * Imposta l'identificativo univoco del commento.
     *
     * @param id l'ID da assegnare
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Restituisce il testo del commento.
     *
     * @return il testo del commento
     */
    public String getTesto() {
        return testo;
    }

    /**
     * Imposta il testo del commento.
     *
     * @param testo il testo da assegnare
     */
    public void setTesto(String testo) {
        this.testo = testo;
    }

    /**
     * Restituisce l'utente autore del commento.
     *
     * @return l'utente che ha scritto il commento
     */
    public Utente getUtente() {
        return utente;
    }

    /**
     * Imposta l'utente autore del commento.
     *
     * @param utente l'utente da associare al commento
     */
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    /**
     * Restituisce la partita a cui è associato il commento.
     *
     * @return la partita commentata
     */
    public Partita getPartita() {
        return partita;
    }

    /**
     * Imposta la partita a cui associare il commento.
     *
     * @param partita la partita da associare
     */
    public void setPartita(Partita partita) {
        this.partita = partita;
    }

}
