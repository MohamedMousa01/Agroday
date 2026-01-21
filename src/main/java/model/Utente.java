package model;

import misc.TipoUtente;

public abstract class Utente {
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String password;
    private final String idUtente;
    private String citta;

    public abstract TipoUtente getTipo();


    protected Utente(String idUtente, String username,String email, String password, String nome, String cognome, String citta) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.email = email;
        this.password = password;
        this.idUtente = idUtente; // Genera un UUID univoco
        this.citta = citta;
    }



    // Getter e Setter
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdUtente() {
        return idUtente;
    }

    public void setCitta(String citta){
        this.citta = citta;
    }
    public String getCitta() { return citta;}

}
