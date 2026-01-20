package main.java.engclasses.dao.fileSystem;

import main.java.engclasses.dao.api.AgricoltoreDAO;
import main.java.engclasses.dao.api.UtenteDAO;
import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;
import main.java.misc.PersistenceType;
import main.java.model.Utente;

public class AgricoltoreDAOFile extends UtenteDAOFile implements AgricoltoreDAO {

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo)  {

    }

    @Override
    public Utente selezionaUtente(String campo, String valore, boolean persistence) {

        return selezionaUtente(campo, valore, persistence); //non ha senso cancella
    }

    public boolean aggiornaUtente(Utente utenteAggiornato, boolean persistence){
        return true;
    }

}
