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

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.MappingManager;
import de.tub.model.Exam;
import de.tub.model.Patient;
import de.tub.trace.TraceHelper;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExamServiceImpl implements ExamService{

    @Autowired
    TraceHelper helper;

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
        return helper.wrapCall(()-> manager.mapper(Exam.class).map(session.execute(listAll.bind())));
    }

    @Override
    public Iterable<Exam> listExamsBy(Iterable<Patient> ssn, LocalDate start, LocalDate end, String type) {
        return helper.wrapCall(()-> {
            LinkedList<Integer> ssns = StreamSupport.stream(ssn.spliterator(), false).map(Patient::getSSN).map(Long::intValue).collect(Collectors.toCollection(LinkedList::new));
            if (start != null && end != null) {
                return serach(start, end, ssns);
            } else if (start != null) {
                return openEnd(ssns, start);
            } else if (end != null) {
                return openBegin(ssns, end);
            } else {
                return filterBySSN(ssns);
            }
        },"listExamsBy");
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
        return helper.wrapCall(()-> manager.mapper(Exam.class).map(session.execute(listAllBySSN.bind(ssn.intValue()))),"getExamsBySSN");
    }

    @Override
    public Iterable<Exam> getExamBy(Long ssn, String type) {
        return helper.wrapCall(()-> manager.mapper(Exam.class).map(session.execute(listAllBySSN.bind(ssn.intValue()))),"getExamBy");
    }
}
