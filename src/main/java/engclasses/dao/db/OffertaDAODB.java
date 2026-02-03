package engclasses.dao.db;

import engclasses.dao.api.OffertaDAO;
import engclasses.pattern.ConnessioneDB;
import model.Offerta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione database del DAO per le offerte.
 */
public class OffertaDAODB implements OffertaDAO {

    private static final Logger logger = LoggerFactory.getLogger(OffertaDAODB.class);

    public void creaTabella() {
        String sql = """
            CREATE TABLE IF NOT EXISTS offerte (
                id_offerta VARCHAR(45) PRIMARY KEY,
                id_annuncio VARCHAR(45) NOT NULL,
                username_venditore VARCHAR(100) NOT NULL,
                prezzo_al_kg DECIMAL(10,2) NOT NULL,
                prezzo_totale DECIMAL(10,2) NOT NULL,
                data_offerta DATETIME NOT NULL,
                stato VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                INDEX idx_annuncio (id_annuncio),
                INDEX idx_venditore (username_venditore),
                INDEX idx_stato (stato)
            )
            """;

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.error("Errore nella creazione tabella offerte", e);
        }
    }

    @Override
    public boolean salva(Offerta offerta) {
        String sql = """
            INSERT INTO offerte 
            (id_offerta, id_annuncio, username_venditore, prezzo_al_kg, prezzo_totale, data_offerta, stato)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, offerta.getIdOfferta());
            pstmt.setString(2, offerta.getIdAnnuncio());
            pstmt.setString(3, offerta.getUsernameVenditore());
            pstmt.setDouble(4, offerta.getPrezzoAlKg());
            pstmt.setDouble(5, offerta.getPrezzoTotale());
            pstmt.setTimestamp(6, Timestamp.valueOf(offerta.getDataOfferta()));
            pstmt.setString(7, offerta.getStato());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Errore durante il salvataggio offerta", e);
            return false;
        }
    }

    @Override
    public boolean elimina(String idOfferta) {
        String sql = "DELETE FROM offerte WHERE id_offerta = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idOfferta);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Errore durante l'eliminazione offerta", e);
            return false;
        }
    }

    @Override
    public Offerta trovaPerId(String idOfferta) {
        String sql = "SELECT id_offerta, id_annuncio, username_venditore, prezzo_al_kg, prezzo_totale, data_offerta, stato FROM offerte WHERE id_offerta = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idOfferta);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mappaRigaAOfferta(rs);
            }

        } catch (SQLException e) {
            logger.error("Errore nella ricerca offerta per ID", e);
        }
        return null;
    }

    @Override
    public List<Offerta> trovaPerAnnuncio(String idAnnuncio) {
        return eseguiQueryLista("SELECT * FROM offerte WHERE id_annuncio = ?", idAnnuncio);
    }

    @Override
    public List<Offerta> trovaPerVenditore(String usernameVenditore) {
        return eseguiQueryLista("SELECT * FROM offerte WHERE username_venditore = ? ORDER BY data_offerta DESC", usernameVenditore);
    }

    @Override
    public List<Offerta> trovaOffertePendingPerVenditore(String usernameVenditore) {
        String sql = "SELECT * FROM offerte WHERE username_venditore = ? AND stato = 'PENDING' ORDER BY data_offerta DESC";
        return eseguiQueryLista(sql, usernameVenditore);
    }

    @Override
    public boolean aggiornaStato(String idOfferta, String nuovoStato) {
        String sql = "UPDATE offerte SET stato = ? WHERE id_offerta = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuovoStato);
            pstmt.setString(2, idOfferta);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Errore durante l'aggiornamento stato offerta", e);
            return false;
        }
    }

    @Override
    public List<Offerta> trovaTutte() {
        String sql = "SELECT id_offerta, id_annuncio, username_venditore, prezzo_al_kg, prezzo_totale, data_offerta, stato FROM offerte ORDER BY data_offerta DESC";
        List<Offerta> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                risultati.add(mappaRigaAOfferta(rs));
            }

        } catch (SQLException e) {
            logger.error("Errore nel recupero di tutte le offerte", e);
        }
        return risultati;
    }

    private Offerta mappaRigaAOfferta(ResultSet rs) throws SQLException {
        String idOfferta = rs.getString("id_offerta");
        String idAnnuncio = rs.getString("id_annuncio");
        String usernameVenditore = rs.getString("username_venditore");
        double prezzoAlKg = rs.getDouble("prezzo_al_kg");
        double prezzoTotale = rs.getDouble("prezzo_totale");
        LocalDateTime dataOfferta = rs.getTimestamp("data_offerta").toLocalDateTime();
        String stato = rs.getString("stato");

        return new Offerta(idOfferta, idAnnuncio, usernameVenditore, prezzoAlKg, prezzoTotale, dataOfferta, stato);
    }

    @Override
    public List<Offerta> trovaOfferteRicevutePerAgricoltore(String usernameAgricoltore) {
        String sql = """
            SELECT o.* 
            FROM offerte o
            JOIN annunci a ON o.id_annuncio COLLATE utf8mb4_unicode_ci = a.idAnnuncio COLLATE utf8mb4_unicode_ci
            WHERE a.nome_autore = ?
            ORDER BY o.data_offerta DESC
            """;
        return eseguiQueryLista(sql, usernameAgricoltore);
    }

    private List<Offerta> eseguiQueryLista(String sql, String parametro) {
        List<Offerta> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, parametro);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                risultati.add(mappaRigaAOfferta(rs));
            }

        } catch (SQLException e) {
            logger.error("Errore durante l'esecuzione query offerte", e);
        }
        return risultati;
    }
}
