package ua.foxminded.dao.implementation;

import ua.foxminded.dao.GroupDao;
import ua.foxminded.models.createmodel.CreateGroup;
import ua.foxminded.models.Group;
import ua.foxminded.util.ConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GroupDaoImplementation implements GroupDao {
    private static final String GROUP_ID = "group_id";
    private static final String GROUP_NAME = "group_name";
    @Override
    public Group save(Group group) throws SQLException {
        String sqlQuery = "UPDATE school.groups set group_name = ? where" +
                " groups.group_id = ?";

        try (Connection connection = ConnectionManager.open();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){

            preparedStatement.setString(1, group.getName());
            preparedStatement.setInt(2, group.getId());

            preparedStatement.executeUpdate();
        }

        return group;
    }

    @Override
    public Group insert(CreateGroup createGroup) throws SQLException {
        Group group = new Group(-1, createGroup.getName());

        String sqlQuery = "INSERT INTO school.groups (group_name) VALUES (?)";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, group.getName());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        group.setId(generatedKeys.getInt(1));
                    }
                }
            } else {
                throw new SQLException("Insertion Failed");
            }
        }

        return group;
    }

    @Override
    public Optional<Group> get(int id) throws SQLException {
        String sqlQuery = "SELECT group_id, group_name " +
                "FROM school.groups " +
                "WHERE group_id = ?";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int groupID = resultSet.getInt(GROUP_ID);
                    String groupName = resultSet.getString(GROUP_NAME);

                    return Optional.of(new Group(groupID, groupName));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Group> getAll() throws SQLException {
        List<Group> groupList = new ArrayList<>();

        String sqlQuery = "SELECT group_id, group_name " +
                "FROM school.groups ";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int groupID = resultSet.getInt(GROUP_NAME);
                    String groupName = resultSet.getString(GROUP_NAME);
                    groupList.add(new Group(groupID, groupName));
                }
            }
        }

        return groupList;
    }

    @Override
    public Group delete(Group group) throws SQLException {
        String sqlQuery = "DELETE FROM school.groups " +
                "WHERE group_id = ?";

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            preparedStatement.setInt(1, group.getId());
            preparedStatement.executeUpdate();
        }

        return group;
    }

    @Override
    public List<Group> findAllGroupsWithLessOrEqualStudentsNumber(int studentsNumber) throws SQLException{
        List<Group> groupList = new ArrayList<>();

        String sqlQuery = "SELECT groups.group_id, groups.group_name," +
                " COUNT(students.student_id) AS student_count\n" +
                "FROM school.groups\n" +
                "LEFT JOIN school.students ON groups.group_id = students.group_id\n" +
                "GROUP BY groups.group_id\n" +
                "HAVING COUNT(students.student_id) <= ?;\n";

        try(Connection connection = ConnectionManager.open();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)){
            preparedStatement.setInt(1, studentsNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    int groupId = resultSet.getInt(GROUP_ID);
                    String groupName = resultSet.getString(GROUP_NAME);
                    groupList.add(new Group(groupId, groupName));
                }
            }
        }

        return groupList;
    }
}
