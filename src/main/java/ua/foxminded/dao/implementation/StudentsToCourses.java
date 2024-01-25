package ua.foxminded.dao.implementation;

import ua.foxminded.models.Course;
import ua.foxminded.models.Student;
import ua.foxminded.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class StudentsToCourses {
    public boolean isStudentInCourse(Student student, Course course) throws SQLException {
        String sqlQuery = "SELECT 1 FROM school.student_courses WHERE student_id = ? AND course_id = ?";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, student.getId());
            preparedStatement.setInt(2, course.getId());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public String addStudentToTheCourse(Student student, Course course) throws SQLException {
        if (student == null || course == null) {
            throw new NoSuchElementException();
        }

        if (isStudentInCourse(student, course)) {
            return "Student " + student.getFirstName() + " " + student.getSecondName() + " is already in Course " + course.getName();
        }

        String sqlQuery = "INSERT INTO school.student_courses (student_id, course_id) values (?, ?)";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, student.getId());
            preparedStatement.setInt(2, course.getId());

            preparedStatement.executeUpdate();
            return "Student " + student.getFirstName() + " " + student.getSecondName() + " successfully added to the course " + course.getName();
        }
    }

    public String removeStudentFromCourse(Student student, Course course) throws SQLException {
        if (student == null || course == null) {
            throw new NoSuchElementException();
        }

        String sqlQuery = "DELETE FROM school.student_courses WHERE student_id = ? AND course_id = ?";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, student.getId());
            preparedStatement.setInt(2, course.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return "Student: " + student.getFirstName() + " " + student.getSecondName() +
                        " successfully removed from the course: " + course.getName();
            } else {
                return "No action taken. Student: " + student.getFirstName() + " " + student.getSecondName() +
                        " was not enrolled in the course: " + course.getName();
            }
        }
    }
}


