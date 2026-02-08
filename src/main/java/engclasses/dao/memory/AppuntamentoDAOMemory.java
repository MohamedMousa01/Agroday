package engclasses.dao.memory;

import engclasses.dao.api.AppuntamentoDAO;
import misc.StatoAppuntamento;
import model.Appuntamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementazione in memoria del DAO per gli appuntamenti.
 * Utilizzata in modalità Demo.
 */
public class AppuntamentoDAOMemory extends InMemoryDAO<Appuntamento, String> implements AppuntamentoDAO {

    @Override
    protected String getId(Appuntamento entity) {
        return entity.getIdAppuntamento();
    }

    @Override
    public boolean aggiorna(Appuntamento appuntamento) {
        if (appuntamento == null || !exists(appuntamento.getIdAppuntamento())) {
            return false;
        }
        return salva(appuntamento);
    }

    @Override
    public List<Appuntamento> trovaPerCliente(String idCliente) {
        if (idCliente == null) {
            return new ArrayList<>();
        }
        return trovaPerCriterio(a -> idCliente.equals(a.getIdCliente()));
    }

    @Override
    public List<Appuntamento> trovaPerConsulente(String idConsulente) {
        if (idConsulente == null) {
            return new ArrayList<>();
        }
        return trovaPerCriterio(a -> idConsulente.equals(a.getIdConsulente()));
    }

    @Override
    public List<Appuntamento> trovaPerStato(StatoAppuntamento stato) {
        if (stato == null) {
            return new ArrayList<>();
        }
        return trovaPerCriterio(a -> stato.equals(a.getStato()));
    }

    @Override
    public List<Appuntamento> trovaPerIntervallo(LocalDateTime da, LocalDateTime a) {
        if (da == null || a == null) {
            return new ArrayList<>();
        }
        return trovaPerCriterio(app -> !app.getDataOraInizio().isBefore(da) && !app.getDataOraInizio().isAfter(a));
    }

    @Override
    public List<Appuntamento> trovaPerConsulenteEIntervallo(String idConsulente, LocalDateTime da, LocalDateTime a) {
        if (idConsulente == null || da == null || a == null) {
            return new ArrayList<>();
        }
        return trovaPerCriterio(app -> idConsulente.equals(app.getIdConsulente()) 
                && !app.getDataOraInizio().isBefore(da) 
                && !app.getDataOraInizio().isAfter(a));
    }

    @Override
    public boolean esisteConflitto(String idConsulente, LocalDateTime inizio, LocalDateTime fine, String escludiId) {
        if (idConsulente == null || inizio == null || fine == null) {
            return false;
        }
        
        return storage.values().stream()
                .filter(a -> idConsulente.equals(a.getIdConsulente()))
                .filter(a -> a.getStato().isAttivo())
                .filter(a -> escludiId == null || !escludiId.equals(a.getIdAppuntamento()))
                .anyMatch(a -> inizio.isBefore(a.getDataOraFine()) && fine.isAfter(a.getDataOraInizio()));
    }
}
