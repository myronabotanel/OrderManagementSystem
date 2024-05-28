package org.example.GUI;

import org.example.BLL.ClientBLL;
import org.example.BLL.OrderBLL;
import org.example.BLL.ProductBLL;
import org.example.GUI.ClientsFrame;
import org.example.GUI.OrdersFrame;
import org.example.GUI.ProductsFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The MainFrame class represents the main menu graphical user interface.
 */
public class MainFrame extends JFrame {

    /**
     * Constructs a new MainFrame.
     */
    public MainFrame() {
        setTitle("Main Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Creăm butoanele cu stiluri personalizate
        JButton ordersButton = createStyledButton("Orders");
        JButton clientsButton = createStyledButton("Clients");
        JButton productsButton = createStyledButton("Products");

        // Adăugăm acțiuni pentru butoanele Orders, Clients și Products
        ordersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Creăm și afișăm fereastra OrdersFrame
                OrderBLL orderBLL = new OrderBLL();
                new OrdersFrame(orderBLL);
                setVisible(false);
            }
        });

        clientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Creăm și afișăm fereastra ClientsFrame
                ClientBLL clientBLL = new ClientBLL();
                new ClientsFrame(clientBLL);
                setVisible(false);
            }
        });

        productsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Creăm și afișăm fereastra ProductsFrame
                ProductBLL productBLL = new ProductBLL();
                new ProductsFrame(productBLL);
                setVisible(false);
            }
        });

        // Adăugăm butoanele la un panou principal
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10)); // Ajustăm layout-ul și spațiile între componente
        panel.setBorder(new EmptyBorder(20, 20, 20, 20)); // Adăugăm un border pentru a face componentele să fie mai ușor de văzut
        panel.setBackground(Color.WHITE); // Setăm culoarea de fundal a panoului

        // Adăugăm butoanele la panou
        panel.add(ordersButton);
        panel.add(clientsButton);
        panel.add(productsButton);

        // Adăugăm panoul la fereastră
        getContentPane().add(panel, BorderLayout.CENTER);

        // Afișăm fereastra principală
        setVisible(true);
    }

    // Metodă pentru crearea unui buton cu stil personalizat
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(52, 152, 219)); // Setăm culoarea de fundal
        button.setForeground(Color.WHITE); // Setăm culoarea textului
        button.setFocusPainted(false); // Eliminăm efectul de focus
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Setăm fontul și dimensiunea textului
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Setăm margini
        return button;
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new MainFrame();
//        });
//    }
}
