package ua.foxminded.dao.implementation;

import ua.foxminded.models.Course;
import ua.foxminded.models.Student;
import ua.foxminded.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class StudentsToCourses {
    public String addStudentToTheCourse(Student student, Course course) throws SQLException {
        if (student == null || course == null) {
            throw new NoSuchElementException();
        }

        String sqlQuery = "INSERT INTO student_courses (student_id, course_id)" +
                " values (?, ?)";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){

            preparedStatement.setInt(1, student.getId());
            preparedStatement.setInt(2, course.getId());

            preparedStatement.executeUpdate();
        }

        return "Student:" + student.getFirstName() + " " + student.getSecondName() + " was added to " + course.getName();
    }

    public String removeStudentFromCourse(Student student, Course course) throws SQLException{
        if (student == null || course == null) {
            throw new NoSuchElementException();
        }

        String sqlQuery = "DELETE FROM student_courses " +
                "WHERE student_id = ? AND course_id = ?";

        try(Connection connection = ConnectionManager.open();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){

            preparedStatement.setInt(1, student.getId());
            preparedStatement.setInt(2, course.getId());

            preparedStatement.executeUpdate();
        }

        return "Student: " + student.getFirstName() + " " + student.getSecondName() + " from a course: " + course.getName();
    }

}


