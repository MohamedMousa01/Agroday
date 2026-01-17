package main.java.engclasses.pattern.Factory;

import main.java.engclasses.beans.RegistrazioneBean;
import main.java.model.Agricoltore;
import main.java.model.Utente;

public class ConsulenteFactory implements UtenteFactory{

    @Override
    public Utente creaUtente(String id, RegistrazioneBean bean){

        return new Agricoltore(id,
                bean.getNome(),
                bean.getCognome(),
                bean.getUsername(),
                bean.getEmail(),
                bean.getPassword(),
                bean.getCitta());

    }
}
