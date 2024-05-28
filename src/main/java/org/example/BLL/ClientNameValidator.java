package org.example.BLL;

import org.example.Model.Client;
import org.example.Validator;

/**
 * The ClientNameValidator class validates the name of a client.
 */
public class ClientNameValidator implements Validator<Client>
{

    /**
     * Validates the name of a client.
     *
     * @param client The client object to validate.
     * @throws IllegalArgumentException If the client name is null or empty.
     */
    @Override
    public void validate(Client client)
    {
        if(client.getName() == null || client.getName().isEmpty())
        {
            throw new IllegalArgumentException("Client name cannot be null or empty");
        }
    }
}
