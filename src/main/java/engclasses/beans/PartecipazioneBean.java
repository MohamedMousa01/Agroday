package engclasses.beans;

/**
 * Bean per trasferire dati di partecipazione tra i layer.
 */
public class PartecipazioneBean {

    private String idPartecipazione;
    private String idAnnuncio;
    private String idAgricoltore;
    private int quantitaRichiesta;

    // Costruttore vuoto
    public PartecipazioneBean() {
    }

    // Costruttore con parametri
    public PartecipazioneBean(String idPartecipazione, String idAnnuncio, String idAgricoltore, int quantitaRichiesta) {
        this.idPartecipazione = idPartecipazione;
        this.idAnnuncio = idAnnuncio;
        this.idAgricoltore = idAgricoltore;
        this.quantitaRichiesta = quantitaRichiesta;
    }

    // Getters e Setters
    public String getIdPartecipazione() {
        return idPartecipazione;
    }

    public void setIdPartecipazione(String idPartecipazione) {
        this.idPartecipazione = idPartecipazione;
    }

    public String getIdAnnuncio() {
        return idAnnuncio;
    }

    public void setIdAnnuncio(String idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    public String getIdAgricoltore() {
        return idAgricoltore;
    }

    public void setIdAgricoltore(String idAgricoltore) {
        this.idAgricoltore = idAgricoltore;
    }

    public int getQuantitaRichiesta() {
        return quantitaRichiesta;
    }

    public void setQuantitaRichiesta(int quantitaRichiesta) {
        this.quantitaRichiesta = quantitaRichiesta;
    }
}
