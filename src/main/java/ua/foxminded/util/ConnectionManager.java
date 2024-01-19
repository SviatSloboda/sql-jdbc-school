package ua.foxminded.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class ConnectionManager {
    private static final String DB_URL = "jdbc:postgresql://localhost/school_db";
    private static final String DB_USERNAME = "school_user";
    private static final String DB_PASSWORD = "1234";

    static {
        loadDriver();
    }

    private ConnectionManager() {
    }
    public static void initializeDatabase() {
        String sqlFilePath = "path/to/your/create_database.sql"; // Adjust the path to where your SQL file is.
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/", "postgres", "postgres_password");
             Statement statement = conn.createStatement()) {

            String sql = new String(Files.readAllBytes(Paths.get(sqlFilePath)));
            statement.execute(sql);

            System.out.println("Database and user created successfully.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize the database", e);
        }
    }
    public static Connection open() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
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
