package org.example.BLL;

import org.example.Model.Product;
import org.example.Validator;

/**
 * Validator implementation for checking if the quantity of a product is non-negative.
 */
public class ProductQuantityValidator implements Validator<Product>
{

    /**
     * Validates the quantity of a product.
     *
     * @param product The product to validate.
     * @throws IllegalArgumentException If the quantity of the product is negative.
     */
    @Override
    public void validate(Product product)
    {
        if(product.getQuantity() < 0)
        {
            throw new IllegalArgumentException("Product quantity cannot be negative");
        }
    }
}
