package org.example;

import org.example.Model.Client;

import java.lang.reflect.Field;

/**
 * A class for retrieving properties of Client objects using reflection.
 */
public class ReflectionExemple {
    /**
     * Retrieves properties of the given Client object using reflection and prints them.
     *
     * @param client The Client object whose properties are to be retrieved.
     */
    public static void retrieveProperties(Client client) {
        for (Field field : client.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(client);
                System.out.println(field.getName() + "=" + value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
