package ua.foxminded;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DataGenerator {
    private static final Random random = new Random();

    private DataGenerator() {
    }

    public static void generateGroups(Connection connection) throws SQLException {
        String sql = "INSERT INTO groups (group_name) VALUES (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < 10; i++) {
                String groupName = "GR-" + (i + 10);
                preparedStatement.setString(1, groupName);
                preparedStatement.executeUpdate();
            }
        }
    }

    public static void generateCourses(Connection connection) throws SQLException {
        String sql = "INSERT INTO courses (course_name) VALUES (?)";

        String[] courses = {"Math", "Biology", "Chemistry", "Physics", "History",
                "English", "Art", "Computer Science", "Economics", "Music"};

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (String course : courses) {
                preparedStatement.setString(1, course);
                preparedStatement.executeUpdate();
            }
        }
    }

    public static void generateStudents(Connection connection) throws SQLException {
        String sql = "INSERT INTO students (first_name, last_name) VALUES (?, ?)";

        String[] firstNames = {
                "Liam", "Olivia", "Noah", "Emma", "Oliver", "Ava", "Elijah", "Charlotte", "William", "Sophia",
                "James", "Amelia", "Benjamin", "Isabella", "Lucas", "Mia", "Henry", "Evelyn", "Alexander", "Harper"
        };

        String[] lastNames = {
                "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez",
                "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"
        };

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 0; i < 200; i++) {
                String firstName = firstNames[random.nextInt(firstNames.length)];
                String lastName = lastNames[random.nextInt(lastNames.length)];
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.executeUpdate();
            }
        }
    }

    public static void assignStudentsToGroups(Connection connection) throws SQLException {
        String updateSql = "UPDATE students SET group_id = ? WHERE student_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
            for (int studentId = 1; studentId <= 200; studentId++) {
                int groupId = random.nextInt(10) + 1;
                preparedStatement.setInt(1, groupId);
                preparedStatement.setInt(2, studentId);
                preparedStatement.executeUpdate();
            }
        }
    }

    public static void assignCoursesToStudents(Connection connection) throws SQLException {
        String insertSql = "INSERT INTO student_courses (student_id, course_id) VALUES (?, ?) ON CONFLICT DO NOTHING";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
            for (int studentId = 1; studentId <= 200; studentId++) {
                Set<Integer> assignedCourses = new HashSet<>();
                int coursesCount = random.nextInt(3) + 1;

                for (int i = 0; i < coursesCount; i++) {
                    int courseId;

                    do {
                        courseId = random.nextInt(10) + 1;
                    } while (assignedCourses.contains(courseId));
                    assignedCourses.add(courseId);

                    preparedStatement.setInt(1, studentId);
                    preparedStatement.setInt(2, courseId);
                    preparedStatement.executeUpdate();
                }
            }
        }
    }
}
