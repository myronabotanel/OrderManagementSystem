package org.example.BLL;

import org.example.DAO.ProductDAO;
import org.example.Model.Orders;
import org.example.Model.Product;
import org.example.Validator;

/**
 * Validator implementation to validate the quantity of an order against the available stock of the associated product.
 */
public class OrderQuantityValidator implements Validator<Orders> {
    private ProductDAO productDAO;

    /**
     * Constructs a new OrderQuantityValidator with the specified ProductDAO.
     *
     * @param productDAO The ProductDAO used to retrieve product information for validation.
     */
    public OrderQuantityValidator(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Validates the quantity of an order against the available stock of the associated product.
     *
     * @param orders The order to validate.
     * @throws IllegalArgumentException If the product associated with the order is not found or if the ordered quantity exceeds the available stock.
     */
    @Override
    public void validate(Orders orders) {
        Product product = productDAO.findById(orders.getIdProduct());
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        if (orders.getQuantity() > product.getQuantity()) {
            throw new IllegalArgumentException("Ordered quantity exceeds available stock");
        }
    }
}
