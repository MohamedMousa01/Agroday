package main.java.engclasses.pattern.Factory;

import main.java.engclasses.beans.RegistrazioneBean;
import main.java.model.Utente;
import main.java.model.Venditore;

public class VenditoreFactory implements UtenteFactory {

    @Override
    public Utente creaUtente(String id, RegistrazioneBean bean){

        return new Venditore(id,
                bean.getUsername(),
                bean.getEmail(),
                bean.getPassword(),
                bean.getNome(),
                bean.getCognome(),
                bean.getCitta());
    }
}
