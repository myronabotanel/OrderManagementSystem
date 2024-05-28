package org.example.GUI;

import org.example.BLL.ClientBLL;
import org.example.Model.Client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


/**
 * The ClientsFrame class represents the graphical user interface for managing clients.
 */
public class ClientsFrame extends JFrame {

    private final ClientBLL clientBLL;
    private final JTable table;
    private final DefaultTableModel model;
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField addressField;

    /**
     * Constructs a new ClientsFrame.
     *
     * @param clientBLL The ClientBLL instance for handling client business logic.
     */
    public ClientsFrame(ClientBLL clientBLL) {
        this.clientBLL = clientBLL;

        setTitle("Clients Management");
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
        emailField = new JTextField();
        addressField = new JTextField();
        JLabel nameLabel = new JLabel("Name:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel addressLabel = new JLabel("Address:");
        JButton addButton = createStyledButton("Add");
        JButton deleteButton = createStyledButton("Delete");
        JButton updateButton = createStyledButton("Update");
        JButton findByIdButton = createStyledButton("Find By ID");
        JButton getAllIdsButton = createStyledButton("Get All IDs");
        JButton backButton = createStyledButton("Back"); // Back button added

        // Create panel for text fields and buttons
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(addressLabel);
        inputPanel.add(addressField);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        inputPanel.add(updateButton);
        inputPanel.add(findByIdButton);
        inputPanel.add(getAllIdsButton);
        inputPanel.add(backButton); // Back button added

        // Add the input panel to the main panel
        getContentPane().add(inputPanel, BorderLayout.SOUTH);

        // Populate the table with client data
        populateTable();

        // Add action listeners to buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String email = emailField.getText();
                String address = addressField.getText();
                Client client = new Client(0, name, email, address);
                clientBLL.insertClient(client);
                refreshTable();

                // Clear text fields after adding
                nameField.setText("");
                emailField.setText("");
                addressField.setText("");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int clientId = (int) table.getValueAt(selectedRow, 0);
                    clientBLL.deleteClient(clientId);
                    refreshTable();
                }

                // Clear text fields after deleting
                nameField.setText("");
                emailField.setText("");
                addressField.setText("");
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int clientId = (int) table.getValueAt(selectedRow, 0);
                    String name = nameField.getText();
                    String email = emailField.getText();
                    String address = addressField.getText();
                    Client client = new Client(clientId, name, email, address);
                    clientBLL.updateClient(client);
                    refreshTable();
                }

                // Clear text fields after updating
                nameField.setText("");
                emailField.setText("");
                addressField.setText("");
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
                int id = Integer.parseInt(JOptionPane.showInputDialog("Enter Client ID:"));
                Client client = clientBLL.findById(id);
                if (client != null) {
                    nameField.setText(client.getName());
                    emailField.setText(client.getEmail());
                    addressField.setText(client.getAddress());
                } else {
                    JOptionPane.showMessageDialog(null, "Client not found!");
                }
            }
        });

        getAllIdsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                List<Integer> ids = clientBLL.getClientsIds();
                StringBuilder sb = new StringBuilder("Client IDs:\n");
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
                    String email = (String) table.getValueAt(selectedRow, 2);
                    String address = (String) table.getValueAt(selectedRow, 3);
                    nameField.setText(name);
                    emailField.setText(email);
                    addressField.setText(address);
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
            model.addColumn("Email");
            model.addColumn("Address");
        }
        List<Client> clients = clientBLL.findAllClients();
        for (Client client : clients) {
            Object[] rowData = {client.getId(), client.getName(), client.getEmail(), client.getAddress()};
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
