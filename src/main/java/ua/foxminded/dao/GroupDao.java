package ua.foxminded.dao;

import ua.foxminded.models.Group;
import ua.foxminded.models.createmodel.CreateGroup;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GroupDao {
    Group updateGroup(Group group) throws SQLException;

    Group saveGroup(CreateGroup createGroup) throws SQLException;

    Optional<Group> getGroup(int id) throws SQLException;

    List<Group> getAllGroups() throws SQLException;

    Group deleteGroup(Group group) throws SQLException;

    List<Group> findAllGroupsWithLessOrEqualStudentsNumber(int studentsNumber) throws SQLException;
}
