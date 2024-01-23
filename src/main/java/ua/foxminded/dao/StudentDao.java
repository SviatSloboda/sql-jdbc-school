package ua.foxminded.dao;

import ua.foxminded.models.createmodel.CreateStudent;
import ua.foxminded.models.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StudentDao {
    Student updateStudent(Student student) throws SQLException;

    Student saveStudent(CreateStudent createStudent) throws SQLException;

    Student deleteStudent(Student student) throws SQLException;

    Optional<Student> getStudent(int id) throws SQLException;

    List<Student> getAllStudents() throws SQLException;

    List<Student> findAllStudentsByCourse(String courseName) throws SQLException;
    Student deleteStudentById(int id) throws SQLException;
}
