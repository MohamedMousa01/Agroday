package main.java.engclasses.dao.db;

import main.java.engclasses.dao.api.UtenteDAO;
import main.java.engclasses.exceptions.DatabaseConnessioneFallitaException;
import main.java.engclasses.exceptions.DatabaseOperazioneFallitaException;
import main.java.engclasses.pattern.ConnessioneDB;
import main.java.misc.PersistenceType;
import main.java.model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UtenteDAODB implements UtenteDAO {

    @Override
    public boolean esisteUsername(String username) throws DatabaseConnessioneFallitaException  {

        String sql = """
        SELECT 1
        FROM MOHAMED
        WHERE Username = ?
        LIMIT 1
    """;

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);      // ✅ PRIMA
            ResultSet rs = ps.executeQuery(); // ✅ POI

            return rs.next();
        } catch (SQLException e) {
            throw new DatabaseConnessioneFallitaException(
                    "errore in esisteUsername() di UtenteDAODB", e);
        }
    }

    @Override
    public boolean esisteEmail(String email)
            throws DatabaseConnessioneFallitaException {

        String sql = """
        SELECT 1
        FROM MOHAMED
        WHERE Email = ?
        LIMIT 1
    """;

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);      // ✅ PRIMA
            ResultSet rs = ps.executeQuery(); // ✅ POI

            return rs.next();
        } catch (SQLException e) {
            throw new DatabaseConnessioneFallitaException(
                    "errore in esisteEmail() di UtenteDAODB", e);
        }

    }

    @Override
    public void aggiungiUtente(Utente utente, PersistenceType tipoPersistenza)
            throws DatabaseOperazioneFallitaException {

        String sql = """
            INSERT INTO MOHAMED (Id, Username, Email, Password, Nome, Cognome, Citta)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, utente.getIdUtente());
            ps.setString(2, utente.getUsername());
            ps.setString(3, utente.getEmail());
            ps.setString(4, utente.getPassword());
            ps.setString(5, utente.getNome());
            ps.setString(6, utente.getCognome());
            ps.setString(7, utente.getCitta());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseOperazioneFallitaException(" errore in aggiungi Utente in UtenteDAODB ",e);
        }

    }



    @Override
    public Utente selezionaUtente(String campo, String valore, boolean persistence) {
        return selezionaUtente(campo, valore, persistence);

    }

    @Override
    public boolean aggiornaUtente(Utente utenteAggiornato, boolean persistence){
        return true;
    }

}
