package engclasses.dao.db;

import engclasses.dao.api.AppuntamentoDAO;
import engclasses.pattern.ConnessioneDB;
import misc.StatoAppuntamento;
import misc.TipoConsulenza;
import model.Appuntamento;
import model.AppuntamentoOnline;
import model.AppuntamentoInUfficio;
import model.AppuntamentoSulCampo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione database del DAO per gli appuntamenti.
 */
public class AppuntamentoDAODB implements AppuntamentoDAO {

    private static final String TABLE_NAME = "appuntamenti";

    /**
     * Crea la tabella appuntamenti se non esiste.
     */
    public void creaTabella() {
        String sql = """
            CREATE TABLE IF NOT EXISTS appuntamenti (
                id_appuntamento VARCHAR(36) PRIMARY KEY,
                id_cliente VARCHAR(36) NOT NULL,
                id_consulente VARCHAR(36) NOT NULL,
                tipo_consulenza VARCHAR(20) NOT NULL,
                stato VARCHAR(30) NOT NULL,
                data_ora_inizio DATETIME NOT NULL,
                data_ora_fine DATETIME NOT NULL,
                luogo VARCHAR(255),
                note TEXT,
                motivo_cancellazione TEXT,
                data_creazione DATETIME NOT NULL,
                data_ultima_modifica DATETIME NOT NULL,
                google_calendar_event_id VARCHAR(100),
                INDEX idx_cliente (id_cliente),
                INDEX idx_consulente (id_consulente),
                INDEX idx_stato (stato),
                INDEX idx_data_inizio (data_ora_inizio)
            )
            """;

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("[AppuntamentoDAODB] Errore nella creazione tabella: " + e.getMessage());
        }
    }

    @Override
    public boolean salva(Appuntamento appuntamento) {
        String sql = """
            INSERT INTO appuntamenti 
            (id_appuntamento, id_cliente, id_consulente, tipo_consulenza, stato, 
             data_ora_inizio, data_ora_fine, luogo, note, motivo_cancellazione,
             data_creazione, data_ultima_modifica, google_calendar_event_id)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            impostaParametri(pstmt, appuntamento);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[AppuntamentoDAODB] Errore nel salvataggio: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean aggiorna(Appuntamento appuntamento) {
        String sql = """
            UPDATE appuntamenti SET
                tipo_consulenza = ?,
                stato = ?,
                data_ora_inizio = ?,
                data_ora_fine = ?,
                luogo = ?,
                note = ?,
                motivo_cancellazione = ?,
                data_ultima_modifica = ?,
                google_calendar_event_id = ?
            WHERE id_appuntamento = ?
            """;

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, appuntamento.getTipoConsulenza().name());
            pstmt.setString(2, appuntamento.getStato().name());
            pstmt.setTimestamp(3, Timestamp.valueOf(appuntamento.getDataOraInizio()));
            pstmt.setTimestamp(4, Timestamp.valueOf(appuntamento.getDataOraFine()));
            pstmt.setString(5, appuntamento.getLuogo());
            pstmt.setString(6, appuntamento.getNote());
            pstmt.setString(7, appuntamento.getMotivoCancellazione());
            pstmt.setTimestamp(8, Timestamp.valueOf(appuntamento.getDataUltimaModifica()));
            pstmt.setString(9, appuntamento.getGoogleCalendarEventId());
            pstmt.setString(10, appuntamento.getIdAppuntamento());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[AppuntamentoDAODB] Errore nell'aggiornamento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean elimina(String idAppuntamento) {
        String sql = "DELETE FROM appuntamenti WHERE id_appuntamento = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idAppuntamento);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[AppuntamentoDAODB] Errore nell'eliminazione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Appuntamento trovaPerId(String idAppuntamento) {
        String sql = "SELECT * FROM appuntamenti WHERE id_appuntamento = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idAppuntamento);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mappaRigaAdAppuntamento(rs);
            }

        } catch (SQLException e) {
            System.err.println("[AppuntamentoDAODB] Errore nella ricerca per ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Appuntamento> trovaPerCliente(String idCliente) {
        return eseguiQueryLista("SELECT * FROM appuntamenti WHERE id_cliente = ?", idCliente);
    }

    @Override
    public List<Appuntamento> trovaPerConsulente(String idConsulente) {
        return eseguiQueryLista("SELECT * FROM appuntamenti WHERE id_consulente = ?", idConsulente);
    }

    @Override
    public List<Appuntamento> trovaPerStato(StatoAppuntamento stato) {
        return eseguiQueryLista("SELECT * FROM appuntamenti WHERE stato = ?", stato.name());
    }

    @Override
    public List<Appuntamento> trovaPerIntervallo(LocalDateTime da, LocalDateTime a) {
        String sql = "SELECT * FROM appuntamenti WHERE data_ora_inizio BETWEEN ? AND ?";
        List<Appuntamento> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(da));
            pstmt.setTimestamp(2, Timestamp.valueOf(a));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                risultati.add(mappaRigaAdAppuntamento(rs));
            }

        } catch (SQLException e) {
            System.err.println("[AppuntamentoDAODB] Errore nella ricerca per intervallo: " + e.getMessage());
        }
        return risultati;
    }

    @Override
    public List<Appuntamento> trovaPerConsulenteEIntervallo(String idConsulente, LocalDateTime da, LocalDateTime a) {
        String sql = "SELECT * FROM appuntamenti WHERE id_consulente = ? AND data_ora_inizio BETWEEN ? AND ?";
        List<Appuntamento> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idConsulente);
            pstmt.setTimestamp(2, Timestamp.valueOf(da));
            pstmt.setTimestamp(3, Timestamp.valueOf(a));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                risultati.add(mappaRigaAdAppuntamento(rs));
            }

        } catch (SQLException e) {
            System.err.println("[AppuntamentoDAODB] Errore nella ricerca consulente/intervallo: " + e.getMessage());
        }
        return risultati;
    }

    @Override
    public List<Appuntamento> trovaTutti() {
        String sql = "SELECT * FROM appuntamenti ORDER BY data_ora_inizio DESC";
        List<Appuntamento> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                risultati.add(mappaRigaAdAppuntamento(rs));
            }

        } catch (SQLException e) {
            System.err.println("[AppuntamentoDAODB] Errore nel recupero di tutti gli appuntamenti: " + e.getMessage());
        }
        return risultati;
    }

    @Override
    public boolean esisteConflitto(String idConsulente, LocalDateTime inizio, LocalDateTime fine, String escludiId) {
        String sql = """
            SELECT COUNT(*) FROM appuntamenti 
            WHERE id_consulente = ? 
            AND stato IN ('PRENOTATO', 'CONFERMATO', 'IN_CORSO')
            AND data_ora_inizio < ? 
            AND data_ora_fine > ?
            AND (? IS NULL OR id_appuntamento != ?)
            """;

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, idConsulente);
            pstmt.setTimestamp(2, Timestamp.valueOf(fine));
            pstmt.setTimestamp(3, Timestamp.valueOf(inizio));
            pstmt.setString(4, escludiId);
            pstmt.setString(5, escludiId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("[AppuntamentoDAODB] Errore nel controllo conflitti: " + e.getMessage());
        }
        return false;
    }

    // ==================== Metodi di utilità ====================

    private void impostaParametri(PreparedStatement pstmt, Appuntamento app) throws SQLException {
        pstmt.setString(1, app.getIdAppuntamento());
        pstmt.setString(2, app.getIdCliente());
        pstmt.setString(3, app.getIdConsulente());
        pstmt.setString(4, app.getTipoConsulenza().name());
        pstmt.setString(5, app.getStato().name());
        pstmt.setTimestamp(6, Timestamp.valueOf(app.getDataOraInizio()));
        pstmt.setTimestamp(7, Timestamp.valueOf(app.getDataOraFine()));
        pstmt.setString(8, app.getLuogo());
        pstmt.setString(9, app.getNote());
        pstmt.setString(10, app.getMotivoCancellazione());
        pstmt.setTimestamp(11, Timestamp.valueOf(app.getDataCreazione()));
        pstmt.setTimestamp(12, Timestamp.valueOf(app.getDataUltimaModifica()));
        pstmt.setString(13, app.getGoogleCalendarEventId());
    }

    private Appuntamento mappaRigaAdAppuntamento(ResultSet rs) throws SQLException {
        String idAppuntamento = rs.getString("id_appuntamento");
        String idCliente = rs.getString("id_cliente");
        String idConsulente = rs.getString("id_consulente");
        TipoConsulenza tipoConsulenza = TipoConsulenza.valueOf(rs.getString("tipo_consulenza"));
        StatoAppuntamento stato = StatoAppuntamento.valueOf(rs.getString("stato"));
        LocalDateTime dataOraInizio = rs.getTimestamp("data_ora_inizio").toLocalDateTime();
        LocalDateTime dataOraFine = rs.getTimestamp("data_ora_fine").toLocalDateTime();
        String luogo = rs.getString("luogo");
        String note = rs.getString("note");
        String motivoCancellazione = rs.getString("motivo_cancellazione");
        LocalDateTime dataCreazione = rs.getTimestamp("data_creazione").toLocalDateTime();
        LocalDateTime dataUltimaModifica = rs.getTimestamp("data_ultima_modifica").toLocalDateTime();
        String googleCalendarEventId = rs.getString("google_calendar_event_id");

        // Crea la sottoclasse corretta in base al tipo di consulenza (Factory Method)
        return switch (tipoConsulenza) {
            case ONLINE -> new AppuntamentoOnline(idAppuntamento, idCliente, idConsulente, stato,
                    dataOraInizio, dataOraFine, luogo, note, motivoCancellazione,
                    dataCreazione, dataUltimaModifica, googleCalendarEventId);
            case IN_UFFICIO -> new AppuntamentoInUfficio(idAppuntamento, idCliente, idConsulente, stato,
                    dataOraInizio, dataOraFine, luogo, note, motivoCancellazione,
                    dataCreazione, dataUltimaModifica, googleCalendarEventId);
            case SUL_CAMPO -> new AppuntamentoSulCampo(idAppuntamento, idCliente, idConsulente, stato,
                    dataOraInizio, dataOraFine, luogo, note, motivoCancellazione,
                    dataCreazione, dataUltimaModifica, googleCalendarEventId);
        };
    }

    private List<Appuntamento> eseguiQueryLista(String sql, String parametro) {
        List<Appuntamento> risultati = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, parametro);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                risultati.add(mappaRigaAdAppuntamento(rs));
            }

        } catch (SQLException e) {
            System.err.println("[AppuntamentoDAODB] Errore nell'esecuzione query: " + e.getMessage());
        }
        return risultati;
    }
}
