package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Rappresenta un'offerta di prezzo fatta da un venditore per un annuncio.
 */
public class Offerta implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String idOfferta;
    private final String idAnnuncio;
    private final String usernameVenditore;
    private final double prezzoAlKg;
    private final double prezzoTotale;
    private final LocalDateTime dataOfferta;
    private String stato; // PENDING, ACCETTATA, RIFIUTATA

    // Costruttore per nuova offerta (genera UUID e timestamp)
    public Offerta(String idAnnuncio, String usernameVenditore, double prezzoAlKg, double prezzoTotale) {
        this.idOfferta = UUID.randomUUID().toString();
        this.idAnnuncio = idAnnuncio;
        this.usernameVenditore = usernameVenditore;
        this.prezzoAlKg = prezzoAlKg;
        this.prezzoTotale = prezzoTotale;
        this.dataOfferta = LocalDateTime.now();
        this.stato = "PENDING"; // Di default in attesa
    }

    // Costruttore per caricare dal database (con ID e data esistenti)
    public Offerta(String idOfferta, String idAnnuncio, String usernameVenditore, 
                   double prezzoAlKg, double prezzoTotale, LocalDateTime dataOfferta, String stato) {
        this.idOfferta = idOfferta;
        this.idAnnuncio = idAnnuncio;
        this.usernameVenditore = usernameVenditore;
        this.prezzoAlKg = prezzoAlKg;
        this.prezzoTotale = prezzoTotale;
        this.dataOfferta = dataOfferta;
        this.stato = stato;
    }

    // Getters
    public String getIdOfferta() {
        return idOfferta;
    }

    public String getIdAnnuncio() {
        return idAnnuncio;
    }

    public String getUsernameVenditore() {
        return usernameVenditore;
    }

    public double getPrezzoAlKg() {
        return prezzoAlKg;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public LocalDateTime getDataOfferta() {
        return dataOfferta;
    }

    public String getStato() {
        return stato;
    }

    // Setter per stato
    public void setStato(String stato) {
        this.stato = stato;
    }

    // Metodi di business
    public boolean isPending() {
        return "PENDING".equals(stato);
    }

    public boolean isAccettata() {
        return "ACCETTATA".equals(stato);
    }

    public boolean isRifiutata() {
        return "RIFIUTATA".equals(stato);
    }

    @Override
    public String toString() {
        return String.format("Offerta[id=%s, annuncio=%s, venditore=%s, prezzoAlKg=%.2f, totale=%.2f, stato=%s]",
                idOfferta, idAnnuncio, usernameVenditore, prezzoAlKg, prezzoTotale, stato);
    }
}
