package ua.foxminded.dao;

import ua.foxminded.models.Course;
import ua.foxminded.models.createmodel.CreateCourse;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CourseDao {

    Course updateCourse(Course course) throws SQLException;

    Course saveCourse(CreateCourse createCourse) throws SQLException;

    Optional<Course> getCourse(int id) throws SQLException;

    List<Course> getAllCourses() throws SQLException;

    Course deleteCourse(Course course) throws SQLException;
}
