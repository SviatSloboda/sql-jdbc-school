package ua.foxminded.dao.implementation;

import ua.foxminded.dao.CourseDao;
import ua.foxminded.models.Course;
import ua.foxminded.models.createmodel.CreateCourse;
import ua.foxminded.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDaoImplementation implements CourseDao {
    private static final String COURSE_ID = "course_id";
    private static final String COURSE_NAME = "course_name";
    private static final String COURSE_DESCRIPTION = "course_description";
    @Override
    public Course save(Course course) throws SQLException {
        String sqlQuery = "UPDATE public.courses SET course_name = ?, course_description = ?" +
                " WHERE course_id = ?";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setString(1, course.getName());
            preparedStatement.setString(2, course.getDescription());
            preparedStatement.setInt(3, course.getId());

            preparedStatement.executeUpdate();
        }

        return course;
    }

    @Override
    public Course insert(CreateCourse createCourse) throws SQLException {
        Course course = new Course(-1, createCourse.getName(), createCourse.getDescription());

        String sqlQuery = "INSERT INTO public.courses (course_name, course_description)" +
                " VALUES (?, ?)";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, createCourse.getName());
            preparedStatement.setString(2, createCourse.getDescription());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                    if (keys.next()) {
                        course.setId(keys.getInt(1));
                    }
                }
            } else {
                throw new SQLException("Insertion is not successfully");
            }
        }

        return course;
    }

    @Override
    public Optional<Course> get(int id) throws SQLException {
        String sqlQuery = "SELECT course_id, course_name, course_description " +
                "FROM public.courses " +
                "WHERE course_id = ?";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int courseId = resultSet.getInt(COURSE_ID);
                    String courseName = resultSet.getString(COURSE_NAME);
                    String courseDescription = resultSet.getString(COURSE_DESCRIPTION);

                    return Optional.of(new Course(courseId, courseName, courseDescription));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Course> getAll() throws SQLException {
        List<Course> courseList = new ArrayList<>();

        String sqlQuery = "SELECT course_id, course_name, course_description " +
                "FROM public.courses ";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {


            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int courseId = resultSet.getInt(COURSE_ID);
                    String courseName = resultSet.getString(COURSE_NAME);
                    String courseDescription = resultSet.getString(COURSE_DESCRIPTION);

                    courseList.add(new Course(courseId, courseName, courseDescription));
                }
            }
        }
        return courseList;
    }

    @Override
    public Course delete(Course course) throws SQLException {
        String sqlQuery = "DELETE FROM public.courses " +
                "WHERE course_id = ?";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, course.getId());
            preparedStatement.executeUpdate();
        }
        return course;
    }
}
