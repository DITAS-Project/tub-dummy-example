package de.tub.services;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.MappingManager;
import de.tub.model.Exam;
import de.tub.model.Patient;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExamServiceImpl implements ExamService{

    @Value("${cassandra.uri}")
    private String uri;

    @Value("${cassandra.keyspace}")
    private String keyspace;

    private final MappingManager manager;
    private final Session session;

    final PreparedStatement listAll;
    final PreparedStatement listAllBySSN;

    final PreparedStatement serach;
    public ExamServiceImpl(){
        Cluster cluster = null;
        cluster = Cluster.builder()
                    .addContactPoint(uri)
                    .build();
        session = cluster.connect(keyspace);
        manager = new MappingManager(session);

        listAll = session.prepare("Select * from exams");
        listAllBySSN =  session.prepare("Select * from exams where ssn = ?");
        serach =  session.prepare("Select * from exams where ssn in ? and date > ? and date < ?");
    }

    @Override
    public Iterable<Exam> listAllExams() {
        return manager.mapper(Exam.class).map(session.execute(listAll.bind()));
    }

    @Override
    public Iterable<Exam> listExamsBy(Iterable<Patient> ssn, LocalDate start, LocalDate end, String type) {

        HashSet<Long> ssns = StreamSupport.stream(ssn.spliterator(),false).map(Patient::getSSN).collect(Collectors.toCollection(HashSet::new));

        return manager.mapper(Exam.class).map(session.execute(serach.bind(ssns,start,end)));
    }

    @Override
    public Iterable<Exam> getExamBySSN(Long ssn) {
        return manager.mapper(Exam.class).map(session.execute(listAllBySSN.bind(ssn)));

    }

    @Override
    public Iterable<Exam> getExamBy(Long ssn, String type) {
        return manager.mapper(Exam.class).map(session.execute(listAllBySSN.bind(ssn)));
    }
}
