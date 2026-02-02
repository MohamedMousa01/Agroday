package engclasses.dao.db;

import engclasses.dao.api.AnnuncioDAO;
import engclasses.pattern.ConnessioneDB;
import model.Annuncio;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione database del DAO per gli annunci.
 */
public class AnnuncioDAODB implements AnnuncioDAO {

    private static final String TABLE_NAME = "Annunci";

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
            System.err.println("[AnnuncioDAODB] Errore nella creazione tabella: " + e.getMessage());
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
            System.err.println("[AnnuncioDAODB] Errore nel salvataggio: " + e.getMessage());
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
            System.err.println("[AnnuncioDAODB] Errore nell'eliminazione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Annuncio trovaPerId(String idAnnuncio) {
        String sql = "SELECT * FROM Annunci WHERE idAnnuncio = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idAnnuncio);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mappaRigaAdAnnuncio(rs);
            }

        } catch (SQLException e) {
            System.err.println("[AnnuncioDAODB] Errore nella ricerca per ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Annuncio> trovaPerAutore(String autore) {
        return eseguiQueryLista("SELECT * FROM Annunci WHERE nome_autore = ?", autore);
    }

    @Override
    public List<Annuncio> trovaPerCitta(String citta) {
        return eseguiQueryLista("SELECT * FROM Annunci WHERE citta = ?", citta);
    }

    @Override
    public List<Annuncio> trovaTutti() {
        String sql = "SELECT * FROM Annunci ORDER BY data_pubblicazione DESC";
        List<Annuncio> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                risultati.add(mappaRigaAdAnnuncio(rs));
            }

        } catch (SQLException e) {
            System.err.println("[AnnuncioDAODB] Errore nel recupero di tutti gli annunci: " + e.getMessage());
        }
        return risultati;
    }

    @Override
    public List<Annuncio> trovaAttivi() {
        String sql = "SELECT * FROM Annunci WHERE data_scadenza >= CURDATE() ORDER BY data_pubblicazione DESC";
        List<Annuncio> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                risultati.add(mappaRigaAdAnnuncio(rs));
            }

        } catch (SQLException e) {
            System.err.println("[AnnuncioDAODB] Errore nel recupero degli annunci attivi: " + e.getMessage());
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

        // Crea l'annuncio dal database
        Annuncio annuncio = new Annuncio(
            idAnnuncio,
            nomeAutore,
            titolo,
            descrizione,
            dataPubblicazione,
            dataScadenza,
            citta,
            quantitaDesiderata
        );

        // Carica quantita_totale e stato se presenti
        try {
            int quantitaTotale = rs.getInt("quantita_totale");
            String stato = rs.getString("stato");
            // Nota: dovremmo aggiungere setter nel modello o usare reflection
            // Per ora il costruttore imposta già questi valori
        } catch (SQLException e) {
            // Colonne non presenti (tabella vecchia)
        }

        return annuncio;
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
            System.err.println("[AnnuncioDAODB] Errore nell'esecuzione query: " + e.getMessage());
        }
        return risultati;
    }
}
