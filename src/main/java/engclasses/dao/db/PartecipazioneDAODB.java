package engclasses.dao.db;

import engclasses.dao.api.PartecipazioneDAO;
import engclasses.pattern.ConnessioneDB;
import model.Partecipazione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione database del DAO per le partecipazioni.
 */
public class PartecipazioneDAODB implements PartecipazioneDAO {

    private static final String TABLE_NAME = "partecipazioni";

    /**
     * Crea la tabella partecipazioni se non esiste.
     */
    public void creaTabella() {
        String sql = """
            CREATE TABLE IF NOT EXISTS partecipazioni (
                id_partecipazione VARCHAR(45) PRIMARY KEY,
                id_annuncio VARCHAR(45) NOT NULL,
                id_agricoltore VARCHAR(100) NOT NULL,
                quantita_richiesta INT NOT NULL,
                data_partecipazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                INDEX idx_annuncio (id_annuncio),
                INDEX idx_agricoltore (id_agricoltore),
                UNIQUE KEY uk_annuncio_agricoltore (id_annuncio, id_agricoltore)
            )
            """;

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[PartecipazioneDAODB] Errore nella creazione tabella: " + e.getMessage());
        }
    }

    @Override
    public boolean salva(Partecipazione partecipazione) {
        String sql = """
            INSERT INTO partecipazioni 
            (id_partecipazione, id_annuncio, id_agricoltore, quantita_richiesta)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, partecipazione.getIdPartecipazione());
            pstmt.setString(2, partecipazione.getIdAnnuncio());
            pstmt.setString(3, partecipazione.getIdAgricoltore());
            pstmt.setInt(4, partecipazione.getQuantitaRichiesta());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[PartecipazioneDAODB] Errore nel salvataggio: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean elimina(String idPartecipazione) {
        String sql = "DELETE FROM partecipazioni WHERE id_partecipazione = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idPartecipazione);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[PartecipazioneDAODB] Errore nell'eliminazione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Partecipazione trovaPerId(String idPartecipazione) {
        String sql = "SELECT * FROM partecipazioni WHERE id_partecipazione = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idPartecipazione);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mappaRigaAPartecipazione(rs);
            }

        } catch (SQLException e) {
            System.err.println("[PartecipazioneDAODB] Errore nella ricerca per ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Partecipazione> trovaPerAnnuncio(String idAnnuncio) {
        String sql = "SELECT * FROM partecipazioni WHERE id_annuncio = ?";
        List<Partecipazione> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idAnnuncio);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                risultati.add(mappaRigaAPartecipazione(rs));
            }

        } catch (SQLException e) {
            System.err.println("[PartecipazioneDAODB] Errore nella ricerca per annuncio: " + e.getMessage());
        }
        return risultati;
    }

    @Override
    public List<Partecipazione> trovaPerAgricoltore(String idAgricoltore) {
        String sql = "SELECT * FROM partecipazioni WHERE id_agricoltore = ?";
        List<Partecipazione> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idAgricoltore);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                risultati.add(mappaRigaAPartecipazione(rs));
            }

        } catch (SQLException e) {
            System.err.println("[PartecipazioneDAODB] Errore nella ricerca per agricoltore: " + e.getMessage());
        }
        return risultati;
    }

    @Override
    public boolean haPartecipatoGia(String idAnnuncio, String idAgricoltore) {
        String sql = "SELECT COUNT(*) FROM partecipazioni WHERE id_annuncio = ? AND id_agricoltore = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idAnnuncio);
            pstmt.setString(2, idAgricoltore);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("[PartecipazioneDAODB] Errore nella verifica partecipazione: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Partecipazione> trovaTutte() {
        String sql = "SELECT * FROM partecipazioni";
        List<Partecipazione> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                risultati.add(mappaRigaAPartecipazione(rs));
            }

        } catch (SQLException e) {
            System.err.println("[PartecipazioneDAODB] Errore nel recupero di tutte le partecipazioni: " + e.getMessage());
        }
        return risultati;
    }

    private Partecipazione mappaRigaAPartecipazione(ResultSet rs) throws SQLException {
        String idPartecipazione = rs.getString("id_partecipazione");
        String idAnnuncio = rs.getString("id_annuncio");
        String idAgricoltore = rs.getString("id_agricoltore");
        int quantitaRichiesta = rs.getInt("quantita_richiesta");

        return new Partecipazione(idPartecipazione, idAnnuncio, idAgricoltore, quantitaRichiesta);
    }
}
