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
    private String ruoloUtente; // es: "consulente", "produttore", "acquirente"


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

    public String getRuoloUtente() { return ruoloUtente; }

    public void setRuoloUtente(String ruoloUtente) {
        this.ruoloUtente = ruoloUtente;
    }

    public List<AnnuncioBean> getAnnunci() {
        return Annunci;
    }

    public void setAnnunci(List<AnnuncioBean> Annunci) {
        this.Annunci = Annunci;
    }

}
