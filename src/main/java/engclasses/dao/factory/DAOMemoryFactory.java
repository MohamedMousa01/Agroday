package engclasses.dao.factory;

import engclasses.dao.api.AgricoltoreDAO;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.api.VenditoreDAO;
import engclasses.dao.fileSystem.AgricoltoreDAOFile;
import engclasses.dao.fileSystem.ConsulenteDAOFile;
import engclasses.dao.fileSystem.VenditoreDAOFile;
import engclasses.dao.memory.AgricoltoreDAOMemory;
import engclasses.dao.memory.ConsulenteDAOMemory;
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
}