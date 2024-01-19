package ua.foxminded;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TableCreation {

    private TableCreation() {
    }

    public static void createTables(Connection connection) throws SQLException {
        executeSqlScript(connection, "create_tables.sql");
        System.out.println("Tables created successfully.");
    }

    public static void dropTables(Connection connection) throws SQLException {
        executeSqlScript(connection, "drop_tables.sql");
        System.out.println("Tables dropped successfully.");
    }

    public static void executeSqlScript(Connection connection, String resourceName) throws SQLException {
        try (InputStream inputStream = TableCreation.class.getClassLoader().getResourceAsStream(resourceName);
             Scanner scanner = new Scanner(inputStream).useDelimiter(";");
             Statement statement = connection.createStatement()) {
            while (scanner.hasNext()) {
                String sql = scanner.next().trim();
                if (!sql.isEmpty()) {
                    statement.execute(sql);
                }
            }
        } catch (IOException e) {
            throw new SQLException("Error loading SQL file: " + resourceName, e);
        }
    }

}
