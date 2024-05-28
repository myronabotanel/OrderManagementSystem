package org.example.DAO;

import org.example.Model.Bill;
import org.example.Connection.ConnectionFactory;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * The BillDAO class provides data access methods for manipulating Bill entities.
 */
public class BillDAO extends AbstractDAO<Bill> {

    /**
     * Retrieves all bills from the database.
     *
     * @return A list of all bills in the database.
     */
    @Override
    public List<Bill> findAll() {
        List<Bill> bills = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM Bill";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                bills.add(new Bill(
                        resultSet.getInt("id"),
                        resultSet.getInt("idOrder"),
                        resultSet.getString("clientName"),
                        resultSet.getString("productName"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("price"),
                        resultSet.getInt("total")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return bills;
    }

    /**
     * Finds a bill by its associated order ID.
     *
     * @param idOrder The ID of the order associated with the bill.
     * @return The bill with the specified order ID, or null if not found.
     */
    public Bill findByIdOrder(int idOrder) {
        Bill bill = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM Bill WHERE idOrder = ?"; // Modificare aici
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, idOrder);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                bill = new Bill(
                        resultSet.getInt("id"),
                        resultSet.getInt("idOrder"),
                        resultSet.getString("clientName"),
                        resultSet.getString("productName"),
                        resultSet.getInt("quantity"),
                        resultSet.getInt("price"),
                        resultSet.getInt("total")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:findByIdOrder " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return bill;
    }
    /**
     * Inserts a new bill into the database.
     *
     * @param bill The bill to insert.
     * @return The ID of the inserted bill.
     */
    public int insertBill(Bill bill) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int insertedId = -1;
        String query = "INSERT INTO Bill (idOrder, clientName, productName, quantity, price, total) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, bill.idOrder());
            statement.setString(2, bill.clientName());
            statement.setString(3, bill.productName());
            statement.setInt(4, bill.quantity());
            statement.setInt(5, bill.price());
            statement.setInt(6, bill.total());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                insertedId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "BillDAO:insertBill " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return insertedId;
    }

    /**
     * Unsupported operation. Throws UnsupportedOperationException.
     *
     * @param entity The bill entity to update.
     * @return The updated bill entity.
     * @throws UnsupportedOperationException Always thrown to indicate that the operation is not supported.
     */
    @Override
    public Bill update(Bill entity)
    {
        throw new UnsupportedOperationException("Update operation is not supported for Bill.");
    }

    /**
     * Unsupported operation. Throws UnsupportedOperationException.
     *
     * @param id The ID of the bill to delete.
     * @return Always returns false.
     * @throws UnsupportedOperationException Always thrown to indicate that the operation is not supported.
     */
    @Override
    public boolean delete(int id)
    {
        throw new UnsupportedOperationException("Delete operation is not supported for Bill.");
    }
}
