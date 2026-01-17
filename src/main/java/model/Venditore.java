package main.java.model;

import main.java.misc.TipoUtente;

public class Venditore extends Utente {

    @Override
    public TipoUtente getTipo(){
        return TipoUtente.VENDITORE;
    }

    public Venditore(String idUtente, String nome, String cognome, String username, String email, String password) {
        super(nome, cognome, username, email, password, idUtente);

    }
}
