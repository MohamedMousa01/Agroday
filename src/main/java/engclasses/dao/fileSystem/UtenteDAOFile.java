package engclasses.dao.fileSystem;

import engclasses.dao.api.UtenteDAO;
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
    public boolean esisteUsername(String username) {
        List<Utente> utente = caricaDaFile();
        return utente.stream()
                .anyMatch(a -> a.getUsername().equalsIgnoreCase(username));
    }

    @Override
    public boolean esisteEmail(String email) {
        List<Utente> utente = caricaDaFile();
        return utente.stream()
                .anyMatch(a -> a.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipo) {
//        List<Utente> utente = caricaDaFile();
//        utente.add((Utente) utente);
//        salvaSuFile(utente);
    }


    @Override
    public Utente selezionaUtente(String campo, String valore) {

        return selezionaUtente(campo, valore); //non ha senso cancella
    }


    public boolean aggiornaUtente(Utente utenteAggiornato, boolean persistence){
        return true;
    }


    public boolean aggiornaUtente(Utente utente) {
        // leggi → modifica → riscrivi
        return true;
    }




    protected List<Utente> caricaDaFile() {

        Path file = FILE;

        if (!Files.exists(file)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(Files.newInputStream(file))) {

            return (List<Utente>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }


    private void salvaSuFile(List<Agricoltore> agricoltori) {

        try (ObjectOutputStream oos =
                     new ObjectOutputStream(Files.newOutputStream(FILE))) {

            oos.writeObject(agricoltori);

        } catch (IOException e) {
            throw new RuntimeException("Errore scrittura file");
        }
    }

}
