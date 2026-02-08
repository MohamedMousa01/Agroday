package engclasses.dao.db;

import engclasses.pattern.ConnessioneDB;
import org.slf4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Helper class con metodi template comuni per i DAO DB.
 * Riduce duplicazioni nelle query CRUD.
 */
public final class DAOHelper {

    private DAOHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Esegue una query che restituisce un singolo risultato.
     *
     * @param sql query SQL con placeholder
     * @param mapper funzione per mappare ResultSet a oggetto
     * @param logger logger per errori
     * @param params parametri della query
     * @param <T> tipo di ritorno
     * @return oggetto trovato o null
     */
    public static <T> T executeSingleResultQuery(String sql, Function<ResultSet, T> mapper, 
                                                   Logger logger, Object... params) {
        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParameters(pstmt, params);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapper.apply(rs);
            }

        } catch (SQLException e) {
            SQLExceptionHandler.handleReadError(logger, "query singola", e);
        }
        return null;
    }

    /**
     * Esegue una query che restituisce una lista di risultati.
     *
     * @param sql query SQL con placeholder
     * @param mapper funzione per mappare ResultSet a oggetto
     * @param logger logger per errori
     * @param params parametri della query
     * @param <T> tipo di ritorno
     * @return lista di risultati
     */
    public static <T> List<T> executeListQuery(String sql, Function<ResultSet, T> mapper,
                                                 Logger logger, Object... params) {
        List<T> results = new ArrayList<>();

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParameters(pstmt, params);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(mapper.apply(rs));
            }

        } catch (SQLException e) {
            SQLExceptionHandler.handleReadError(logger, "query lista", e);
        }
        return results;
    }

    /**
     * Esegue un update/insert/delete.
     *
     * @param sql query SQL con placeholder
     * @param logger logger per errori
     * @param operazione nome dell'operazione per log
     * @param params parametri della query
     * @return true se operazione riuscita
     */
    public static boolean executeUpdate(String sql, Logger logger, String operazione, Object... params) {
        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setParameters(pstmt, params);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            return SQLExceptionHandler.handleWriteError(logger, operazione, e);
        }
    }

    /**
     * Imposta i parametri nel PreparedStatement.
     */
    private static void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if (param instanceof String) {
                pstmt.setString(i + 1, (String) param);
            } else if (param instanceof Integer) {
                pstmt.setInt(i + 1, (Integer) param);
            } else if (param instanceof java.time.LocalDate) {
                pstmt.setDate(i + 1, Date.valueOf((java.time.LocalDate) param));
            } else if (param instanceof java.time.LocalDateTime) {
                pstmt.setTimestamp(i + 1, Timestamp.valueOf((java.time.LocalDateTime) param));
            } else if (param == null) {
                pstmt.setNull(i + 1, Types.NULL);
            } else {
                pstmt.setObject(i + 1, param);
            }
        }
    }
}
