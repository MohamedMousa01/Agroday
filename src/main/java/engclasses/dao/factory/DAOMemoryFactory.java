package engclasses.dao.factory;

import engclasses.dao.api.AgricoltoreDAO;
import engclasses.dao.api.AnnuncioDAO;
import engclasses.dao.api.AppuntamentoDAO;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.api.VenditoreDAO;
import engclasses.dao.db.UtenteDAODB;
import engclasses.dao.fileSystem.AgricoltoreDAOFile;
import engclasses.dao.fileSystem.ConsulenteDAOFile;
import engclasses.dao.fileSystem.VenditoreDAOFile;
import engclasses.dao.memory.AgricoltoreDAOMemory;
import engclasses.dao.memory.AnnuncioDAOMemory;
import engclasses.dao.memory.AppuntamentoDAOMemory;
import engclasses.dao.memory.ConsulenteDAOMemory;
import engclasses.dao.memory.UtenteDAOMemory;
import engclasses.dao.memory.VenditoreDAOMemory;
import misc.TipoUtente;

public class DAOMemoryFactory extends DAOFactory {

    @Override
    public UtenteDAO getUtenteDAO(TipoUtente tipo) {
        return switch (tipo) {
            case AGRICOLTORE -> new AgricoltoreDAOMemory();
            case VENDITORE -> new VenditoreDAOMemory();
            case CONSULENTE -> new ConsulenteDAOMemory();
        };
    }

    @Override
    public UtenteDAO getLoginUtenteDAO() {
        return new UtenteDAOMemory();
    }

    @Override
    public AppuntamentoDAO getAppuntamentoDAO() {
        return new AppuntamentoDAOMemory();
    }

    @Override
    public AnnuncioDAO getAnnuncioDAO() {
        return new AnnuncioDAOMemory();
    }

    @Override
    public engclasses.dao.api.PartecipazioneDAO getPartecipazioneDAO() {
        return new engclasses.dao.memory.PartecipazioneDAOMemory();
    }

    @Override
    public engclasses.dao.api.OffertaDAO getOffertaDAO() {
        return new engclasses.dao.memory.OffertaDAOMemory();
    }

    @Override
    public engclasses.dao.api.ConsulenteDAO getConsulenteDAO() {
        return new ConsulenteDAOMemory();
    }
}
