package model;

import java.util.UUID;

public class Partecipazione {

    private final String idPartecipazione;
    private final String idAnnuncio;
    private final String idAgricoltore;
    private int quantitaRichiesta;

    // Costruttore
    public Partecipazione(String idAnnuncio, String idAgricoltore, int quantitaRichiesta) {
        this.idPartecipazione = UUID.randomUUID().toString();
        this.idAnnuncio = idAnnuncio;
        this.idAgricoltore = idAgricoltore;
        this.quantitaRichiesta = quantitaRichiesta;
    }

    // Getter
    public String getIdPartecipazione() {
        return idPartecipazione;
    }

    public String getIdAnnuncio() {
        return idAnnuncio;
    }

    public String getIdAgricoltore() {
        return idAgricoltore;
    }

    public int getQuantitaRichiesta() {
        return quantitaRichiesta;
    }

    // Setter
    public void setQuantitaRichiesta(int quantitaRichiesta) {
        if (quantitaRichiesta <= 0) {
            throw new IllegalArgumentException("La quantità richiesta deve essere maggiore di zero");
        }
        this.quantitaRichiesta = quantitaRichiesta;
    }
}
