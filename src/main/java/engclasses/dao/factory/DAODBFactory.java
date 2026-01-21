package engclasses.dao.factory;

import engclasses.dao.api.AgricoltoreDAO;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.api.VenditoreDAO;
import engclasses.dao.db.AgricoltoreDAODB;
import engclasses.dao.db.ConsulenteDAODB;
import engclasses.dao.db.VenditoreDAODB;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.TipoUtente;
import model.Utente;


public class DAODBFactory extends DAOFactory{

    @Override
    public UtenteDAO getUtenteDAO(TipoUtente tipo) {
        return switch (tipo){
            case AGRICOLTORE -> new AgricoltoreDAODB();
            case VENDITORE -> new VenditoreDAODB();
            case CONSULENTE -> new ConsulenteDAODB();
        };
    }
}
