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

package util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.stream.Collectors;

public class DataGenerator {

    private static final int BATCH_SIZE = 500;
    private static final int PATIENT_SIZE = BATCH_SIZE*1000;
    private static final int EXAMS_PER_PATIENT_BOUND = 25;

    private static ArrayList<String> readCSV(Path f){
        try {
            return Files.lines(f).collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        int ssn = 1;

        ArrayList<String> lastNames = readCSV(Paths.get("lastname.csv"));
        ArrayList<String> names = readCSV(Paths.get("name.csv"));

        String[] domains = new String[]{"gmail.com","web.de","tu-berlin.de"};

        Random rand = new Random();

        SimpleDateFormat birthdayFormat = new SimpleDateFormat("YYYY-MM-dd");
        SimpleDateFormat examFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

        BufferedWriter patient = Files.newBufferedWriter(Paths.get("patient.sql"));
        BufferedWriter exams = Files.newBufferedWriter(Paths.get("exams.sql"));

        //gender, date, cholesterol, triglyceride, hepatitis
        String KEYSPACE = "osr";

        writeExamsHeader(exams, KEYSPACE);

        String tableName = "patient";
        writePatientHeader(patient, tableName);

        for (int k = 0; k < PATIENT_SIZE; k+= BATCH_SIZE) {
            patient.write(("INSERT INTO "+tableName+" (SSN,LASTNAME,NAME,GENDER,MAIL,BIRTHDAY) VALUES"));
            for (int j = 0; j < BATCH_SIZE; j++) {
                int id = ssn++;
                String lastName = lastNames.get(rand.nextInt(lastNames.size()));
                String name = names.get(rand.nextInt(names.size()));
                String gender = (rand.nextBoolean())?"m":"f";
                String domain = domains[rand.nextInt(domains.length)];
                String mail = String.format("%s.%s@%s",name,lastName,domain);
                Calendar birthdate = Calendar.getInstance();
                birthdate.set(Calendar.YEAR,1910+rand.nextInt(100));
                birthdate.set(Calendar.MONTH,1+rand.nextInt(11));
                birthdate.set(Calendar.DATE,1+rand.nextInt(25));

                patient.write(String.format("(%d,'%s','%s','%s','%s','%s')\n",id,lastName,name,gender,mail,birthdayFormat.format(birthdate.getTime())));
                if(j < BATCH_SIZE -1){
                    patient.write(",");
                }

                boolean hipertites = false;
                Calendar lastDate = Calendar.getInstance();
                lastDate.set(Calendar.YEAR,1990+rand.nextInt(27));
                lastDate.set(Calendar.MONTH,1+rand.nextInt(11));
                lastDate.set(Calendar.DATE,1);

                exams.write("BEGIN BATCH\n");

                int examsBound = rand.nextInt(EXAMS_PER_PATIENT_BOUND);
                for (int l = 0; l < examsBound; l++) {
                    lastDate.add(Calendar.DATE,rand.nextInt(180));

                    lastDate.set(Calendar.HOUR,rand.nextInt(23));
                    lastDate.set(Calendar.MINUTE,rand.nextInt(59));
                    lastDate.set(Calendar.SECOND,rand.nextInt(59));

                    hipertites = (hipertites) || 0.2 * rand.nextInt(100) > 15;
                    double cholesteral = rand.nextInt(500) / 100.0;
                    double triglyceride = rand.nextInt(500) / 100.0;
                    exams.write("INSERT INTO "+KEYSPACE+".exams (SSN,lastname,name,date,cholesterol,triglyceride,hepatitis) VALUES");
                    exams.write(String.format("(%d,'%s','%s','%s','%s',%f,%f,%b);\n",ssn,lastName,name,gender,examFormat.format(lastDate.getTime()),cholesteral,triglyceride,hipertites));
                }
                exams.write("APPLY BATCH;\n");
            }
            exams.write("\n");
            exams.flush();
            patient.write(";\n\n");
            patient.flush();
        }
        exams.flush();
        exams.close();
        patient.close();
    }

    private static void writePatientHeader(BufferedWriter patient, String tableName) throws IOException {
        patient.write("CREATE TABLE IF NOT EXISTS " + tableName + " (\n" +
                "\tSSN INT NOT NULL AUTO_INCREMENT,\n" +
                "\tLASTNAME VARCHAR(100),\n" +
                "\tNAME VARCHAR(100),\n" +
                "\tGENDER CHAR(1),\n" +
                "\tMAIL VARCHAR(255),\n" +
                "\tBIRTHDAY DATE,\n" +
                "\tPRIMARY KEY (SSN)\n" +
                ") ;\n\n");
    }

    private static void writeExamsHeader(BufferedWriter exams, String KEYSPACE) throws IOException {
        exams.write("CREATE KEYSPACE IF NOT EXISTS " + KEYSPACE + "\n" +
                "WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };\n" +
                "USE osr;\n" +
                "CREATE TABLE IF NOT EXISTS "+KEYSPACE+".exams (\n" +
                "  ssn int,\n" +
                "  lastname text,\n" +
                "  name text,\n" +
                "  date timestamp,\n" +
                "  cholesterol double,\n" +
                "  triglyceride double,\n" +
                "  hepatitis boolean,\n" +
                "  Primary KEY(ssn, date)\n" +
                ");\n\n");
    }
}
