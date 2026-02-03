package engclasses.dao;

import engclasses.dao.api.UtenteDAO;
import engclasses.dao.db.UtenteDAODB;
import engclasses.dao.filesystem.UtenteDAOFile;
import engclasses.dao.memory.UtenteDAOMemory;

import misc.PersistenceType;


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
