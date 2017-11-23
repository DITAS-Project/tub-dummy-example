package de.tub.util;

import de.tub.model.Patient;

import javax.persistence.AttributeConverter;

public class GenderConverter implements AttributeConverter<Patient.GenderEnum,String> {


    @Override
    public String convertToDatabaseColumn(Patient.GenderEnum genderEnum) {
        return (genderEnum != null)?genderEnum.getCode():null;
    }

    @Override
    public Patient.GenderEnum convertToEntityAttribute(String s) {
        return (s != null)? Patient.GenderEnum.fromValue(s):null;
    }
}