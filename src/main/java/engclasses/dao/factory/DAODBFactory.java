package engclasses.dao.factory;

import engclasses.dao.api.AgricoltoreDAO;
import engclasses.dao.api.AnnuncioDAO;
import engclasses.dao.api.AppuntamentoDAO;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.api.VenditoreDAO;
import engclasses.dao.db.AgricoltoreDAODB;
import engclasses.dao.db.AnnuncioDAODB;
import engclasses.dao.db.AppuntamentoDAODB;
import engclasses.dao.db.ConsulenteDAODB;
import engclasses.dao.db.UtenteDAODB;
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

    @Override
    public UtenteDAO getLoginUtenteDAO() {
        return new UtenteDAODB();
    }

    @Override
    public AppuntamentoDAO getAppuntamentoDAO() {
        return new AppuntamentoDAODB();
    }

    @Override
    public AnnuncioDAO getAnnuncioDAO() {
        return new AnnuncioDAODB();
    }

    @Override
    public engclasses.dao.api.PartecipazioneDAO getPartecipazioneDAO() {
        return new engclasses.dao.db.PartecipazioneDAODB();
    }

    @Override
    public engclasses.dao.api.OffertaDAO getOffertaDAO() {
        return new engclasses.dao.db.OffertaDAODB();
    }

    @Override
    public engclasses.dao.api.ConsulenteDAO getConsulenteDAO() {
        return new ConsulenteDAODB();
    }
}
