package de.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class DataGenerator {

    private static ArrayList<String> readCSV(Path f){
        try {
            return Files.lines(f).collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void bmain(String[] args) throws IOException {
        int i = 1;
        ArrayList<String> lastNames = readCSV(Paths.get("lastname.csv"));
        ArrayList<String> names = readCSV(Paths.get("name.csv"));
        String[] domains = new String[]{"gmail.com","web.de","tu-berlin.de"};
        Random rand = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");

        BufferedWriter writer = Files.newBufferedWriter(Paths.get("patient.sql"));

        writer.write("CREATE TABLE IF NOT EXISTS patient (\n" +
                "\tSSN INT NOT NULL AUTO_INCREMENT,\n" +
                "\tLASTNAME VARCHAR(100),\n" +
                "\tNAME VARCHAR(100),\n" +
                "\tGENDER CHAR(1),\n" +
                "\tMAIL VARCHAR(255),\n" +
                "\tBIRTHDAY DATE,\n" +
                "\tPRIMARY KEY (SSN)\n" +
                ") ;\n\n");

        for (int k = 0; k < 500000; k+=500) {
            writer.write(("INSERT INTO PATIENT (SSN,LASTNAME,NAME,GENDER,MAIL,BIRTHDAY) VALUES"));
            for (int j = 0; j < 500; j++) {
                int id = i++;
                String lastName = lastNames.get(rand.nextInt(lastNames.size()));
                String name = names.get(rand.nextInt(names.size()));
                String gender = (rand.nextBoolean())?"m":"f";
                String domain = domains[rand.nextInt(domains.length)];
                String mail = String.format("%s.%s@%s",name,lastName,domain);
                Calendar birthdate = Calendar.getInstance();
                birthdate.set(Calendar.YEAR,1910+rand.nextInt(100));
                birthdate.set(Calendar.MONTH,1+rand.nextInt(11));
                birthdate.set(Calendar.DATE,1+rand.nextInt(25));

                writer.write(String.format("(%d,'%s','%s','%s','%s','%s')\n",id,lastName,name,gender,mail,sdf.format(birthdate.getTime())));
                if(j < 500-1){
                    writer.write(",");
                }
            }
            writer.write(";\n\n");
            writer.flush();
        }

        writer.close();
    }
}
