package ua.foxminded.dao;

import ua.foxminded.models.createmodel.CreateGroup;
import ua.foxminded.models.Group;

import java.sql.SQLException;
import java.util.List;

public interface GroupDao extends Dao<Group, CreateGroup> {
    List<Group> findAllGroupsWithLessOrEqualStudentsNumber(int studentsNumber) throws SQLException;
}
