package engclasses.dao.fileSystem;

import engclasses.dao.api.AnnuncioDAO;
import model.Annuncio;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementazione su file del DAO per gli annunci.
 * Utilizza la serializzazione Java per persistere i dati.
 */
public class AnnuncioDAOFile implements AnnuncioDAO {

    private static final String FILE_PATH = "annunci.dat";
    private Map<String, Annuncio> annunci;

    public AnnuncioDAOFile() {
        this.annunci = caricaDaFile();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Annuncio> caricaDaFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Annuncio>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[AnnuncioDAOFile] Errore nel caricamento: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private boolean salvaSuFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(annunci);
            return true;
        } catch (IOException e) {
            System.err.println("[AnnuncioDAOFile] Errore nel salvataggio: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean salva(Annuncio annuncio) {
        if (annuncio == null || annuncio.getIdAnnuncio() == null) {
            return false;
        }
        annunci.put(annuncio.getIdAnnuncio(), annuncio);
        return salvaSuFile();
    }

    @Override
    public boolean elimina(String idAnnuncio) {
        if (idAnnuncio == null) {
            return false;
        }
        if (annunci.remove(idAnnuncio) != null) {
            return salvaSuFile();
        }
        return false;
    }

    @Override
    public Annuncio trovaPerId(String idAnnuncio) {
        if (idAnnuncio == null) {
            return null;
        }
        return annunci.get(idAnnuncio);
    }

    @Override
    public List<Annuncio> trovaPerAutore(String autore) {
        if (autore == null) {
            return new ArrayList<>();
        }
        return annunci.values().stream()
                .filter(a -> autore.equals(a.getAutore()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Annuncio> trovaPerCitta(String citta) {
        if (citta == null) {
            return new ArrayList<>();
        }
        return annunci.values().stream()
                .filter(a -> citta.equals(a.getCitta()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Annuncio> trovaTutti() {
        return new ArrayList<>(annunci.values());
    }

    @Override
    public List<Annuncio> trovaAttivi() {
        return annunci.values().stream()
                .filter(a -> !a.isScaduto())
                .collect(Collectors.toList());
    }
}
