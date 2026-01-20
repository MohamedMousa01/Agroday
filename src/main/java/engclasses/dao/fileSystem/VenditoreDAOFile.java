package main.java.engclasses.dao.fileSystem;

import main.java.engclasses.dao.api.VenditoreDAO;
import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;
import main.java.misc.PersistenceType;
import main.java.model.Utente;

public class VenditoreDAOFile extends UtenteDAOFile implements VenditoreDAO {

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo)  {

    }


}
