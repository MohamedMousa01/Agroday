package engclasses.dao.filesystem;

import engclasses.dao.api.UtenteDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Utente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAOFile implements UtenteDAO {

    private static final Logger logger = LoggerFactory.getLogger(UtenteDAOFile.class);
    private static final Path FILE = Paths.get("utenti.dat");

    @Override
    public boolean esisteUsername(String username) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        try {
            List<Utente> utenti = caricaDaFile();
            return utenti.stream()
                    .anyMatch(a -> a.getUsername().equalsIgnoreCase(username));
        } catch (IOException e) {
            throw new DatabaseConnessioneFallitaException("Errore durante la verifica username dal file.", e);
        }
    }

    @Override
    public boolean esisteEmail(String email) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        try {
            List<Utente> utenti = caricaDaFile();
            return utenti.stream()
                    .anyMatch(a -> a.getEmail().equalsIgnoreCase(email));
        } catch (IOException e) {
            throw new DatabaseConnessioneFallitaException("Errore durante la verifica email dal file.", e);
        }
    }

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo) throws DatabaseOperazioneFallitaException {
        try {
            List<Utente> utenti = caricaDaFile();
            utenti.add(utente);
            logger.debug("Aggiunto utente al buffer per file: {}", utente.getUsername());
            salvaSuFile(utenti);
            logger.debug("Utente salvato su file: {}", utente.getUsername());
        } catch (IOException e) {
            throw new DatabaseOperazioneFallitaException("Errore durante l'aggiunta dell'utente al file.", e);
        }
    }


    @Override
    public Utente selezionaUtente(String username, String password) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        try {
            logger.debug("Cerca utente in file: username={}", username);
            List<Utente> utenti = caricaDaFile();
            logger.debug("Utenti caricati da file: {}", utenti.size());
            
            if (logger.isTraceEnabled()) {
                utenti.forEach(u -> logger.trace("Utente in lista (file): {}, pass={}, equals user: {}, equals pass: {}", 
                    u.getUsername(), u.getPassword(), u.getUsername().equals(username), u.getPassword().equals(password)));
            }

            Utente foundUtente = utenti.stream()
                    .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                    .findFirst()
                    .orElse(null);

            if (foundUtente != null) {
                logger.debug("Utente trovato su file: {}", foundUtente.getUsername());
            } else {
                logger.debug("Utente NON trovato su file con persistenza file");
            }
            return foundUtente;
        } catch (IOException e) {
            throw new DatabaseOperazioneFallitaException("Errore durante la selezione dell'utente dal file per login.", e);
        }
    }


    @Override
    public boolean aggiornaUtente(Utente utenteAggiornato, boolean persistence){
        try {
            List<Utente> utenti = caricaDaFile();
            boolean trovato = false;
            
            for (int i = 0; i < utenti.size(); i++) {
                if (utenti.get(i).getIdUtente().equals(utenteAggiornato.getIdUtente())) {
                    utenti.set(i, utenteAggiornato);
                    trovato = true;
                    break;
                }
            }
            
            if (!trovato) {
                return false;
            }
            
            if (persistence) {
                salvaSuFile(utenti);
            }
            
            return true;
        } catch (IOException e) {
            logger.error("Errore durante l'aggiornamento dell'utente", e);
            return false;
        }
    }

    public boolean aggiornaUtente(Utente utente) {
        // leggi → modifica → riscrivi (con persistenza immediata)
        return aggiornaUtente(utente, true);
    }




    protected List<Utente> caricaDaFile() throws IOException {

        Path file = FILE;

        if (!Files.exists(file) || Files.size(file) == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(Files.newInputStream(file))) {

            return (List<Utente>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Errore durante il caricamento degli utenti dal file.", e);
        }
    }


    private void salvaSuFile(List<Utente> utenti) throws IOException {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(Files.newOutputStream(FILE))) {
            oos.writeObject(utenti);
        }
    }

}
