package engclasses.dao.memory;

import engclasses.dao.api.AppuntamentoDAO;
import misc.StatoAppuntamento;
import model.Appuntamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementazione in memoria del DAO per gli appuntamenti.
 * Utilizzata in modalità Demo.
 */
public class AppuntamentoDAOMemory implements AppuntamentoDAO {

    // Storage in memoria condiviso (Singleton-like per mantenere i dati durante la sessione)
    private static final Map<String, Appuntamento> appuntamenti = new HashMap<>();

    @Override
    public boolean salva(Appuntamento appuntamento) {
        if (appuntamento == null || appuntamento.getIdAppuntamento() == null) {
            return false;
        }
        appuntamenti.put(appuntamento.getIdAppuntamento(), appuntamento);
        return true;
    }

    @Override
    public boolean aggiorna(Appuntamento appuntamento) {
        if (appuntamento == null || appuntamento.getIdAppuntamento() == null) {
            return false;
        }
        if (!appuntamenti.containsKey(appuntamento.getIdAppuntamento())) {
            return false;
        }
        appuntamenti.put(appuntamento.getIdAppuntamento(), appuntamento);
        return true;
    }

    @Override
    public boolean elimina(String idAppuntamento) {
        if (idAppuntamento == null) {
            return false;
        }
        return appuntamenti.remove(idAppuntamento) != null;
    }

    @Override
    public Appuntamento trovaPerId(String idAppuntamento) {
        if (idAppuntamento == null) {
            return null;
        }
        return appuntamenti.get(idAppuntamento);
    }

    @Override
    public List<Appuntamento> trovaPerCliente(String idCliente) {
        if (idCliente == null) {
            return new ArrayList<>();
        }
        return appuntamenti.values().stream()
                .filter(a -> idCliente.equals(a.getIdCliente()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appuntamento> trovaPerConsulente(String idConsulente) {
        if (idConsulente == null) {
            return new ArrayList<>();
        }
        return appuntamenti.values().stream()
                .filter(a -> idConsulente.equals(a.getIdConsulente()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appuntamento> trovaPerStato(StatoAppuntamento stato) {
        if (stato == null) {
            return new ArrayList<>();
        }
        return appuntamenti.values().stream()
                .filter(a -> stato.equals(a.getStato()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appuntamento> trovaPerIntervallo(LocalDateTime da, LocalDateTime a) {
        if (da == null || a == null) {
            return new ArrayList<>();
        }
        return appuntamenti.values().stream()
                .filter(app -> !app.getDataOraInizio().isBefore(da) && !app.getDataOraInizio().isAfter(a))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appuntamento> trovaPerConsulenteEIntervallo(String idConsulente, LocalDateTime da, LocalDateTime a) {
        if (idConsulente == null || da == null || a == null) {
            return new ArrayList<>();
        }
        return appuntamenti.values().stream()
                .filter(app -> idConsulente.equals(app.getIdConsulente()))
                .filter(app -> !app.getDataOraInizio().isBefore(da) && !app.getDataOraInizio().isAfter(a))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appuntamento> trovaTutti() {
        return new ArrayList<>(appuntamenti.values());
    }

    @Override
    public boolean esisteConflitto(String idConsulente, LocalDateTime inizio, LocalDateTime fine, String escludiId) {
        if (idConsulente == null || inizio == null || fine == null) {
            return false;
        }
        
        return appuntamenti.values().stream()
                .filter(a -> idConsulente.equals(a.getIdConsulente()))
                .filter(a -> a.getStato().isAttivo()) // Solo appuntamenti attivi
                .filter(a -> escludiId == null || !escludiId.equals(a.getIdAppuntamento()))
                .anyMatch(a -> {
                    // Verifica sovrapposizione: il nuovo appuntamento si sovrappone se
                    // inizia prima della fine dell'esistente E finisce dopo l'inizio dell'esistente
                    return inizio.isBefore(a.getDataOraFine()) && fine.isAfter(a.getDataOraInizio());
                });
    }

    /**
     * Pulisce tutti i dati in memoria.
     * Utile per i test.
     */
    public static void clear() {
        appuntamenti.clear();
    }
}
