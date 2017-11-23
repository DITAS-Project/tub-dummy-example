package de.tub.services;

import de.tub.model.Patient;

public interface PatientService {
    Iterable<Patient> listAllPatients();

    Patient getPatientBySSN(Long ssn);

}
