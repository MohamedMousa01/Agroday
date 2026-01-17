package main.java.engclasses.dao.factory;

import main.java.engclasses.dao.api.AgricoltoreDAO;
import main.java.engclasses.dao.api.UtenteDAO;
import main.java.engclasses.dao.api.VenditoreDAO;
import main.java.misc.PersistenceType;
import main.java.misc.TipoUtente;

public abstract class DAOFactory {

//    public abstract AgricoltoreDAO getAgricoltoreDAO();
//    public abstract VenditoreDAO getVenditoreDAO();

    public abstract UtenteDAO getUtenteDAO(TipoUtente tipo);

    public static DAOFactory getFactory(PersistenceType type) {
        return switch (type) {
            case MEMORY -> new DAOMemoryFactory();
            case DB     -> new DAODBFactory();
            case FILE   -> new DAOFileFactory();
        };
    }
}
