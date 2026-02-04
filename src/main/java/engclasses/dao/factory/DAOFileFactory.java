package engclasses.dao.factory;


import engclasses.dao.api.AnnuncioDAO;
import engclasses.dao.api.AppuntamentoDAO;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.filesystem.AgricoltoreDAOFile;
import engclasses.dao.filesystem.AnnuncioDAOFile;
import engclasses.dao.filesystem.AppuntamentoDAOFile;
import engclasses.dao.filesystem.ConsulenteDAOFile;
import engclasses.dao.filesystem.UtenteDAOFile;
import engclasses.dao.filesystem.VenditoreDAOFile;
import misc.TipoUtente;

public class DAOFileFactory extends DAOFactory {

    @Override
    public UtenteDAO getUtenteDAO(TipoUtente tipo) {
        return switch (tipo) {
            case AGRICOLTORE -> new AgricoltoreDAOFile();
            case VENDITORE -> new VenditoreDAOFile();
            case CONSULENTE -> new ConsulenteDAOFile();
        };
    }

    @Override
    public UtenteDAO getLoginUtenteDAO() {
        return new UtenteDAOFile();
    }

    @Override
    public AppuntamentoDAO getAppuntamentoDAO() {
        return new AppuntamentoDAOFile();
    }

    @Override
    public AnnuncioDAO getAnnuncioDAO() {
        return new AnnuncioDAOFile();
    }

    @Override
    public engclasses.dao.api.PartecipazioneDAO getPartecipazioneDAO() {
        return new engclasses.dao.filesystem.PartecipazioneDAOFile();
    }

    @Override
    public engclasses.dao.api.OffertaDAO getOffertaDAO() {
        return new engclasses.dao.filesystem.OffertaDAOFile();
    }

    @Override
    public engclasses.dao.api.ConsulenteDAO getConsulenteDAO() {
        return new ConsulenteDAOFile();
    }
}
