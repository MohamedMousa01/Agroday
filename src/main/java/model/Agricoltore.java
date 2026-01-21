package model;

import misc.TipoUtente;

public class Agricoltore extends model.Utente {


    @Override
    public TipoUtente getTipo(){
        return TipoUtente.AGRICOLTORE;
    }

    public Agricoltore( String idUtente, String username,String email, String password, String nome, String cognome, String citta) {
        super(idUtente, username, email, password, nome, cognome, citta);
    }


}