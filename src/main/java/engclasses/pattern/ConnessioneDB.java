package engclasses.pattern;

import engclasses.exceptions.DatabaseConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnessioneDB {

    private static final Logger logger = LoggerFactory.getLogger(ConnessioneDB.class);
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConnessioneDB.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            if (input == null) {
                logger.error("Unable to find database.properties");
                throw new DatabaseConfigurationException("database.properties not found in classpath");
            }
            properties.load(input);
            logger.info("Database configuration loaded successfully");
        } catch (IOException e) {
            throw new DatabaseConfigurationException("Failed to load database configuration", e);
        }
    }

    private ConnessioneDB() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");
        
        logger.debug("Attempting database connection to: {}", url);
        return DriverManager.getConnection(url, user, password);
    }
}