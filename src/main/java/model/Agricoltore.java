package main.java.model;

import main.java.misc.TipoUtente;

public class Agricoltore extends Utente {


    @Override
    public TipoUtente getTipo(){
        return TipoUtente.AGRICOLTORE;
    }

    public Agricoltore( String idUtente, String username,String email, String password, String nome, String cognome, String citta) {
        super(idUtente, username, email, password, nome, cognome, citta);
    }


}