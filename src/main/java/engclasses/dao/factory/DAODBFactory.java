package main.java.engclasses.dao.factory;

import main.java.engclasses.dao.api.AgricoltoreDAO;
import main.java.engclasses.dao.api.UtenteDAO;
import main.java.engclasses.dao.api.VenditoreDAO;
import main.java.engclasses.dao.db.AgricoltoreDAODB;
import main.java.engclasses.dao.db.ConsulenteDAODB;
import main.java.engclasses.dao.db.VenditoreDAODB;
import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;
import main.java.misc.TipoUtente;
import main.java.model.Utente;


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
