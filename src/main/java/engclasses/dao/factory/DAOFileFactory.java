package engclasses.dao.factory;

import engclasses.dao.api.AgricoltoreDAO;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.api.VenditoreDAO;
import engclasses.dao.db.AgricoltoreDAODB;
import engclasses.dao.db.ConsulenteDAODB;
import engclasses.dao.db.VenditoreDAODB;
import engclasses.dao.fileSystem.AgricoltoreDAOFile;
import engclasses.dao.fileSystem.ConsulenteDAOFile;
import engclasses.dao.fileSystem.VenditoreDAOFile;
import engclasses.dao.memory.AgricoltoreDAOMemory;
import engclasses.dao.memory.VenditoreDAOMemory;
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

}
