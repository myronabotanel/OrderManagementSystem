package org.example;

import org.example.BLL.ClientBLL;
import org.example.GUI.MainFrame;
import org.example.Model.Client;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

 /**
 * The main class of the application
 */
public class Main {

    // Declarație pentru LOGGER
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());


     /**
     * The main method of the application.
     * @param args Command line arguments (not used).
     * @throws SQLException If a database access error occurs.
     */
    public static void main(String[] args) throws SQLException {
        // Crează o instanță a clasei MainFrame și o afișează
        javax.swing.SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });

        ClientBLL clientBLL = new ClientBLL();
        Client client1 = null;

        try {
            client1 = clientBLL.findById(17);
        } catch (Exception e) {
            LOGGER.log(Level.INFO, e.getMessage());
        }
        ReflectionExemple.retrieveProperties(client1);
    }
}
