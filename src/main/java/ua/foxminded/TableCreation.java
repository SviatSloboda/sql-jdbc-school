package ua.foxminded;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TableCreation {
    private TableCreation() {

    }

    public static boolean doTablesExist(Connection connection) throws SQLException {
        DatabaseMetaData metadata = connection.getMetaData();

        ResultSet groupsResultSet = metadata.getTables(null, null, "groups", null);
        boolean groupsTableExists = groupsResultSet.next();
        groupsResultSet.close();

        ResultSet studentsResultSet = metadata.getTables(null, null, "students", null);
        boolean studentsTableExists = studentsResultSet.next();
        studentsResultSet.close();

        ResultSet coursesResultSet = metadata.getTables(null, null, "courses", null);
        boolean coursesTableExists = coursesResultSet.next();
        coursesResultSet.close();

        return groupsTableExists && studentsTableExists && coursesTableExists;
    }

    public static void createTables(Connection connection) throws SQLException {
        if (!doTablesExist(connection)) {
            try (Statement statement = connection.createStatement()) {

            String createTablesScript =
                    "CREATE TABLE public.groups (" +
                            "group_id serial PRIMARY KEY," +
                            "group_name VARCHAR(255)" +
                            ");" +
                            "CREATE TABLE public.students (" +
                            "student_id serial PRIMARY KEY," +
                            "group_id INT," +
                            "first_name VARCHAR(255)," +
                            "last_name VARCHAR(255)," +
                            "FOREIGN KEY (group_id) REFERENCES public.groups(group_id)" +
                            ");" +
                            "CREATE TABLE public.courses (" +
                            "course_id serial PRIMARY KEY," +
                            "course_name VARCHAR(255)," +
                            "course_description VARCHAR(255)" +
                            ");" +
                            "CREATE TABLE public.student_courses (\n" +
                            "    student_id INT,\n" +
                            "    course_id INT,\n" +
                            "    PRIMARY KEY (student_id, course_id),\n" +
                            "    FOREIGN KEY (student_id) REFERENCES public.students(student_id),\n" +
                            "    FOREIGN KEY (course_id) REFERENCES public.courses(course_id)\n" +
                            ");";

            statement.execute(createTablesScript);
        }

            System.out.println("Tables created successfully.");
        }
    }

    public static void dropTables(Connection connection) throws SQLException {
        if (doTablesExist(connection)) {
            try (Statement statement = connection.createStatement()) {
                String dropGroupsTable = "DROP TABLE IF EXISTS groups CASCADE;";
                String dropStudentsTable = "DROP TABLE IF EXISTS students CASCADE;";
                String dropCoursesTable = "DROP TABLE IF EXISTS courses CASCADE;";
                String dropStudentCoursesTable = "DROP TABLE IF EXISTS student_courses CASCADE;";

                statement.execute(dropGroupsTable);
                statement.execute(dropStudentsTable);
                statement.execute(dropCoursesTable);
                statement.execute(dropStudentCoursesTable);
            }

            System.out.println("Tables dropped successfully.");
        }
    }
}

