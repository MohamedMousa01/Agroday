package model;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import java.util.List;

public class Annuncio implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String stato; // ATTIVO, SCADUTO


    // Costruttore per creare un nuovo annuncio (genera UUID)
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
        this.prezzo = 0.0;
        this.quantitaDesiderata = quantitaDesiderata;
        this.dataPubblicazione = LocalDate.now();
        this.dataScadenza = dataScadenza;
        this.citta = citta;
        this.stato = "ATTIVO"; // Inizialmente attivo
        this.quantitaTotale = quantitaDesiderata; // Inizialmente solo la quantità dell'autore
    }

    // Costruttore per caricare un annuncio esistente (con ID dal database)
    public Annuncio(
            String idAnnuncio,
            String nomeAutore,
            String titolo,
            String descrizione,
            LocalDate dataPubblicazione,
            LocalDate dataScadenza,
            String citta,
            int quantitaDesiderata) {

        this.idAnnuncio = idAnnuncio;
        this.nomeAutore = nomeAutore;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.prezzo = 0.0;
        this.quantitaDesiderata = quantitaDesiderata;
        this.dataPubblicazione = dataPubblicazione;
        this.dataScadenza = dataScadenza;
        this.citta = citta;
        this.stato = isScaduto() ? "SCADUTO" : "ATTIVO";
        this.quantitaTotale = quantitaDesiderata;
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

    public String getStato() {
        return stato;
    }

    public int getQuantitaTotale() {
        return quantitaTotale;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    // ======================
    // Operazioni di dominio
    // ======================

    public boolean isScaduto() {
        return LocalDate.now().isAfter(this.dataScadenza);
    }

    public boolean isAttivo() {
        return "ATTIVO".equals(stato) && !isScaduto();
    }

    public void aggiornaQuantitaTotale(List<Partecipazione> partecipazioni) {
        // Somma la quantità dell'autore originale più quelle dei partecipanti
        int totale = this.quantitaDesiderata;

        for (Partecipazione p : partecipazioni) {
            totale += p.getQuantitaRichiesta();
        }

        this.quantitaTotale = totale;
    }

    public void aggiornaStato() {
        if (isScaduto() && "ATTIVO".equals(stato)) {
            this.stato = "SCADUTO";
        }
    }

}

