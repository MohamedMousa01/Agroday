package engclasses.dao.fileSystem;

import engclasses.dao.api.PartecipazioneDAO;
import model.Partecipazione;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementazione su file del DAO per le partecipazioni.
 */
public class PartecipazioneDAOFile implements PartecipazioneDAO {

    private static final String FILE_PATH = "partecipazioni.dat";
    private Map<String, Partecipazione> partecipazioni;

    public PartecipazioneDAOFile() {
        this.partecipazioni = caricaDaFile();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Partecipazione> caricaDaFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Partecipazione>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[PartecipazioneDAOFile] Errore nel caricamento: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private boolean salvaSuFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(partecipazioni);
            return true;
        } catch (IOException e) {
            System.err.println("[PartecipazioneDAOFile] Errore nel salvataggio: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean salva(Partecipazione partecipazione) {
        if (partecipazione == null || partecipazione.getIdPartecipazione() == null) {
            return false;
        }
        partecipazioni.put(partecipazione.getIdPartecipazione(), partecipazione);
        return salvaSuFile();
    }

    @Override
    public boolean elimina(String idPartecipazione) {
        if (idPartecipazione == null) {
            return false;
        }
        if (partecipazioni.remove(idPartecipazione) != null) {
            return salvaSuFile();
        }
        return false;
    }

    @Override
    public Partecipazione trovaPerId(String idPartecipazione) {
        if (idPartecipazione == null) {
            return null;
        }
        return partecipazioni.get(idPartecipazione);
    }

    @Override
    public List<Partecipazione> trovaPerAnnuncio(String idAnnuncio) {
        if (idAnnuncio == null) {
            return new ArrayList<>();
        }
        return partecipazioni.values().stream()
                .filter(p -> idAnnuncio.equals(p.getIdAnnuncio()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Partecipazione> trovaPerAgricoltore(String idAgricoltore) {
        if (idAgricoltore == null) {
            return new ArrayList<>();
        }
        return partecipazioni.values().stream()
                .filter(p -> idAgricoltore.equals(p.getIdAgricoltore()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean haPartecipatoGia(String idAnnuncio, String idAgricoltore) {
        if (idAnnuncio == null || idAgricoltore == null) {
            return false;
        }
        return partecipazioni.values().stream()
                .anyMatch(p -> idAnnuncio.equals(p.getIdAnnuncio()) && 
                              idAgricoltore.equals(p.getIdAgricoltore()));
    }

    @Override
    public List<Partecipazione> trovaTutte() {
        return new ArrayList<>(partecipazioni.values());
    }
}
