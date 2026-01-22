package engclasses.dao.db;

import engclasses.beans.RegistrazioneBean;
import engclasses.dao.api.UtenteDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import engclasses.pattern.ConnessioneDB;
import engclasses.pattern.Factory.UtenteFactory;
import engclasses.pattern.Factory.UtenteFactoryProvider;
import misc.PersistenceType;
import misc.TipoUtente;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UtenteDAODB implements UtenteDAO {

    @Override
    public boolean esisteUsername(String username) throws DatabaseConnessioneFallitaException  {

        String sql = """
        SELECT 1
        FROM UTENTE
        WHERE Username = ?
        LIMIT 1
    """;

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);      // ✅ PRIMA
            ResultSet rs = ps.executeQuery(); // ✅ POI

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace(); // Aggiunto per debug
            throw new DatabaseConnessioneFallitaException(
                    "errore in esisteUsername() di UtenteDAODB", e);
        }
    }

    @Override
    public boolean esisteEmail(String email)
            throws DatabaseConnessioneFallitaException {

        String sql = """
        SELECT 1
        FROM UTENTE
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
            INSERT INTO UTENTE (Id, Username, Email, Password, Nome, Cognome, Citta, Ruolo)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
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
            ps.setString(8, utente.getTipo().toString());

            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new DatabaseOperazioneFallitaException(" errore in aggiungi Utente in UtenteDAODB ",e);
        }

    }



    @Override
    public Utente selezionaUtente(String username, String password) throws DatabaseConnessioneFallitaException, DatabaseOperazioneFallitaException {
        String sql = """
            SELECT Id, Username, Email, Password, Nome, Cognome, Citta, Ruolo
            FROM UTENTE
            WHERE Username = ? AND Password = ?
            LIMIT 1
        """;

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();


            if (rs.next()) {

                RegistrazioneBean bean = new RegistrazioneBean();  //posso leggere il database, salvare i dati in un bean, e poi passare i bean al metodo creaUtente()

                bean.setIdUtente(rs.getString("Id"));
                bean.setUsername(rs.getString("Username"));
                bean.setEmail(rs.getString("Email"));
                bean.setPassword(rs.getString("Password"));
                bean.setNome(rs.getString("Nome"));
                bean.setCognome(rs.getString("Cognome"));
                bean.setCitta(rs.getString("Citta"));
                // Assumendo che TipoUtente sia un enum o una String nel modello Utente
                bean.setTipoUtente(TipoUtente.valueOf(rs.getString("TipoUtente")));

                TipoUtente ruolo = TipoUtente.valueOf(rs.getString("ruolo"));
                UtenteFactory factory = UtenteFactoryProvider.getFactory(ruolo);
                Utente utente = factory.creaUtente(rs.getString("Id"),bean);

                return utente;
            }
            return null; // Utente non trovato
        } catch (SQLException e) {
            throw new DatabaseOperazioneFallitaException("Errore durante la selezione utente per login.", e);
        }
    }

    @Override
    public boolean aggiornaUtente(Utente utenteAggiornato, boolean persistence){
        return true;
    }

}
