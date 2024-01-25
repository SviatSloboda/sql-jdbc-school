package ua.foxminded.dao.implementation;

import ua.foxminded.dao.StudentDao;
import ua.foxminded.models.Student;
import ua.foxminded.models.createmodel.CreateStudent;
import ua.foxminded.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class StudentDaoImplementation implements StudentDao {
    private static final String STUDENT_ID = "student_id";
    private static final String STUDENT_GROUP_ID = "group_id";
    private static final String STUDENT_FIRST_NAME = "first_name";
    private static final String STUDENT_SECOND_NAME = "last_name";

    @Override
    public Student updateStudent(Student student) throws SQLException {
        String sqlQuery = "UPDATE school.students " + "SET group_id = ?, first_name = ?, last_name = ? " + "WHERE student_id = ?";

        try (Connection connection = ConnectionManager.open(); PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, student.getGroupId());
            preparedStatement.setString(2, student.getFirstName());
            preparedStatement.setString(3, student.getSecondName());
            preparedStatement.setInt(4, student.getId());

            preparedStatement.executeUpdate();
        }

        return student;
    }

    private static void validateGroupExists(int id) throws NoSuchElementException, SQLException {
        GroupDaoImplementation groupDaoImplementation = new GroupDaoImplementation();
        boolean exists = groupDaoImplementation.getAllGroups().stream().anyMatch(group -> group.getId() == id);

        if (!exists) {
            throw new NoSuchElementException("Group with ID " + id + " does not exist.");
        }
    }

    @Override
    public Student saveStudent(CreateStudent createStudent) throws SQLException {
        Student student = new Student(-1, createStudent.getGroupId(), createStudent.getFirstName(), createStudent.getSecondName());

        validateGroupExists(student.getGroupId());

        String insertSql = "INSERT INTO school.students (group_id, first_name, last_name) VALUES (?,?,?)";

        try (Connection connection = ConnectionManager.open(); PreparedStatement preparedStatement = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, createStudent.getGroupId());
            preparedStatement.setString(2, createStudent.getFirstName());
            preparedStatement.setString(3, createStudent.getSecondName());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Insertion failed, no generated keys found.");
                    }
                }
            } else {
                throw new SQLException("Insertion failed, no rows affected.");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode());
        }

        return student;
    }

    @Override
    public Student deleteStudent(Student student) throws SQLException {
        String sqlQuery = "DELETE FROM school.student_courses WHERE student_id = ?;";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, student.getId());
            preparedStatement.executeUpdate();
        }

        sqlQuery = "DELETE FROM school.students WHERE student_id = ?;";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, student.getId());
            preparedStatement.executeUpdate();
        }

        return student;
    }

    @Override
    public Optional<Student> getStudent(int id) throws SQLException {
        String sqlQuery = "SELECT student_id, group_id, first_name, last_name " + "FROM school.students " + "WHERE student_id = ?;";

        try (Connection connection = ConnectionManager.open(); PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int studentId = resultSet.getInt(STUDENT_ID);
                    int studentGroupId = resultSet.getInt(STUDENT_GROUP_ID);
                    String studentFirstName = resultSet.getString(STUDENT_FIRST_NAME);
                    String studentLastName = resultSet.getString(STUDENT_SECOND_NAME);

                    return Optional.of(new Student(studentId, studentGroupId, studentFirstName, studentLastName));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();

        String sqlQuery = "SELECT student_id, group_id, first_name, last_name " + "FROM school.students ";

        try (Connection connection = ConnectionManager.open(); PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int studentId = resultSet.getInt(STUDENT_ID);
                    int studentGroupId = resultSet.getInt(STUDENT_GROUP_ID);
                    String studentFirstName = resultSet.getString(STUDENT_FIRST_NAME);
                    String studentLastName = resultSet.getString(STUDENT_SECOND_NAME);
                    list.add(new Student(studentId, studentGroupId, studentFirstName, studentLastName));
                }
            }
        }

        return list;
    }

    public static void checkCourse(String courseName) throws SQLException {
        GroupDaoImplementation groupDaoImplementation = new GroupDaoImplementation();
        boolean courseExists = groupDaoImplementation.getAllGroups()
                .stream().anyMatch(group -> group.getName().equals(courseName));

        if (!courseExists) throw new NoSuchElementException();
    }

    @Override
    public List<Student> findAllStudentsByCourse(String courseName) throws SQLException {
        checkCourse(courseName);

        List<Student> students = new ArrayList<>();

        String sqlQuery = "select students.student_id, students.group_id, students.first_name,\n" +
                "students.last_name\n" +
                "from school.students\n" +
                "inner join school.student_courses on students.student_id = student_courses.student_id\n" +
                "inner join school.courses on student_courses.course_id = courses.course_id\n" +
                "where courses.course_name like ?";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, courseName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(new Student(
                            resultSet.getInt(STUDENT_ID),
                            resultSet.getInt(STUDENT_GROUP_ID),
                            resultSet.getString(STUDENT_FIRST_NAME),
                            resultSet.getString(STUDENT_SECOND_NAME)
                    ));
                }
            }
        }

        return students;
    }

    @Override
    public Student deleteStudentById(int id) throws SQLException {
        return deleteStudent(getStudent(id)
                .orElseThrow(() -> new NoSuchElementException("There is no such student!")));
    }
}