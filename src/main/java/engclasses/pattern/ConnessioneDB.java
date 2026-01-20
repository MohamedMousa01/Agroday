package main.java.engclasses.pattern;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class ConnessioneDB {

        private static final String URL =
                "jdbc:mysql://localhost:3306/MOHAMED?useSSL=false&serverTimezone=UTC";
        private static final String USER = "root";
        private static final String PASSWORD = "Mac.0123";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }