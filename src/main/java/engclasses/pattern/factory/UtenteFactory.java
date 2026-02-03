package engclasses.pattern.factory;

import engclasses.beans.RegistrazioneBean;
import model.Utente;


// Mi sa che non serve a nulla e va cancellata

public interface UtenteFactory {
    Utente creaUtente(String id, RegistrazioneBean bean);
}

