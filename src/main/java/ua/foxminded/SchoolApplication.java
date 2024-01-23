package ua.foxminded;

import ua.foxminded.dao.implementation.CourseDaoImplementation;
import ua.foxminded.dao.implementation.GroupDaoImplementation;
import ua.foxminded.dao.implementation.StudentDaoImplementation;
import ua.foxminded.dao.implementation.StudentsToCourses;
import ua.foxminded.models.Student;
import ua.foxminded.models.createmodel.CreateStudent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SchoolApplication {
    private final GroupDaoImplementation groupDao = new GroupDaoImplementation();
    private final StudentDaoImplementation studentDao = new StudentDaoImplementation();
    private final CourseDaoImplementation courseDao = new CourseDaoImplementation();
    private final StudentsToCourses studentsToCourses = new StudentsToCourses();

    public void initializeDatabase(Connection connection) throws SQLException {
        TableCreation.createSchema(connection);
        TableCreation.dropTables(connection);
        TableCreation.createTables(connection);
        DataGenerator.generateGroups(connection);
        DataGenerator.generateCourses(connection);
        DataGenerator.generateStudents(connection);
        DataGenerator.assignStudentsToGroups(connection);
        DataGenerator.assignCoursesToStudents(connection);
    }

    public void runApplicationLoop() {
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
        } catch (NoSuchElementException e){
            System.out.println("There is no such course!!!");
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
            Student isAdded = studentDao.saveStudent(newStudent);
            if (isAdded != null) {
                System.out.println(isAdded);
                System.out.println("Student successfully added.");
            } else {
                System.out.println("Failed to add the student.");
            }
        } catch (SQLException e) {
            System.out.println("Error while adding student: " + e.getMessage());
        } catch (NoSuchElementException e){
            System.out.println("There is no such group: " + e.getMessage());
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
        } catch (NoSuchElementException e){
            System.out.println("There is no such student");
        }
    }

    private void handleAddStudentToCourse(Scanner scanner) {
        System.out.println("Enter the ID of the student: ");
        int studentId = scanner.nextInt();

        System.out.println("Enter the ID of the course: ");
        int courseId = scanner.nextInt();

        try {
            String resultMessage = studentsToCourses.addStudentToTheCourse(
                    studentDao.getStudent(studentId).orElseThrow(NoSuchElementException::new),
                    courseDao.getCourse(courseId).orElseThrow(NoSuchElementException::new));

            System.out.println(resultMessage);

        } catch (SQLException e) {
            System.out.println("Error while adding student to course: " + e.getMessage());
        } catch (NoSuchElementException e){
            System.out.println("There is no such student or course. Try again!");
        }
    }


    private void handleRemoveStudentFromCourse(Scanner scanner) {
        System.out.println("Enter the ID of the student: ");
        int studentId = scanner.nextInt();

        System.out.println("Enter the ID of the course: ");
        int courseId = scanner.nextInt();

        try {
            String resultMessage = studentsToCourses.removeStudentFromCourse(
                    studentDao.getStudent(studentId).orElseThrow(NoSuchElementException::new),
                    courseDao.getCourse(courseId).orElseThrow(NoSuchElementException::new));

            System.out.println(resultMessage);

        } catch (SQLException e) {
            System.out.println("Error while removing student from course: " + e.getMessage());
        } catch (NoSuchElementException e){
            System.out.println("There is no such student or course. Try again!");
        }
    }
}
