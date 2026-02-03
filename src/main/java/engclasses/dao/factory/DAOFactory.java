package engclasses.dao.factory;


import engclasses.dao.api.AnnuncioDAO;
import engclasses.dao.api.AppuntamentoDAO;
import engclasses.dao.api.ConsulenteDAO;
import engclasses.dao.api.PartecipazioneDAO;
import engclasses.dao.api.UtenteDAO;
import misc.PersistenceType;
import misc.TipoUtente;

public abstract class DAOFactory {

    public abstract UtenteDAO getUtenteDAO(TipoUtente tipo);
    public abstract UtenteDAO getLoginUtenteDAO();
    public abstract AppuntamentoDAO getAppuntamentoDAO();
    public abstract AnnuncioDAO getAnnuncioDAO();
    public abstract PartecipazioneDAO getPartecipazioneDAO();
    public abstract engclasses.dao.api.OffertaDAO getOffertaDAO();
    public abstract ConsulenteDAO getConsulenteDAO();

    public static DAOFactory getFactory(PersistenceType type) {
        return switch (type) {
            case MEMORY -> new DAOMemoryFactory();
            case DB     -> new DAODBFactory();
            case FILE   -> new DAOFileFactory();
        };
    }
}
