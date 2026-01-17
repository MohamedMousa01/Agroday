package main.java.engclasses.dao.factory;

import main.java.engclasses.dao.api.AgricoltoreDAO;
import main.java.engclasses.dao.api.UtenteDAO;
import main.java.engclasses.dao.api.VenditoreDAO;
import main.java.engclasses.dao.fileSystem.AgricoltoreDAOFile;
import main.java.engclasses.dao.fileSystem.ConsulenteDAOFile;
import main.java.engclasses.dao.fileSystem.VenditoreDAOFile;
import main.java.engclasses.dao.memory.AgricoltoreDAOMemory;
import main.java.engclasses.dao.memory.ConsulenteDAOMemory;
import main.java.engclasses.dao.memory.VenditoreDAOMemory;
import main.java.misc.TipoUtente;

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