package engclasses.dao.fileSystem;

import engclasses.dao.api.UtenteDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import misc.PersistenceType;
import model.Agricoltore;
import model.Utente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAOFile implements UtenteDAO {

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
            System.out.println("DEBUG: Aggiunto utente al buffer per file: " + utente.getUsername());
            salvaSuFile(utenti);
            System.out.println("DEBUG: Utente salvato su file: " + utente.getUsername());
        } catch (IOException e) {
            throw new DatabaseOperazioneFallitaException("Errore durante l'aggiunta dell'utente al file.", e);
        }
    }


    @Override
    public Utente selezionaUtente(String username, String password) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        try {
            System.out.println("DEBUG: Cerca utente in file: username=" + username + ", password=" + password);
            List<Utente> utenti = caricaDaFile();
            System.out.println("DEBUG: Utenti caricati da file: " + utenti.size());
            utenti.forEach(u -> System.out.println("DEBUG: Utente in lista (file): " + u.getUsername() + ", pass=" + u.getPassword() + ", equals user: " + u.getUsername().equals(username) + ", equals pass: " + u.getPassword().equals(password)));

            Utente foundUtente = utenti.stream()
                    .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                    .findFirst()
                    .orElse(null);

            if (foundUtente != null) {
                System.out.println("DEBUG: Utente trovato su file: " + foundUtente.getUsername());
            } else {
                System.out.println("DEBUG: Utente NON trovato su file con persistenza file.");
            }
            return foundUtente;
        } catch (IOException e) {
            System.err.println("DEBUG ERROR: IOException in selezionaUtente (file): " + e.getMessage());
            e.printStackTrace();
            throw new DatabaseOperazioneFallitaException("Errore durante la selezione dell'utente dal file per login.", e);
        }
    }


    public boolean aggiornaUtente(Utente utenteAggiornato, boolean persistence){
        return true;
    }


    public boolean aggiornaUtente(Utente utente) {
        // leggi → modifica → riscrivi
        return true;
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
            e.printStackTrace(); // Added for debugging
            throw new IOException("Errore durante il caricamento degli utenti dal file.", e);
        }
    }


    private void salvaSuFile(List<Utente> utenti) throws IOException {

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(Files.newOutputStream(FILE))) {

            oos.writeObject(utenti);

        } catch (IOException e) {
            throw e; // Rilancia IOException
        }
    }

}
