package main.java.engclasses.pattern.Factory;

import main.java.engclasses.beans.RegistrazioneBean;
import main.java.model.Utente;


// Mi sa che non serve a nulla e va cancellata

public interface UtenteFactory {
    Utente creaUtente(String id, RegistrazioneBean bean);
}

