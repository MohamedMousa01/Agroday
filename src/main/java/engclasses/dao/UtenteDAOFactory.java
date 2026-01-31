package engclasses.dao;

import engclasses.dao.api.AgricoltoreDAO;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.api.VenditoreDAO;
import engclasses.dao.db.UtenteDAODB;
import engclasses.dao.fileSystem.UtenteDAOFile;
import engclasses.dao.memory.UtenteDAOMemory;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Agricoltore;
import model.Utente;

public class UtenteDAOFactory {

    private UtenteDAOFactory() {
        // impedisce istanziazione
    }

    public static UtenteDAO getUtenteDAO(PersistenceType type) {

        return switch (type) {

            case MEMORY -> new UtenteDAOMemory();

            case FILE-> new UtenteDAOFile();

            case DB -> new UtenteDAODB();
        };
    }
}
