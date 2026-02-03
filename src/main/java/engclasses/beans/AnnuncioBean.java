package engclasses.beans;

// da controllare le classi, non so se nl bean ci possono essere altri metodi oltre a setter e getter
import misc.StatoAnnuncio;

import java.time.LocalDate;

/**
 * Bean per trasferire dati degli annunci tra i layer
 */
public class AnnuncioBean {
    
    private String idAnnuncio;
    private String autore;
    private String titolo;
    private String descrizione;
    private LocalDate dataCreazione;
    private LocalDate dataScadenza;
    private int quantita;
    private String citta;
    private double prezzo;
    private String stato;
    private int quantitaTotale;
    private int numeroPartecipanti;

    // Getters e Setters
    public String getIdAnnuncio() {
        return idAnnuncio;
    }

    public void setIdAnnuncio(String idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDate getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDate dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public LocalDate getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(LocalDate dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public int getQuantitaTotale() {
        return quantitaTotale;
    }

    public void setQuantitaTotale(int quantitaTotale) {
        this.quantitaTotale = quantitaTotale;
    }

    public int getNumeroPartecipanti() {
        return numeroPartecipanti;
    }

    public void setNumeroPartecipanti(int numeroPartecipanti) {
        this.numeroPartecipanti = numeroPartecipanti;
    }

    public boolean isScaduto() {
        return StatoAnnuncio.SCADUTO.equals(stato) || (dataScadenza != null && LocalDate.now().isAfter(dataScadenza));
    }

    public boolean isAttivo() {
        return StatoAnnuncio.ATTIVO.equals(stato) && !isScaduto();
    }
}
