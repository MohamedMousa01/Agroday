package test;

import engclasses.pattern.ConnessioneDB;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Autore del test : Mohamed Boussaidi
 */

 class TestDB {

    @Test
    void testConnessioneDB() {
        try (Connection conn = ConnessioneDB.getConnection()) {
            assertNotNull(conn, "La connessione non dovrebbe essere null");
            assertFalse(conn.isClosed(), "La connessione non dovrebbe essere chiusa");
        } catch (Exception e) {
            fail("Connessione FALLITA: " + e.getMessage());
        }
    }
}
