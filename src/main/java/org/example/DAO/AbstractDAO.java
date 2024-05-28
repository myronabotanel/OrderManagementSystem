package org.example.DAO;

import org.example.Connection.ConnectionFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  The AbstractDAO class provides a generic implementation for data access objects.
 * @param <T>
 */
public abstract class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> type;

    /**
     * Constructs a new AbstractDAO instance.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO()
    {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String createSelectQuery(String field)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(type.getSimpleName());
        if (!field.equals("ALL")) {
            sb.append(" WHERE ").append(field).append(" =?");
        }
        return sb.toString();
    }

    private String createInsertQuery()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(type.getSimpleName().toLowerCase()).append(" (");
        for (Field field : type.getDeclaredFields()) {
            if (!field.getName().equals("id")) {
                sb.append(field.getName()).append(",");
            }
        }
        sb.setLength(sb.length() - 1); // Remove last comma
        sb.append(") VALUES (");
        for (Field field : type.getDeclaredFields()) {
            if (!field.getName().equals("id")) {
                sb.append("?,");
            }
        }
        sb.setLength(sb.length() - 1); // Remove last comma
        sb.append(")");
        return sb.toString();
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id The ID of the entity to find.
     * @return The entity with the specified ID, or null if not found.
     */
    public T findById(int id)
    {
        T instance = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(instance, resultSet.getObject(field.getName()));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return instance;
    }

    /**
     * Gets a list of all entity IDs.
     *
     * @return A list of all entity IDs.
     */
    public List<Integer> getIds()
    {
        List<Integer> ids = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT id FROM " + type.getSimpleName();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:getIds " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return ids;
    }


    /**
     * Finds all entities.
     *
     * @return A list of all entities.
     */
    public List<T> findAll()
    {
        List<T> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("ALL");
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            list = createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return list;
    }

    private List<T> createObjects(ResultSet resultSet)
    {
        List<T> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                T instance = type.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);
                    field.set(instance, resultSet.getObject(field.getName()));
                }
                list.add(instance);
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error creating objects " + e.getMessage());
        }
        return list;
    }

    /**
     * Gets the column names of the entity.
     *
     * @return A list of column names.
     */
    public List<String> getColumnNames()
    {
        List<String> columnNames = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            columnNames.add(field.getName());
        }
        return columnNames;
    }

    /**
     * Inserts an entity into the database.
     *
     * @param entity The entity to insert.
     * @return The ID of the inserted entity.
     */
    public int insert(T entity)
    {
        Connection connection = null;
        PreparedStatement statement = null;
        int insertedId = -1;
        String query = createInsertQuery();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            int i = 1;
            for (Field field : type.getDeclaredFields()) {
                if (!field.getName().equals("id")) {
                    field.setAccessible(true);
                    statement.setObject(i++, field.get(entity));
                }
            }
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                insertedId = rs.getInt(1);
            }
        } catch (SQLException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return insertedId;
    }
    /**
     * Updates an entity in the database.
     *
     * @param entity The entity to update.
     * @return The updated entity.
     */
    public T update( T entity)
    {
        Connection connection = null;
        PreparedStatement statement = null;
        StringBuilder query = new StringBuilder("UPDATE ").append(type.getSimpleName()).append(" SET ");
        Field idField = null;
        try {
            // Construim interogarea SQL
            for (Field field : type.getDeclaredFields()) {
                if (!field.getName().equals("id")) {
                    query.append(field.getName()).append("=?,");
                } else {
                    idField = field;
                }
            }
            query.setLength(query.length() - 1); // Eliminăm ultima virgulă
            query.append(" WHERE id=?");

            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query.toString());

            // Setăm valorile pentru interogarea SQL
            int i = 1;
            for (Field field : type.getDeclaredFields()) {
                if (!field.getName().equals("id")) {
                    field.setAccessible(true);
                    statement.setObject(i++, field.get(entity));
                }
            }

            if (idField != null) {
                idField.setAccessible(true);
                statement.setObject(i, idField.get(entity));
            } else {
                throw new IllegalArgumentException("ID field is missing in the entity.");
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return entity;
    }

    /**
     * Deletes an entity from the database by its ID.
     *
     * @param id The ID of the entity to delete.
     * @return True if the entity was deleted successfully, false otherwise.
     */
    public boolean  delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        String query = "DELETE FROM " + type.getSimpleName() + " WHERE id = ?";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0; // Returnează true dacă a fost șters cel puțin un rând
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage());
            return false;
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }



}
