package org.example.BLL;

import org.example.DAO.BillDAO;
import org.example.DAO.ClientDAO;
import org.example.DAO.OrdersDAO;
import org.example.DAO.ProductDAO;
import org.example.Model.Bill;
import org.example.Model.Client;
import org.example.Model.Orders;
import org.example.Model.Product;
import org.example.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The OrderBLL class provides business logic functionalities for managing orders.
 */
public class OrderBLL {
    private List<Validator<Orders>> validatorList;
    private OrdersDAO ordersDAO;
    private BillDAO billDAO;
    private ProductDAO productDAO;

    /**
     * Constructs an OrderBLL object with dependencies.
     *
     * @param validatorList The list of validators for orders.
     * @param ordersDAO     The Data Access Object for orders.
     * @param billDAO       The Data Access Object for bills.
     * @param productDAO    The Data Access Object for products.
     */
    public OrderBLL(List<Validator<Orders>> validatorList, OrdersDAO ordersDAO, BillDAO billDAO, ProductDAO productDAO) {
        this.validatorList = validatorList;
        this.ordersDAO = ordersDAO;
        this.billDAO = billDAO;
        this.productDAO = productDAO;
    }

    /**
     * Constructs an OrderBLL object with default dependencies.
     */
    public OrderBLL() {
        validatorList = new ArrayList<>();
        validatorList.add(new OrderQuantityValidator(new ProductDAO()));
        ordersDAO = new OrdersDAO();
        billDAO = new BillDAO();
        productDAO = new ProductDAO();
    }

    public Orders findOrderById(int id) {
        Orders order = ordersDAO.findById(id);
        if (order == null) {
            throw new NoSuchElementException("The order with id =" + id + " was not found!");
        }
        return order;
    }

    public List<Orders> findAllOrders() {
        return ordersDAO.findAll();
    }

    /**
     * Inserts an order into the system.
     *
     * @param order The order to be inserted.
     * @return The inserted order.
     * @throws IllegalArgumentException If the order could not be inserted or if there are not enough products in stock.
     */
    public Orders insertOrder(Orders order) {
        for (Validator<Orders> validator : validatorList) {
            validator.validate(order);
        }

        Product product = productDAO.findById(order.getIdProduct());
        if (product == null) {
            throw new NoSuchElementException("Product with id =" + order.getIdProduct() + " was not found!");
        }

        // Verificăm dacă există suficiente produse în stoc
        if (product.getQuantity() < order.getQuantity()) {
            throw new IllegalArgumentException("Not enough products in stock!");
        }

        // Actualizăm stocul produsului
        product.setQuantity(product.getQuantity() - order.getQuantity());
        productDAO.update(product);

        int id = ordersDAO.insert(order);
        if (id == -1) {
            throw new IllegalArgumentException("The order could not be inserted!");
        }

        Orders insertedOrder = ordersDAO.findById(id);
        createBillForOrder(insertedOrder);

        return insertedOrder;
    }

    public Orders updateOrder(Orders order) {
        for (Validator<Orders> validator : validatorList) {
            validator.validate(order);
        }
        return ordersDAO.update(order);
    }

    public boolean deleteOrder(int id) {
        return ordersDAO.delete(id);
    }

    public void createBillForOrder(Orders order) {
        int total = calculateTotalForOrder(order);
        ClientDAO clientDAO = new ClientDAO();
        Client client = clientDAO.findById(order.getIdClient());
        Product product = productDAO.findById(order.getIdProduct());
        if (product == null) {
            throw new NoSuchElementException("Product with id =" + order.getIdProduct() + " was not found!");
        }
        if (client == null) {
            throw new NoSuchElementException("Client with id =" + order.getIdClient() + " was not found!");
        }
        Bill bill = new Bill(0, order.getId(), client.getName(), product.getName(), order.getQuantity(), product.getPrice(), total);
        billDAO.insertBill(bill);
    }

    /**
     * Calculates the total cost for the given order.
     *
     * @param order The order for which to calculate the total cost.
     * @return The total cost of the order.
     * @throws NoSuchElementException If the product associated with the order is not found.
     */
    private int calculateTotalForOrder(Orders order) {
        Product product = productDAO.findById(order.getIdProduct());
        if (product == null) {
            throw new NoSuchElementException("Product with id =" + order.getIdProduct() + " was not found!");
        }
        return product.getPrice() * order.getQuantity();
    }
}
