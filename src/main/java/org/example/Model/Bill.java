package org.example.Model;

/**
 *  The Bill class represents an invoice dor a specific order
 * @param id
 * @param idOrder
 * @param clientName
 * @param productName
 * @param quantity
 * @param price
 * @param total
 */
public record Bill(int id, int idOrder, String clientName, String productName, int quantity, int price, int total) {
    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", idOrder=" + idOrder +
                ", clientName='" + clientName + '\'' +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", total=" + total +
                '}';
    }
}
