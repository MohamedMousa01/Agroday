package main.java.misc;

import main.java.engclasses.beans.AnnuncioBean;
import java.util.List;

public class Session {

    private String nome;
    private String username;
    private String idUtente;
    private boolean persistence; // Modalità di persistenza (buffer o database)
    private long idAnnuncio;
    private List<AnnuncioBean> Annunci;
    private boolean isAgricoltore; // es: "produttore", "acquirente"
    private String nomeAgricoltore;
    private String cognomeAgricoltore;


    public Session(boolean persistence) {
            this.persistence = persistence;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {return nome;}

    public boolean isPersistence() {
        return persistence;
    }

    public void setPersistence(boolean persistence) {
        this.persistence = persistence;
    }

    public void setUsername(String username) { this.username = username; }

    public String getUsername() {
        return username;
    }

    public void setIdUtente(String idUtente) {
        this.idUtente = idUtente;
    }

    public String getIdUtente() {
        return idUtente;
    }

    public long getIdAnnuncio() {
        return idAnnuncio;
    }

    public void setIdAnnuncio(long idAnnuncio) {
        this.idAnnuncio = idAnnuncio;
    }

    public boolean isAgricoltore() {
        return isAgricoltore;
    }

    public void setIsOrganizzatore(boolean agricoltore) {
        isAgricoltore = agricoltore;
    }

    public List<AnnuncioBean> getAnnunci() {
        return Annunci;
    }

    public void setAnnunci(List<AnnuncioBean> Annunci) {
        this.Annunci = Annunci;
    }

    public String getNomeAgricoltore() { return nomeAgricoltore; }

    public void setNomeAgricoltore(String nomeAgricoltore) { this.nomeAgricoltore = nomeAgricoltore; }

    public String getCognomeAgricoltore() { return cognomeAgricoltore; }

    public void setCognomeAgricoltore (String cognomeAgricoltore) { this.cognomeAgricoltore = cognomeAgricoltore;  }


}
