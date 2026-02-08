package engclasses.dao.factory;


import engclasses.dao.api.AnnuncioDAO;
import engclasses.dao.api.AppuntamentoDAO;
import engclasses.dao.api.UtenteDAO;
import engclasses.dao.memory.AgricoltoreDAOMemory;
import engclasses.dao.memory.AnnuncioDAOMemory;
import engclasses.dao.memory.AppuntamentoDAOMemory;
import engclasses.dao.memory.ConsulenteDAOMemory;
import engclasses.dao.memory.UtenteDAOMemory;
import engclasses.dao.memory.VenditoreDAOMemory;
import misc.TipoUtente;

public class DAOMemoryFactory extends DAOFactory {

    // Singleton instances per garantire lo stesso storage in memoria
    private static AgricoltoreDAOMemory agricoltoreDAO;
    private static VenditoreDAOMemory venditoreDAO;
    private static ConsulenteDAOMemory consulenteDAO;
    private static UtenteDAOMemory utenteDAO;
    private static AppuntamentoDAOMemory appuntamentoDAO;
    private static AnnuncioDAOMemory annuncioDAO;
    private static engclasses.dao.memory.PartecipazioneDAOMemory partecipazioneDAO;
    private static engclasses.dao.memory.OffertaDAOMemory offertaDAO;

    @Override
    public UtenteDAO getUtenteDAO(TipoUtente tipo) {
        return switch (tipo) {
            case AGRICOLTORE -> {
                if (agricoltoreDAO == null) {
                    agricoltoreDAO = new AgricoltoreDAOMemory();
                }
                yield agricoltoreDAO;
            }
            case VENDITORE -> {
                if (venditoreDAO == null) {
                    venditoreDAO = new VenditoreDAOMemory();
                }
                yield venditoreDAO;
            }
            case CONSULENTE -> {
                if (consulenteDAO == null) {
                    consulenteDAO = new ConsulenteDAOMemory();
                }
                yield consulenteDAO;
            }
        };
    }

    @Override
    public UtenteDAO getLoginUtenteDAO() {
        if (utenteDAO == null) {
            utenteDAO = new UtenteDAOMemory();
        }
        return utenteDAO;
    }

    @Override
    public AppuntamentoDAO getAppuntamentoDAO() {
        if (appuntamentoDAO == null) {
            appuntamentoDAO = new AppuntamentoDAOMemory();
        }
        return appuntamentoDAO;
    }

    @Override
    public AnnuncioDAO getAnnuncioDAO() {
        if (annuncioDAO == null) {
            annuncioDAO = new AnnuncioDAOMemory();
        }
        return annuncioDAO;
    }

    @Override
    public engclasses.dao.api.PartecipazioneDAO getPartecipazioneDAO() {
        if (partecipazioneDAO == null) {
            partecipazioneDAO = new engclasses.dao.memory.PartecipazioneDAOMemory();
        }
        return partecipazioneDAO;
    }

    @Override
    public engclasses.dao.api.OffertaDAO getOffertaDAO() {
        if (offertaDAO == null) {
            offertaDAO = new engclasses.dao.memory.OffertaDAOMemory();
        }
        return offertaDAO;
    }

    @Override
    public engclasses.dao.api.ConsulenteDAO getConsulenteDAO() {
        if (consulenteDAO == null) {
            consulenteDAO = new ConsulenteDAOMemory();
        }
        return consulenteDAO;
    }
}
