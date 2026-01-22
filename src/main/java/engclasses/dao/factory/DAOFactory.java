package engclasses.dao.factory;

import engclasses.dao.api.AgricoltoreDAO;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.api.VenditoreDAO;
import misc.PersistenceType;
import misc.TipoUtente;

public abstract class DAOFactory {



    public abstract UtenteDAO getUtenteDAO(TipoUtente tipo);
    public abstract UtenteDAO getLoginUtenteDAO();

    public static DAOFactory getFactory(PersistenceType type) {
        return switch (type) {
            case MEMORY -> new DAOMemoryFactory();
            case DB     -> new DAODBFactory();
            case FILE   -> new DAOFileFactory();
        };
    }
}
