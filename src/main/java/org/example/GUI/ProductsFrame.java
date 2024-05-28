package org.example.GUI;

import org.example.BLL.ProductBLL;
import org.example.Model.Product;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * The ProductsFrame class represents the graphical user interface for managing products.
 */
public class ProductsFrame extends JFrame {

    private final ProductBLL productBLL;
    private final JTable table;
    private final DefaultTableModel model;
    private final JTextField nameField;
    private final JTextField priceField;
    private final JTextField quantityField;

    /**
     * Constructs a new ProductsFrame with the specified ProductBLL instance.
     *
     * @param productBLL The ProductBLL instance to use for product management.
     */
    public ProductsFrame(ProductBLL productBLL) {
        this.productBLL = productBLL;

        setTitle("Products Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a table model
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to the frame
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create text fields
        nameField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();
        JLabel nameLabel = new JLabel("Name:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel quantityLabel = new JLabel("Quantity:");
        JButton addButton = createStyledButton("Add");
        JButton deleteButton = createStyledButton("Delete");
        JButton updateButton = createStyledButton("Update");
        JButton findByIdButton = createStyledButton("Find By ID");
        JButton getAllIdsButton = createStyledButton("Get All IDs");
        JButton backButton = createStyledButton("Back"); // Back button added

        // Set preferred dimensions for buttons
        Dimension buttonDimension = new Dimension(100, 30);
        addButton.setPreferredSize(buttonDimension);
        deleteButton.setPreferredSize(buttonDimension);
        updateButton.setPreferredSize(buttonDimension);
        findByIdButton.setPreferredSize(buttonDimension);
        getAllIdsButton.setPreferredSize(buttonDimension);

        // Create panel for text fields and buttons
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(updateButton);
        inputPanel.add(findByIdButton);
        inputPanel.add(getAllIdsButton);
        inputPanel.add(backButton); // Back button added

        // Add the input panel to the main panel
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        // Populate the table with product data
        populateTable();

        // Add action listeners to buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int price = Integer.parseInt(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                Product product = new Product(0, name, quantity, price);
                productBLL.insertProduct(product);
                refreshTable();

                // Clear text fields after adding
                nameField.setText("");
                priceField.setText("");
                quantityField.setText("");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int productId = (int) table.getValueAt(selectedRow, 0);
                    productBLL.deleteProduct(productId);
                    refreshTable();
                }

                // Clear text fields after deleting
                nameField.setText("");
                priceField.setText("");
                quantityField.setText("");
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int productId = (int) table.getValueAt(selectedRow, 0);
                    String name = nameField.getText();
                    int price = Integer.parseInt(priceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    Product product = new Product(productId, name, quantity, price);
                    productBLL.updateProduct(product);
                    refreshTable();
                }

                // Clear text fields after updating
                nameField.setText("");
                priceField.setText("");
                quantityField.setText("");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new MainFrame().setVisible(true);
            }
        });

        findByIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Product ID:"));
                Product product = productBLL.findById(id);
                if (product != null) {
                    nameField.setText(product.getName());
                    priceField.setText(String.valueOf(product.getPrice()));
                    quantityField.setText(String.valueOf(product.getQuantity()));
                } else {
                    JOptionPane.showMessageDialog(null, "Product not found!");
                }
            }
        });

        getAllIdsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Integer> ids = productBLL.getProductsIds();
                StringBuilder sb = new StringBuilder("Product IDs:\n");
                for (Integer id : ids) {
                    sb.append(id).append("\n");
                }
                JOptionPane.showMessageDialog(null, sb.toString());
            }
        });

        // Add a selection listener to the table
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    int selectedRow = table.getSelectedRow();
                    String name = (String) table.getValueAt(selectedRow, 1);
                    int price = (int) table.getValueAt(selectedRow, 2); // Corrected price to int
                    int quantity = (int) table.getValueAt(selectedRow, 3);
                    nameField.setText(name);
                    priceField.setText(String.valueOf(price));
                    quantityField.setText(String.valueOf(quantity));
                }
            }
        });

        // Display the frame
        setVisible(true);
    }

    /**
     * Populates the table with product data.
     */
    private void populateTable() {
        if (model.getColumnCount() == 0) {
            model.addColumn("ID");
            model.addColumn("Name");
            model.addColumn("Price");
            model.addColumn("Quantity");
        }
        List<Product> products = productBLL.findAllProducts();
        for (Product product : products) {
            Object[] rowData = {product.getId(), product.getName(), product.getPrice(), product.getQuantity()};
            model.addRow(rowData);
        }
    }

    /**
     * Refreshes the table with updated product data.
     */
    private void refreshTable() {
        model.setRowCount(0);
        populateTable();
    }

    // Method for creating a styled button
    /**
     * Creates a styled button with the specified text.
     *
     * @param text The text to display on the button.
     * @return The styled JButton object.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 152, 219)); // Set background color
        button.setForeground(Color.WHITE); // Set text color
        button.setFocusPainted(false); // Remove focus effect
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Set font and text size
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Set margins
        return button;
    }
}

