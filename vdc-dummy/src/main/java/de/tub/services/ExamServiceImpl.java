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

import com.datastax.driver.core.*;
import com.datastax.driver.mapping.MappingManager;
import de.tub.model.Exam;
import de.tub.model.Patient;
import de.tub.trace.Trace;
import de.tub.trace.TraceHelper;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Collections;
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

    private final PreparedStatement search;
    private final PreparedStatement openBegin;
    private final PreparedStatement openEnd;
    private final PreparedStatement filterBySSN;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

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
        search =  session.prepare("Select * from exams where ssn in ? and date > ? and date < ?");
        openEnd =  session.prepare("Select * from exams where ssn in ? and date > ?");
        openBegin =  session.prepare("Select * from exams where ssn in ? and date < ?");
        filterBySSN = session.prepare("Select * from exams where ssn in ?");
    }

    @Override
    public Iterable<Exam> listAllExams() {
        return helper.wrapCall(()-> map(execWithTrace(listAll.bind())));
    }

    private Iterable<Exam> map(ResultSet set){
        return manager.mapper(Exam.class).map(set);
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
        return map(execWithTrace(filterBySSN.bind(ssns)));
    }

    private Iterable<Exam> serach(LocalDate start, LocalDate end, LinkedList<Integer> ssns) {
        return map(execWithTrace(search.bind(ssns, start.toDate(), end.toDate())));
    }

    private Iterable<Exam> openBegin(LinkedList<Integer> ssns, LocalDate end) {
        return map(execWithTrace(openBegin.bind(ssns, end.toDate())));
    }

    private Iterable<Exam> openEnd(LinkedList<Integer> ssns, LocalDate start) {
        return map(execWithTrace(openEnd.bind(ssns, start.toDate())));
    }

    @Override
    public Iterable<Exam> getExamBySSN(Long ssn) {
        return helper.wrapCall(()-> map(execWithTrace(listAllBySSN.bind(ssn.intValue()))),"getExamsBySSN");
    }

    @Override
    public Iterable<Exam> getExamBy(Long ssn, String type) {
        return helper.wrapCall(()-> map(execWithTrace(listAllBySSN.bind(ssn.intValue()))),"getExamBy");
    }

    private ResultSet execWithTrace(Statement statement){
        Trace trace = Trace.extractFromThread();
        if(trace != null) {
            trace = trace.ChildOf("cassandra");
            helper.push(trace);
            statement.setOutgoingPayload(Collections.singletonMap("zipkin", trace.toBuffer()));
            ResultSet resultSet = exec(statement.enableTracing());
            helper.finish(trace);
            return resultSet;
        }
        return exec(statement);
    }
    
    private ResultSet exec(Statement statement){
        return session.execute(statement);
    }
    
    
    
}
