package org.example.BLL;

import org.example.DAO.ProductDAO;
import org.example.Model.Product;
import org.example.Validator;
import org.example.Validator.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Business logic layer for managing products.
 */
public class ProductBLL {
    private List<Validator<Product>> validatorList;
    private ProductDAO productDAO;

    /**
     * Constructs a new ProductBLL object with default validators and a ProductDAO.
     */
    public ProductBLL() {
        validatorList = new ArrayList<>();
        validatorList.add(new ProductQuantityValidator());
        productDAO = new ProductDAO();
    }

    /**
     * Inserts a new product into the database.
     *
     * @param product The product to insert.
     * @return The ID of the inserted product, or -1 if insertion failed.
     */
    public int insertProduct(Product product) {
        List<String> errors = new ArrayList<>();
        for (Validator<Product> validator : validatorList) {
            try {
                validator.validate(product);
            } catch (IllegalArgumentException e) {
                errors.add(e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            String errorMessage = String.join("\n", errors);
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }

        return productDAO.insert(product);
    }

    /**
     * Finds a product by its ID.
     *
     * @param id The ID of the product to find.
     * @return The product with the specified ID, or null if not found.
     */
    public Product findById(int id) {
        Product product = productDAO.findById(id);
        if (product == null) {
            JOptionPane.showMessageDialog(null, "The product with ID " + id + " does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return product;
    }

    /**
     * Updates an existing product.
     *
     * @param product The product to update.
     * @return The updated product, or null if update failed.
     */
    public Product updateProduct(Product product) {
        List<String> errors = new ArrayList<>();
        for (Validator<Product> validator : validatorList) {
            try {
                validator.validate(product);
            } catch (IllegalArgumentException e) {
                errors.add(e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            String errorMessage = String.join("\n", errors);
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return productDAO.update(product);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @return true if the product was successfully deleted, false otherwise.
     */
    public boolean deleteProduct(int id) {
        return productDAO.delete(id);
    }

    /**
     * Retrieves a list of all products.
     *
     * @return A list containing all products.
     */
    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    /**
     * Retrieves a list of IDs for all products.
     *
     * @return A list of product IDs.
     */
    public List<Integer> getProductsIds() {
        return productDAO.getIds();
    }

    /**
     * Retrieves a list of column names for the product table.
     *
     * @return A list of column names.
     */
    public List<String> getColumns() {
        return productDAO.getColumnNames();
    }
}
