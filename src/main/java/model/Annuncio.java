package model;


import misc.StatoAnnuncio;

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

    // Builder Pattern per risolvere il problema dei troppi parametri
    public static class Builder {
        // Campi obbligatori
        private final String nomeAutore;
        private final String titolo;
        private final String descrizione;
        
        // Campi opzionali con valori di default
        private String idAnnuncio = null; // null = genera UUID
        private LocalDate dataPubblicazione = LocalDate.now();
        private LocalDate dataScadenza = LocalDate.now().plusMonths(1);
        private String citta = "";
        private int quantitaDesiderata = 0;
        private int quantitaTotale = 0;
        private String stato = StatoAnnuncio.ATTIVO;
        
        public Builder(String nomeAutore, String titolo, String descrizione) {
            this.nomeAutore = nomeAutore;
            this.titolo = titolo;
            this.descrizione = descrizione;
        }
        
        public Builder idAnnuncio(String idAnnuncio) {
            this.idAnnuncio = idAnnuncio;
            return this;
        }
        
        public Builder dataPubblicazione(LocalDate dataPubblicazione) {
            this.dataPubblicazione = dataPubblicazione;
            return this;
        }
        
        public Builder dataScadenza(LocalDate dataScadenza) {
            this.dataScadenza = dataScadenza;
            return this;
        }
        
        public Builder citta(String citta) {
            this.citta = citta;
            return this;
        }
        
        public Builder quantitaDesiderata(int quantitaDesiderata) {
            this.quantitaDesiderata = quantitaDesiderata;
            this.quantitaTotale = quantitaDesiderata; // Default = stessa quantità
            return this;
        }
        
        public Builder quantitaTotale(int quantitaTotale) {
            this.quantitaTotale = quantitaTotale;
            return this;
        }
        
        public Builder stato(String stato) {
            this.stato = stato;
            return this;
        }
        
        public Annuncio build() {
            return new Annuncio(this);
        }
    }

    // Costruttore privato usato dal Builder
    private Annuncio(Builder builder) {
        this.idAnnuncio = builder.idAnnuncio != null ? builder.idAnnuncio : UUID.randomUUID().toString();
        this.nomeAutore = builder.nomeAutore;
        this.titolo = builder.titolo;
        this.descrizione = builder.descrizione;
        this.prezzo = 0.0;
        this.quantitaDesiderata = builder.quantitaDesiderata;
        this.dataPubblicazione = builder.dataPubblicazione;
        this.dataScadenza = builder.dataScadenza;
        this.citta = builder.citta;
        this.stato = builder.stato;
        this.quantitaTotale = builder.quantitaTotale;
    }

    // Costruttore semplificato - Usa questo per nuovi annunci
    public Annuncio(String nomeAutore, String titolo, String descrizione, String citta, int quantitaDesiderata) {
        this(new Builder(nomeAutore, titolo, descrizione)
                .citta(citta)
                .quantitaDesiderata(quantitaDesiderata)
                .stato(StatoAnnuncio.ATTIVO));
    }

    // Costruttore con date - Per annunci con date specifiche
    public Annuncio(String nomeAutore, String titolo, String descrizione, String citta, 
                    int quantitaDesiderata, LocalDate dataScadenza) {
        this(new Builder(nomeAutore, titolo, descrizione)
                .citta(citta)
                .quantitaDesiderata(quantitaDesiderata)
                .dataScadenza(dataScadenza)
                .stato(StatoAnnuncio.ATTIVO));
    }


    // ======================
    // Getter
    // ======================

    public String getId() {
        return idAnnuncio;
    }
    
    public String getIdAnnuncio() {
        return getId();
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
        return getDataPubblicazione();
    }
    
    public int getQuantita() {
        return getQuantitaDesiderata();
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
        return StatoAnnuncio.ATTIVO.equals(stato) && !isScaduto();
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
        if (isScaduto() && StatoAnnuncio.ATTIVO.equals(stato)) {
            this.stato = StatoAnnuncio.SCADUTO;
        }
    }

}

