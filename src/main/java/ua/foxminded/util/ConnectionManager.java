package ua.foxminded.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final String DB_DEFAULT_URL = "jdbc:postgresql://localhost/school_db";
    private static final String DB_DEFAULT_USERNAME = "postgres";
    private static final String DB_DEFAULT_PASSWORD = "1234";

    static {
        loadDriver();
    }

    private ConnectionManager() {
    }

    public static Connection openSuperuser() throws SQLException {
        return DriverManager.getConnection(DB_DEFAULT_URL, DB_DEFAULT_USERNAME, DB_DEFAULT_PASSWORD);
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(DB_DEFAULT_URL, DB_DEFAULT_USERNAME, DB_DEFAULT_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open database connection", e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC driver not found", e);
        }
    }
}
