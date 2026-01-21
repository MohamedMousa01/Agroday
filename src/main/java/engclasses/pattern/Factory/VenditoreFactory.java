package engclasses.pattern.Factory;

import engclasses.beans.RegistrazioneBean;
import model.Utente;
import model.Venditore;

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
