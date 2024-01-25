package ua.foxminded;

import ua.foxminded.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCRunner {
    private static final SchoolApplication application = new SchoolApplication();

    public static void main(String[] args) {
        startApplication();
    }

    private static void startApplication() {
        try (Connection connection = ConnectionManager.open()) {
            application.initializeDatabase(connection);
            application.runApplicationLoop();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
        }
    }
}
