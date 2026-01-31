package model;

import misc.TipoUtente;

import java.io.Serializable;

public class Consulente extends Utente implements Serializable {

    @Override
    public TipoUtente getTipo(){
        return TipoUtente.CONSULENTE;
    }

    public Consulente(String idUtente, String username, String email, String password, String nome, String cognome, String citta) {
        super(idUtente, username, email, password, nome, cognome, citta);
    }

}
