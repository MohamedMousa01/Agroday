package engclasses.dao.db;

import engclasses.dao.api.AnnuncioDAO;
import engclasses.pattern.ConnessioneDB;
import model.Annuncio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione database del DAO per gli annunci.
 */
public class AnnuncioDAODB implements AnnuncioDAO {

    private static final Logger logger = LoggerFactory.getLogger(AnnuncioDAODB.class);

    /**
     * Crea la tabella Annunci se non esiste.
     */
    public void creaTabella() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Annunci (
                idAnnuncio VARCHAR(45) PRIMARY KEY,
                nome_autore VARCHAR(100) NOT NULL,
                titolo VARCHAR(200) NOT NULL,
                descrizione VARCHAR(45),
                data_pubblicazione DATE NOT NULL,
                data_scadenza DATE NOT NULL,
                citta VARCHAR(100) NOT NULL,
                quantita_desiderata INT NOT NULL,
                quantita_totale INT NOT NULL DEFAULT 0,
                stato VARCHAR(20) NOT NULL DEFAULT 'ATTIVO',
                INDEX idx_autore (nome_autore),
                INDEX idx_citta (citta),
                INDEX idx_data_scadenza (data_scadenza),
                INDEX idx_stato (stato)
            )
            """;

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.error("Errore nella creazione tabella annunci", e);
        }
    }

    @Override
    public boolean salva(Annuncio annuncio) {
        String sql = """
            INSERT INTO Annunci 
            (idAnnuncio, nome_autore, titolo, descrizione, data_pubblicazione, 
             data_scadenza, citta, quantita_desiderata, quantita_totale, stato)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, annuncio.getIdAnnuncio());
            pstmt.setString(2, annuncio.getAutore());
            pstmt.setString(3, annuncio.getTitolo());
            
            // Tronca la descrizione a 45 caratteri se necessario
            String descrizione = annuncio.getDescrizione();
            if (descrizione != null && descrizione.length() > 45) {
                descrizione = descrizione.substring(0, 45);
            }
            pstmt.setString(4, descrizione);
            
            pstmt.setDate(5, Date.valueOf(annuncio.getDataPubblicazione()));
            pstmt.setDate(6, Date.valueOf(annuncio.getDataScadenza()));
            pstmt.setString(7, annuncio.getCitta());
            pstmt.setInt(8, annuncio.getQuantitaDesiderata());
            pstmt.setInt(9, annuncio.getQuantitaTotale());
            pstmt.setString(10, annuncio.getStato());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Errore nel salvataggio annuncio", e);
            return false;
        }
    }

    @Override
    public boolean elimina(String idAnnuncio) {
        String sql = "DELETE FROM Annunci WHERE idAnnuncio = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idAnnuncio);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Errore nell'eliminazione annuncio", e);
            return false;
        }
    }

    @Override
    public Annuncio trovaPerId(String idAnnuncio) {
        String sql = "SELECT idAnnuncio, nome_autore, titolo, descrizione, " +
                     "data_pubblicazione, data_scadenza, citta, quantita_desiderata, " +
                     "quantita_totale, stato FROM Annunci WHERE idAnnuncio = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idAnnuncio);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mappaRigaAdAnnuncio(rs);
            }

        } catch (SQLException e) {
            logger.error("Errore nella ricerca annuncio per ID", e);
        }
        return null;
    }

    @Override
    public List<Annuncio> trovaPerAutore(String autore) {
        String sql = "SELECT idAnnuncio, nome_autore, titolo, descrizione, " +
                     "data_pubblicazione, data_scadenza, citta, quantita_desiderata, " +
                     "quantita_totale, stato FROM Annunci WHERE nome_autore = ?";
        return eseguiQueryLista(sql, autore);
    }

    @Override
    public List<Annuncio> trovaPerCitta(String citta) {
        String sql = "SELECT idAnnuncio, nome_autore, titolo, descrizione, " +
                     "data_pubblicazione, data_scadenza, citta, quantita_desiderata, " +
                     "quantita_totale, stato FROM Annunci WHERE citta = ?";
        return eseguiQueryLista(sql, citta);
    }

    @Override
    public List<Annuncio> trovaTutti() {
        String sql = "SELECT idAnnuncio, nome_autore, titolo, descrizione, " +
                     "data_pubblicazione, data_scadenza, citta, quantita_desiderata, " +
                     "quantita_totale, stato FROM Annunci ORDER BY data_pubblicazione DESC";
        List<Annuncio> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                risultati.add(mappaRigaAdAnnuncio(rs));
            }

        } catch (SQLException e) {
            logger.error("Errore nel recupero di tutti gli annunci", e);
        }
        return risultati;
    }

    @Override
    public List<Annuncio> trovaAttivi() {
        String sql = "SELECT idAnnuncio, nome_autore, titolo, descrizione, " +
                     "data_pubblicazione, data_scadenza, citta, quantita_desiderata, " +
                     "quantita_totale, stato FROM Annunci " +
                     "WHERE data_scadenza >= CURDATE() ORDER BY data_pubblicazione DESC";
        List<Annuncio> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                risultati.add(mappaRigaAdAnnuncio(rs));
            }

        } catch (SQLException e) {
            logger.error("Errore nel recupero degli annunci attivi", e);
        }
        return risultati;
    }

    // ==================== Metodi di utilità ====================

    private Annuncio mappaRigaAdAnnuncio(ResultSet rs) throws SQLException {
        String idAnnuncio = rs.getString("idAnnuncio");
        String nomeAutore = rs.getString("nome_autore");
        String titolo = rs.getString("titolo");
        String descrizione = rs.getString("descrizione");
        LocalDate dataPubblicazione = rs.getDate("data_pubblicazione").toLocalDate();
        LocalDate dataScadenza = rs.getDate("data_scadenza").toLocalDate();
        String citta = rs.getString("citta");
        int quantitaDesiderata = rs.getInt("quantita_desiderata");

        // Usa il Builder per creare l'annuncio dal database
        return new Annuncio.Builder(nomeAutore, titolo, descrizione)
            .idAnnuncio(idAnnuncio)
            .dataPubblicazione(dataPubblicazione)
            .dataScadenza(dataScadenza)
            .citta(citta)
            .quantitaDesiderata(quantitaDesiderata)
            .build();
    }

    private List<Annuncio> eseguiQueryLista(String sql, String parametro) {
        List<Annuncio> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, parametro);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                risultati.add(mappaRigaAdAnnuncio(rs));
            }

        } catch (SQLException e) {
            logger.error("Errore nell'esecuzione query annunci", e);
        }
        return risultati;
    }
}
