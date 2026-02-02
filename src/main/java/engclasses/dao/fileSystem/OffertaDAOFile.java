package engclasses.dao.fileSystem;

import engclasses.dao.api.OffertaDAO;
import model.Offerta;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementazione su file del DAO per le offerte.
 */
public class OffertaDAOFile implements OffertaDAO {

    private static final String FILE_PATH = "offerte.dat";
    private Map<String, Offerta> offerte;

    public OffertaDAOFile() {
        this.offerte = caricaDaFile();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Offerta> caricaDaFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Offerta>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[OffertaDAOFile] Errore nel caricamento: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private boolean salvaSuFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(offerte);
            return true;
        } catch (IOException e) {
            System.err.println("[OffertaDAOFile] Errore nel salvataggio: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean salva(Offerta offerta) {
        if (offerta == null || offerta.getIdOfferta() == null) {
            return false;
        }
        offerte.put(offerta.getIdOfferta(), offerta);
        return salvaSuFile();
    }

    @Override
    public boolean elimina(String idOfferta) {
        if (offerte.remove(idOfferta) != null) {
            return salvaSuFile();
        }
        return false;
    }

    @Override
    public Offerta trovaPerId(String idOfferta) {
        return offerte.get(idOfferta);
    }

    @Override
    public List<Offerta> trovaPerAnnuncio(String idAnnuncio) {
        return offerte.values().stream()
                .filter(o -> idAnnuncio.equals(o.getIdAnnuncio()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Offerta> trovaPerVenditore(String usernameVenditore) {
        return offerte.values().stream()
                .filter(o -> usernameVenditore.equals(o.getUsernameVenditore()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Offerta> trovaOffertePendingPerVenditore(String usernameVenditore) {
        return offerte.values().stream()
                .filter(o -> usernameVenditore.equals(o.getUsernameVenditore()) && o.isPending())
                .collect(Collectors.toList());
    }

    @Override
    public boolean aggiornaStato(String idOfferta, String nuovoStato) {
        Offerta offerta = offerte.get(idOfferta);
        if (offerta != null) {
            offerta.setStato(nuovoStato);
            return salvaSuFile();
        }
        return false;
    }

    @Override
    public List<Offerta> trovaTutte() {
        return new ArrayList<>(offerte.values());
    }

    @Override
    public List<Offerta> trovaOfferteRicevutePerAgricoltore(String usernameAgricoltore) {
        // Per implementazione file, dobbiamo caricare anche gli annunci
        // Soluzione semplificata: restituiamo lista vuota o implementazione base
        // In produzione si dovrebbe caricare anche AnnuncioDAOFile
        return new ArrayList<>();
    }
}
