package model;

import misc.TipoUtente;

import java.io.Serializable;

public class Agricoltore extends model.Utente implements Serializable {


    @Override
    public TipoUtente getTipo(){
        return TipoUtente.AGRICOLTORE;
    }

    public Agricoltore( String idUtente, String username,String email, String password, String nome, String cognome, String citta) {
        super(idUtente, username, email, password, nome, cognome, citta);
    }


}