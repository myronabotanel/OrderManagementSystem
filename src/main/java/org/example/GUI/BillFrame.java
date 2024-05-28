package org.example.GUI;

import org.example.Model.Bill;

import javax.swing.*;
import java.awt.*;

public class BillFrame extends JFrame {

    public BillFrame(Bill bill) {
        setTitle("Bill");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel to hold bill information
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("ID:"));
        panel.add(new JLabel(String.valueOf(bill.id())));
        panel.add(new JLabel("Client Name:"));
        panel.add(new JLabel(bill.clientName()));
        panel.add(new JLabel("Product Name:"));
        panel.add(new JLabel(bill.productName()));
        panel.add(new JLabel("Quantity:"));
        panel.add(new JLabel(String.valueOf(bill.quantity())));
        panel.add(new JLabel("Price:"));
        panel.add(new JLabel(String.valueOf(bill.price())));
        panel.add(new JLabel("Total:"));
        panel.add(new JLabel(String.valueOf(bill.total())));

        getContentPane().add(panel, BorderLayout.CENTER);

        setVisible(true);
    }
}
