package org.example.GUI;

import org.example.BLL.OrderBLL;
import org.example.DAO.ClientDAO;
import org.example.DAO.ProductDAO;
import org.example.Model.Bill;
import org.example.Model.Client;
import org.example.Model.Orders;
import org.example.Model.Product;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The OrdersFrame class represents the graphical user interface for managing orders.
 */
public class OrdersFrame extends JFrame {

    private final OrderBLL orderBLL;
    private final JTable table;
    private final DefaultTableModel model;
    private final JTextField clientIdField;
    private final JTextField productIdField;
    private final JTextField quantityField;

    /**
     * Constructs a new OrdersFrame with the specified OrderBLL instance.
     *
     * @param orderBLL The OrderBLL instance to use for order management.
     */
    public OrdersFrame(OrderBLL orderBLL) {
        this.orderBLL = orderBLL;

        setTitle("Orders Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a table model
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to the frame
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create text fields
        clientIdField = new JTextField();
        productIdField = new JTextField();
        quantityField = new JTextField();
        JLabel clientIdLabel = new JLabel("Client ID:");
        JLabel productIdLabel = new JLabel("Product ID:");
        JLabel quantityLabel = new JLabel("Quantity:");
        JLabel priceLabel = new JLabel("Price:"); // Nou camp pentru Price
        JButton addButton = createStyledButton("Add");

        // Set preferred dimensions for text fields
        Dimension textFieldDimension = new Dimension(100, 25);
        clientIdField.setPreferredSize(textFieldDimension);
        productIdField.setPreferredSize(textFieldDimension);
        quantityField.setPreferredSize(textFieldDimension);


        // Create panel for text fields and buttons
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5)); // GridLayout actualizat pentru un rand in plus
        inputPanel.add(clientIdLabel);
        inputPanel.add(clientIdField);
        inputPanel.add(productIdLabel);
        inputPanel.add(productIdField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);
        inputPanel.add(addButton);

        // Add the input panel to the main panel
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        // Populate the table with order data
        populateTable();

        // Add action listeners to buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int clientId = Integer.parseInt(clientIdField.getText());
                    int productId = Integer.parseInt(productIdField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    Orders order = new Orders(0, clientId, productId, quantity);
                    Orders insertedOrder = orderBLL.insertOrder(order);
                    refreshTable();

                    // Clear text fields after adding
                    clientIdField.setText("");
                    productIdField.setText("");
                    quantityField.setText("");


                    // Afisam factura pentru comanda curenta
                    createBillForOrder(insertedOrder);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(OrdersFrame.this, "Please enter valid numbers for Client ID, Product ID, and Quantity.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                } catch (NoSuchElementException ex) {
                    JOptionPane.showMessageDialog(OrdersFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(OrdersFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Creăm un buton "Back"
        JButton backButton = createStyledButton("Back");

        // Adăugăm un ascultător de evenimente pentru butonul "Back"
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ascundem fereastra curentă
                setVisible(false);

                // Creăm și afișăm fereastra principală (MainFrame)
                new MainFrame().setVisible(true);
            }
        });

        // Adăugăm butonul "Back" la panoul nostru
        inputPanel.add(backButton);

        // Add a selection listener to the table
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    int selectedRow = table.getSelectedRow();
                    int clientId = (int) table.getValueAt(selectedRow, 1);
                    int productId = (int) table.getValueAt(selectedRow, 2);
                    int quantity = (int) table.getValueAt(selectedRow, 3);
                    clientIdField.setText(String.valueOf(clientId));
                    productIdField.setText(String.valueOf(productId));
                    quantityField.setText(String.valueOf(quantity));

                }
            }
        });

        // Add action listener to update button
