package de.tub.services;

import de.tub.model.Exam;
import de.tub.model.Patient;
import org.joda.time.LocalDate;

public interface ExamService {
    Iterable<Exam> listAllExams();

    Iterable<Exam> listExamsBy(Iterable<Patient> ssn, LocalDate start, LocalDate end, String type);

    Iterable<Exam> getExamBySSN(Long ssn);

    Iterable<Exam> getExamBy(Long ssn,String type);


}
