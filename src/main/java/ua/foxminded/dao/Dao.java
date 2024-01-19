package ua.foxminded.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T, C> {
    T save(T obj) throws SQLException;

    T insert(C obj) throws SQLException;

    Optional<T> get(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    T delete(T obj) throws SQLException;
}
