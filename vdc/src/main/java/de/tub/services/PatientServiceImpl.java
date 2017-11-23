package de.tub.services;

import de.tub.model.Patient;
import de.tub.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    @Autowired
    public void setPatientRepository(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }


    @Override
    public Iterable<Patient> listAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientBySSN(Long ssn) {
        return patientRepository.findOne(ssn);
    }
}
