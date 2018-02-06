package de.tub.ditas.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;

import java.lang.reflect.Field;

public class atFieldNamingStrategy implements FieldNamingStrategy {

    /**
     * Changes every field that is called timestamp in java to @timestamp in the Json
     * @param field
     * @return
     */
    @Override
    public String translateName(Field field) {
            String fieldName =
                    FieldNamingPolicy.IDENTITY.translateName(field);
            if (fieldName.startsWith("timestamp")) {
                fieldName ="@".concat(fieldName);
            }

            return fieldName;
    }
}
