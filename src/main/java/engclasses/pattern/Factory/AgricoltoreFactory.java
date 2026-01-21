package engclasses.pattern.Factory;

import engclasses.beans.RegistrazioneBean;
import model.Agricoltore;
import model.Utente;

public class AgricoltoreFactory implements UtenteFactory {

    @Override
    public Utente creaUtente(String id, RegistrazioneBean bean){

        return new Agricoltore(id,
                bean.getUsername(),
                bean.getEmail(),
                bean.getPassword(),
                bean.getNome(),
                bean.getCognome(),
                bean.getCitta());

    }
}