//        updateButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    int selectedRow = table.getSelectedRow();
//                    if (selectedRow == -1) {
//                        JOptionPane.showMessageDialog(OrdersFrame.this, "No order selected for update.", "Error", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }
//
//                    int clientId = Integer.parseInt(clientIdField.getText());
//                    int productId = Integer.parseInt(productIdField.getText());
//                    int quantity = Integer.parseInt(quantityField.getText());
//
//                    Orders order = new Orders((int) table.getValueAt(selectedRow, 0), clientId, productId, quantity);
//                    orderBLL.updateOrder(order);
//                    refreshTable();
//
//                    // Clear text fields after updating
//                    clientIdField.setText("");
//                    productIdField.setText("");
//                    quantityField.setText("");
//                } catch (NumberFormatException ex) {
//                    JOptionPane.showMessageDialog(OrdersFrame.this, "Please enter valid numbers for Client ID, Product ID, Quantity, and Price.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
//                } catch (NoSuchElementException ex) {
//                    JOptionPane.showMessageDialog(OrdersFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                } catch (IllegalArgumentException ex) {
//                    JOptionPane.showMessageDialog(OrdersFrame.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });

        // Display the frame
        setVisible(true);
    }

    /**
     * Populates the table with order data.
     */
    private void populateTable() {
        if (model.getColumnCount() == 0) {
            model.addColumn("ID");
            model.addColumn("Client ID");
            model.addColumn("Product ID");
            model.addColumn("Quantity");
        }
        List<Orders> orders = orderBLL.findAllOrders();
        for (Orders order : orders) {
            Object[] rowData = {order.getId(), order.getIdClient(), order.getIdProduct(), order.getQuantity()};
            model.addRow(rowData);
        }
    }

    /**
     * Refreshes the table with updated order data.
     */
    private void refreshTable() {
        model.setRowCount(0);
        populateTable();
    }

    /**
     * Creates a bill for the specified order and displays it in a separate frame.
     *
     * @param order The order for which to create the bill.
     * @throws NoSuchElementException If the client or product associated with the order is not found.
     */
    private void createBillForOrder(Orders order) {
        // Calculăm totalul comenzii
        int total = calculateTotalForOrder(order);

        // Obținem informațiile despre client și produs
        ClientDAO clientDAO = new ClientDAO();
        ProductDAO productDAO = new ProductDAO();
        Client client = clientDAO.findById(order.getIdClient());
        Product product = productDAO.findById(order.getIdProduct());

        // Verificăm dacă clientul și produsul există
        if (client == null) {
            throw new NoSuchElementException("Client with id =" + order.getIdClient() + " was not found!");
        }
        if (product == null) {
            throw new NoSuchElementException("Product with id =" + order.getIdProduct() + " was not found!");
        }

        // Construim factura
        Bill bill = new Bill(0, order.getId(), client.getName(), product.getName(), order.getQuantity(), product.getPrice(), total);

        // Afisăm factura într-o fereastră separată
        showBillInSeparateFrame(bill);
    }

    /**
     * Displays the bill in a separate frame.
     *
     * @param bill The bill to display.
     */
    private void showBillInSeparateFrame(Bill bill) {
        // Creăm o fereastră pentru afișarea facturii
        JFrame billFrame = new JFrame("Bill");
        billFrame.setSize(400, 300);
        billFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        billFrame.setLocationRelativeTo(null);

        // Creăm un panou pentru afișarea informațiilor facturii
        JPanel panel = new JPanel(new GridLayout(7, 1));
        panel.add(new JLabel("Bill ID: " + bill.id()));
        panel.add(new JLabel("Order ID: " + bill.idOrder()));
        panel.add(new JLabel("Client Name: " + bill.clientName()));

        panel.add(new JLabel("Product Name: " + bill.productName()));
        panel.add(new JLabel("Quantity: " + bill.quantity()));
        panel.add(new JLabel("Price: " + bill.price()));
        panel.add(new JLabel("Total: " + bill.total()));

        // Adăugăm panoul la fereastră
        billFrame.add(panel);

        // Afișăm fereastra cu factura
        billFrame.setVisible(true);
    }

    /**
     * Calculates the total amount for the specified order.
     *
     * @param order The order for which to calculate the total amount.
     * @return The total amount for the order.
     * @throws NoSuchElementException If the product associated with the order is not found.
     */
    private int calculateTotalForOrder(Orders order) {
        ProductDAO productDAO = new ProductDAO();
        Product product = productDAO.findById(order.getIdProduct());
        if (product == null) {
            throw new NoSuchElementException("Product with id =" + order.getIdProduct() + " was not found!");
        }
        return product.getPrice() * order.getQuantity();
    }

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
