package model;


import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

public class Annuncio {

    private final String idAnnuncio;
    private final String nomeAutore;

    private final String titolo;
    private final String descrizione;
    private double prezzo;  //xecec
    private int quantitaDesiderata;
    private int quantitaTotale;
    private final LocalDate dataPubblicazione;
    private final LocalDate dataScadenza;
    private final String citta;


    public Annuncio(
            String nomeAutore,
            String titolo,
            String descrizione,
            LocalDate dataPubblicazione,
            String nomeProdotto,
            int quantitaDesiderata,
            String citta,
            LocalDate dataScadenza) {

        this.idAnnuncio = UUID.randomUUID().toString();
        this.nomeAutore = nomeAutore;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantitaDesiderata = quantitaDesiderata;
        this.dataPubblicazione = LocalDate.now();
        this.dataScadenza = dataScadenza;
        this.citta = citta;
    }

    // ======================
    // Getter
    // ======================

    public String getId() {
        return idAnnuncio;
    }
    
    public String getIdAnnuncio() {
        return idAnnuncio;
    }

    public String getAutore() {
        return nomeAutore;
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
    
    public LocalDate getDataCreazione() {
        return dataPubblicazione;
    }
    
    public int getQuantita() {
        return quantitaDesiderata;
    }
    
    public String getCitta() {
        return citta;
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

