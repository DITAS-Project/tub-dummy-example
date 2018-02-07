/*
 * Copyright 2017 ISE TU Berlin
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.tub.services;

import de.tub.model.Patient;
import de.tub.repositories.PatientRepository;
import org.apache.tomcat.jni.Local;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.joda.JodaTimeContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    public Iterable<Patient> listAllPatients(Integer minAge, Integer maxAge, Patient.GenderEnum gender) {
        final Stream<Patient> patientStream = StreamSupport.stream(patientRepository.findAll().spliterator(), false)
                .filter(getAgeFilter(minAge, maxAge))
                .filter(p -> {
                    if (gender != null && gender == p.getGender()) {
                        return true;
                    } else {
                        return gender == null;
                    }
                });

        return patientStream::iterator;
    }

    private Predicate<Patient> getAgeFilter(Integer minAge, Integer maxAge) {
        final LocalDate yungestAge =(minAge != null)?LocalDate.now().minusYears(minAge):null;
        final LocalDate olderstAge = (maxAge != null)?LocalDate.now().minusYears(maxAge):null;

        return patient -> {
            LocalDate birhtday = LocalDate.parse(patient.getBirthday());

            if(olderstAge != null && birhtday.isBefore(olderstAge)){
                return false;
            } else if(yungestAge != null && birhtday.isAfter(yungestAge)){
                return false;
            }

            return true;
        };
    }

    @Override
    public Patient getPatientBySSN(Long ssn) {
        return patientRepository.findOne(ssn);
    }
}
