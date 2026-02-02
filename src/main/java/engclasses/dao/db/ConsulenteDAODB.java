package engclasses.dao.db;

import engclasses.dao.api.ConsulenteDAO;
import engclasses.exceptions.DatabaseConnessioneFallitaException;
import engclasses.exceptions.DatabaseOperazioneFallitaException;
import engclasses.pattern.ConnessioneDB;
import misc.PersistenceType;
import model.Consulente;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConsulenteDAODB extends UtenteDAODB implements ConsulenteDAO {
    
    // Il metodo aggiungiUtente è ereditato da UtenteDAODB
    // Non serve override, funziona già
    
    @Override
    public List<Consulente> trovaTutti() {
        List<Consulente> consulenti = new ArrayList<>();
        String sql = "SELECT Id, Username, Nome, Cognome, Email, Citta FROM UTENTE WHERE Ruolo = 'CONSULENTE'";
        
        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Consulente consulente = new Consulente(
                    rs.getString("Id"),
                    rs.getString("Username"),
                    rs.getString("Email"),
                    "", // password non necessaria per la lista
                    rs.getString("Nome"),
                    rs.getString("Cognome"),
                    rs.getString("Citta") != null ? rs.getString("Citta") : ""
                );
                consulenti.add(consulente);
            }
            
        } catch (SQLException e) {
            System.err.println("[ConsulenteDAODB] Errore nel recupero dei consulenti: " + e.getMessage());
        }
        
        return consulenti;
    }
}
