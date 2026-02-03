package test;

import engclasses.pattern.ConnessioneDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class TestDB {

    private static final Logger logger = LoggerFactory.getLogger(TestDB.class);

    public static void main(String[] args) {
        try {
            // Usa ConnessioneDB invece di credenziali hardcoded
            Connection conn = ConnessioneDB.getConnection();

            logger.info("✅ Connessione riuscita!");
            conn.close();

        } catch (Exception e) {
            logger.error("❌ Connessione FALLITA", e);
        }
    }

}
