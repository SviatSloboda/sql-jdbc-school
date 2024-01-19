package ua.foxminded.dao;

import ua.foxminded.models.createmodel.CreateStudent;
import ua.foxminded.models.Student;

import java.sql.SQLException;
import java.util.List;

public interface StudentDao extends Dao<Student, CreateStudent> {
    List<Student> findAllStudentsByCourse(String courseName) throws SQLException;
    Student deleteStudentById(int id) throws SQLException;
}
