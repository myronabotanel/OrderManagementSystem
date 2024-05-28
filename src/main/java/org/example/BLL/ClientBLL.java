package org.example.BLL;

import org.example.DAO.ClientDAO;
import org.example.Model.Client;
import org.example.Validator;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The ClientBLL class provides business logic operations related to the Client model.
 */
public class ClientBLL
{
    private List<Validator<Client>> validatorList;
    private ClientDAO clientDAO;

    /**
     * Constructs a ClientBLL object with a list of validators and a ClientDAO.
     *
     * @param validatorList The list of validators to be used.
     * @param clientDAO     The ClientDAO object to interact with the database.
     */
    public ClientBLL(List<Validator<Client>> validatorList, ClientDAO clientDAO) {
        this.validatorList = validatorList;
        this.clientDAO = clientDAO;
    }

    /**
     * Constructs a ClientBLL object with default validators and a ClientDAO.
     * Uses default validators: EmailValidator, ClientNameValidator.
     */
    public ClientBLL() {
        validatorList = new ArrayList<Validator<Client>>();
        validatorList.add(new EmailValidator());
        validatorList.add(new ClientNameValidator());
        clientDAO = new ClientDAO();
    }

    /**
     * Inserts a new client into the database.
     *
     * @param client The client object to be inserted.
     * @return The ID of the inserted client, or -1 if insertion fails.
     */
    public int insertClient (Client client)
    {
        for(Validator<Client> valid : validatorList)
        {
            try {
                valid.validate(client);
            } catch (IllegalArgumentException e) {
                // Afișăm mesajul de eroare într-o fereastră separată
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return -1; // Returnăm -1 pentru a indica că inserarea a eșuat
            }
        }
        return clientDAO.insert(client);  //return ID
    }

    /**
     * Finds a client by ID.
     *
     * @param id The ID of the client to find.
     * @return The found client, or null if not found.
     */
    public Client findById (int id)
    {
        Client client = clientDAO.findById(id);
        if (client == null) {
            JOptionPane.showMessageDialog(null, "The client with ID " + id + " does not exist", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return client;
    }


    /**
     * Updates an existing client in the database.
     *
     * @param client The client object with updated information.
     * @return The updated client object, or null if update fails.
     */
    public Client updateClient (Client client)
    {
        for(Validator<Client> valid : validatorList)
        {
            try {
                valid.validate(client);
            } catch (IllegalArgumentException e) {
                // Afișăm mesajul de eroare într-o fereastră separată
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return null; // Returnăm null pentru a indica că actualizarea a eșuat
            }
        }
        return clientDAO.update(client);
    }

    /**
     * Deletes a client from the database.
     *
     * @param id The ID of the client to delete.
     * @return True if deletion is successful, false otherwise.
     */
    public boolean deleteClient (int id)
    {
        return clientDAO.delete(id);
    }

    /**
     * Retrieves all clients from the database.
     *
     * @return A list of all clients.
     */
    public List<Client> findAllClients ()
    {
        return clientDAO.findAll();
    }
    /**
     * Retrieves IDs of all clients from the database.
     *
     * @return A list of all client IDs.
     */
    public List<Integer> getClientsIds()
    {
        return clientDAO.getIds();
    }

    /**
     * Retrieves column names of the client table from the database.
     *
     * @return A list of column names.
     * @throws SQLException If an SQL exception occurs.
     */
    public List<String> getColumns() throws SQLException{
        return clientDAO.getColumnNames();
    }

}
