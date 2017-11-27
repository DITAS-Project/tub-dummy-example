package de.tub.services;

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.MappingManager;
import de.tub.model.Exam;
import de.tub.model.Patient;
import de.tub.util.JodaTimeCodec;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class ExamServiceImpl implements ExamService{

    private final MappingManager manager;
    private final Session session;

    final PreparedStatement listAll;
    final PreparedStatement listAllBySSN;

    final PreparedStatement serach;
    private final PreparedStatement openBegin;
    private final PreparedStatement openEnd;
    private final PreparedStatement filterBySSN;

    public ExamServiceImpl(@Value("${cassandra.uri}") String uri, @Value("${cassandra.keyspace}") String keyspace){
        System.out.println(uri);
        Cluster cluster = null;
        cluster = Cluster.builder()
                    .addContactPoint(uri)
                    .build();

        session = cluster.connect(keyspace);
        manager = new MappingManager(session);

        listAll = session.prepare("Select * from exams");
        listAllBySSN =  session.prepare("Select * from exams where ssn = ?");
        serach =  session.prepare("Select * from exams where ssn in ? and date > ? and date < ?");
        openEnd =  session.prepare("Select * from exams where ssn in ? and date > ?");
        openBegin =  session.prepare("Select * from exams where ssn in ? and date < ?");
        filterBySSN = session.prepare("Select * from exams where ssn in ?");
    }

    @Override
    public Iterable<Exam> listAllExams() {
        return manager.mapper(Exam.class).map(session.execute(listAll.bind()));
    }

    @Override
    public Iterable<Exam> listExamsBy(Iterable<Patient> ssn, LocalDate start, LocalDate end, String type) {

        LinkedList<Integer> ssns = StreamSupport.stream(ssn.spliterator(),false).map(Patient::getSSN).map(Long::intValue).collect(Collectors.toCollection(LinkedList::new));
        if(start != null && end != null) {
            return serach(start, end, ssns);
        } else if(start != null){
            return openEnd(ssns,start);
        } else if(end != null){
            return openBegin(ssns,end);
        } else {
            return filterBySSN(ssns);
        }
    }

    private Iterable<Exam> filterBySSN(LinkedList<Integer> ssns) {
        return manager.mapper(Exam.class).map(session.execute(filterBySSN.bind(ssns)));
    }

    private Iterable<Exam> serach(LocalDate start, LocalDate end, LinkedList<Integer> ssns) {
        return manager.mapper(Exam.class).map(session.execute(serach.bind(ssns, start.toDate(), end.toDate())));
    }

    private Iterable<Exam> openBegin(LinkedList<Integer> ssns, LocalDate end) {
        return manager.mapper(Exam.class).map(session.execute(openBegin.bind(ssns, end.toDate())));
    }

    private Iterable<Exam> openEnd(LinkedList<Integer> ssns, LocalDate start) {
        return manager.mapper(Exam.class).map(session.execute(openEnd.bind(ssns, start.toDate())));
    }

    @Override
    public Iterable<Exam> getExamBySSN(Long ssn) {
       return manager.mapper(Exam.class).map(session.execute(listAllBySSN.bind(ssn.intValue())));

    }

    @Override
    public Iterable<Exam> getExamBy(Long ssn, String type) {
        return manager.mapper(Exam.class).map(session.execute(listAllBySSN.bind(ssn.intValue())));
    }
}
