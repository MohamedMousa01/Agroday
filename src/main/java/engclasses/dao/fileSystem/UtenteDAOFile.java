package main.java.engclasses.dao.fileSystem;

import main.java.engclasses.dao.api.UtenteDAO;
import main.java.misc.PersistenceType;
import main.java.model.Agricoltore;
import main.java.model.Utente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAOFile implements UtenteDAO {

    private static final Path FILE = Paths.get("agricoltori.dat");

    @Override
    public boolean esisteUsername(String username) {
        List<Agricoltore> agricoltori = caricaDaFile();
        return agricoltori.stream()
                .anyMatch(a -> a.getUsername().equalsIgnoreCase(username));
    }

    @Override
    public boolean esisteEmail(String email) {
        List<Agricoltore> agricoltori = caricaDaFile();
        return agricoltori.stream()
                .anyMatch(a -> a.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public void aggiungiUtente(Utente utente) {
        List<Agricoltore> agricoltori = caricaDaFile();
        agricoltori.add((Agricoltore) utente);
        salvaSuFile(agricoltori);
    }

    @Override
    public Utente selezionaUtente(String campo, String valore) {
        return caricaDaFile().stream()
                .filter(a -> matchCampo(a, campo, valore))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean aggiornaUtente(Utente utente) {
        // leggi → modifica → riscrivi
        return true;
    }




    protected List<Utente> caricaDaFile() {

        Path file = getFilePath();

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
