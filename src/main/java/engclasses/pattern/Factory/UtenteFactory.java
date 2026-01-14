package main.java.engclasses.pattern.Factory;

import main.java.engclasses.beans.RegistrazioneBean;
import main.java.model.Utente;

public interface UtenteFactory {
    Utente creaUtente(String id, RegistrazioneBean bean);
}

