package main.java.model;


import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

public class Annuncio {

    private final String idAnnuncio;
    private final Agricoltore autore;

    private final String titolo;
    private final String descrizione;
    private double prezzo;
    private int quantitaDesiderata;
    private int quantitaTotale;
    private final LocalDate dataPubblicazione;
    private final LocalDate dataScadenza;

    public Annuncio(
            Agricoltore autore,
            String titolo,
            String descrizione,
            double prezzo,
            int quantitaDesiderata,
            LocalDate dataScadenza) {

        this.idAnnuncio = UUID.randomUUID().toString();
        this.autore = autore;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantitaDesiderata = quantitaDesiderata;
        this.dataPubblicazione = LocalDate.now();
        this.dataScadenza = dataScadenza;
    }

    // ======================
    // Getter
    // ======================

    public String getId() {
        return idAnnuncio;
    }

    public Agricoltore getAutore() {
        return autore;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public int getQuantitaDesiderata() {
        return quantitaDesiderata;
    }

    public LocalDate getDataPubblicazione() {
        return dataPubblicazione;
    }

    public LocalDate getDataScadenza(){ return dataScadenza;}


    // ======================
    // Operazioni di dominio
    // ======================

    public boolean isScaduto() {
        return LocalDate.now().isAfter(this.dataScadenza);
    }


    public void aggiornaQuantitaTotale(List<Partecipazione> partecipazioni) {
        int totale = 0;

        for (Partecipazione p : partecipazioni) {
            totale += p.getQuantitaRichiesta();
        }

        this.quantitaTotale = totale;
    }

}

