package ua.foxminded;

import ua.foxminded.dao.implementation.*;
import ua.foxminded.models.Student;
import ua.foxminded.models.createmodel.CreateStudent;
import ua.foxminded.util.ConnectionManager;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class JDBCRunner {

    private final GroupDaoImplementation groupDao;
    private final StudentDaoImplementation studentDao;
    private final CourseDaoImplementation courseDao;
    private final StudentsToCourses studentsToCourses;

    public JDBCRunner(GroupDaoImplementation groupDao, StudentDaoImplementation studentDao, CourseDaoImplementation courseDao, StudentsToCourses studentsToCourses) {
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.courseDao = courseDao;
        this.studentsToCourses = studentsToCourses;
    }

    public static void main(String[] args) {
        JDBCRunner app = new JDBCRunner(new GroupDaoImplementation(), new StudentDaoImplementation(), new CourseDaoImplementation(), new StudentsToCourses());
        app.startApplication();
    }

    private void startApplication() {
        try (Connection connection = ConnectionManager.open()) {
            initializeDatabase(connection);
            runApplicationLoop();
        } catch (SQLException e) {
            System.out.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeDatabase(Connection connection) throws SQLException, IOException {
        TableCreation.createSchema(connection);
        TableCreation.dropTables(connection);
        TableCreation.createTables(connection);
        DataGenerator.generateGroups(connection);
        DataGenerator.generateCourses(connection);
        DataGenerator.generateStudents(connection);
        DataGenerator.assignStudentsToGroups(connection);
        DataGenerator.assignCoursesToStudents(connection);
    }

    private void runApplicationLoop() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                showMenu();
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        handleFindGroups(scanner);
                        break;
                    case 2:
                        handleFindStudents(scanner);
                        break;
                    case 3:
                        handleAddStudent(scanner);
                        break;
                    case 4:
                        handleDeleteStudent(scanner);
                        break;
                    case 5:
                        handleAddStudentToCourse(scanner);
                        break;
                    case 6:
                        handleRemoveStudentFromCourse(scanner);
                        break;
                    case 7:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    private void showMenu() {
        System.out.println("\n1. Find all groups with less or equal students’ number\n" +
                "2. Find all students related to the course with the given name\n" +
                "3. Add a new student\n" +
                "4. Delete a student by the STUDENT_ID\n" +
                "5. Add a student to the course (from a list)\n" +
                "6. Remove the student from one of their courses.\n" +
                "7. Stop working\n");
        System.out.println("Choose an action: ");
    }

    private void handleFindGroups(Scanner scanner) {
        System.out.println("Enter the maximum number of students: ");
        int maxStudents = scanner.nextInt();
        try {
            System.out.println(groupDao.findAllGroupsWithLessOrEqualStudentsNumber(maxStudents));
        } catch (SQLException e) {
            System.out.println("Error while finding groups: " + e.getMessage());
        }
    }

    private void handleFindStudents(Scanner scanner) {
        System.out.println("Enter the course name: ");
        String courseName = scanner.next();
        try {
            System.out.println(studentDao.findAllStudentsByCourse(courseName));
        } catch (SQLException e) {
            System.out.println("Error while finding students: " + e.getMessage());
        }
    }

    private void handleAddStudent(Scanner scanner) {
        System.out.println("Enter the first name of the student: ");
        String firstName = scanner.next();
        System.out.println("Enter the last name of the student: ");
        String lastName = scanner.next();
        System.out.println("Enter the ID of the student's group: ");
        int groupId = scanner.nextInt();
        CreateStudent newStudent = new CreateStudent(groupId, firstName, lastName);
        try {
            Student isAdded = studentDao.insert(newStudent);
            if (isAdded != null) {
                System.out.println(isAdded);
                System.out.println("Student successfully added.");
            } else {
                System.out.println("Failed to add the student.");
            }
        } catch (SQLException e) {
            System.out.println("Error while adding student: " + e.getMessage());
        }
    }

    private void handleDeleteStudent(Scanner scanner) {
        System.out.println("Enter the ID of the student to delete: ");
        int studentId = scanner.nextInt();
        try {
            Student isDeleted = studentDao.deleteStudentById(studentId);
            if (isDeleted != null) {
                System.out.println(isDeleted);
                System.out.println("Student successfully deleted.");
            } else {
                System.out.println("Failed to delete the student.");
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting student: " + e.getMessage());
        }
    }

    private void handleAddStudentToCourse(Scanner scanner) {
        System.out.println("Enter the ID of the student: ");
        int studentId = scanner.nextInt();
        System.out.println("Enter the ID of the course: ");
        int courseId = scanner.nextInt();
        try {
            String isAdded = studentsToCourses.addStudentToTheCourse
                    (studentDao.get(studentId).orElseThrow(NoSuchElementException::new),
                            courseDao.get(courseId).orElseThrow(NoSuchElementException::new));

            if (isAdded != null) {
                System.out.println(isAdded);
                System.out.println("Student successfully added to the course.");
            } else {
                System.out.println("Failed to add student to the course.");
            }
        } catch (SQLException e) {
            System.out.println("Error while adding student to course: " + e.getMessage());
        }
    }

    private void handleRemoveStudentFromCourse(Scanner scanner) {
        System.out.println("Enter the ID of the student: ");
        int studentId = scanner.nextInt();
        System.out.println("Enter the ID of the course: ");
        int courseId = scanner.nextInt();
        try {
            String isRemoved = studentsToCourses.removeStudentFromCourse(
                    studentDao.get(studentId).orElseThrow(NoSuchElementException::new),
                    courseDao.get(courseId).orElseThrow(NoSuchElementException::new)
            );

            if (isRemoved != null) {
                System.out.println(isRemoved);
                System.out.println("Student successfully removed from the course.");
            } else {
                System.out.println("Failed to remove student from the course.");
            }
        } catch (SQLException e) {
            System.out.println("Error while removing student from course: " + e.getMessage());
        }
    }

}
