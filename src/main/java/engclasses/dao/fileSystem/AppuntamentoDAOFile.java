package engclasses.dao.fileSystem;

import engclasses.dao.api.AppuntamentoDAO;
import misc.StatoAppuntamento;
import model.Appuntamento;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementazione su file del DAO per gli appuntamenti.
 * Utilizza la serializzazione Java per persistere i dati.
 */
public class AppuntamentoDAOFile implements AppuntamentoDAO {

    private static final String FILE_PATH = "appuntamenti.dat";
    private Map<String, Appuntamento> appuntamenti;

    public AppuntamentoDAOFile() {
        this.appuntamenti = caricaDaFile();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Appuntamento> caricaDaFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Appuntamento>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[AppuntamentoDAOFile] Errore nel caricamento: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private boolean salvaSuFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(appuntamenti);
            return true;
        } catch (IOException e) {
            System.err.println("[AppuntamentoDAOFile] Errore nel salvataggio: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean salva(Appuntamento appuntamento) {
        if (appuntamento == null || appuntamento.getIdAppuntamento() == null) {
            return false;
        }
        appuntamenti.put(appuntamento.getIdAppuntamento(), appuntamento);
        return salvaSuFile();
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
        return salvaSuFile();
    }

    @Override
    public boolean elimina(String idAppuntamento) {
        if (idAppuntamento == null) {
            return false;
        }
        if (appuntamenti.remove(idAppuntamento) != null) {
            return salvaSuFile();
        }
        return false;
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
                .filter(a -> a.getStato().isAttivo())
                .filter(a -> escludiId == null || !escludiId.equals(a.getIdAppuntamento()))
                .anyMatch(a -> inizio.isBefore(a.getDataOraFine()) && fine.isAfter(a.getDataOraInizio()));
    }
}
