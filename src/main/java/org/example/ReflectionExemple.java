package org.example;

import java.lang.reflect.Field;

/**
 * A class for retrieving properties of objects using reflection.
 */
public class ReflectionExemple
{
    /**
     * Retrieves properties of the given object using reflection and prints them.
     *
     * @param object The object whose properties are to be retrieved.
     */
    public static void retrieveProperties (Object object)
    {
        for(Field field : object.getClass().getDeclaredFields())
        {
            field.setAccessible(true);
            Object value;
            try
            {
                value = field.get(object);
                System.out.println(field.getName() + "=" + value);
            }
            catch (IllegalArgumentException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
    }
}
