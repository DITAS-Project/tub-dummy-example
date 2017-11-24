package de.tub.services;

import de.tub.model.Patient;

import java.time.LocalDate;

public interface PatientService {
    Iterable<Patient> listAllPatients();
    Iterable<Patient> listAllPatients(Integer minAge, Integer maxAge, Patient.GenderEnum gender);

    Patient getPatientBySSN(Long ssn);



}
