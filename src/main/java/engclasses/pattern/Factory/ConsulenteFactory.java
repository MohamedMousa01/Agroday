package engclasses.pattern.Factory;

import engclasses.beans.RegistrazioneBean;
import model.Consulente;
import model.Utente;

public class ConsulenteFactory implements UtenteFactory{

    @Override
    public Utente creaUtente(String id, RegistrazioneBean bean){

        return new Consulente(id,
                bean.getUsername(),
                bean.getEmail(),
                bean.getPassword(),
                bean.getNome(),
                bean.getCognome(),
                bean.getCitta());

    }
}
