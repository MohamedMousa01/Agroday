package main.java.model;

import main.java.misc.TipoUtente;

public class Consulente extends Utente{

    private final String citta;

    @Override
    public TipoUtente getTipo(){
        return TipoUtente.CONSULENTE;
    }

    public Consulente(String idUtente, String nome, String cognome, String username, String email, String password, String citta) {
        super(nome, cognome, username, email, password,idUtente);
        this.citta = citta;
    }

    public String getCitta(){
        return citta;
    }

}
