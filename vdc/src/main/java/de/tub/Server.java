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

package de.tub;

import com.datastax.driver.core.Cluster;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = { "de.tub", "de.tub.api" })
public class Server implements CommandLineRunner {


    @Override
    public void run(String... arg0) throws Exception {
        if (arg0.length > 0 && arg0[0].equals("exitcode")) {
            throw new ExitException();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("wait for databases to settle");

        Properties properties = new Properties();
        try(InputStream stream = Server.class.getResourceAsStream("/application.properties")){
            properties.load(stream);
            String cassandraURI = properties.getProperty("cassandra.uri");
            String databaseURI = properties.getProperty("spring.datasource.url");

            boolean connection = ping(cassandraURI) && ping(databaseURI.substring("jdbc:mysql://".length(),databaseURI.lastIndexOf(':')));

            if(!connection){
                System.exit(-1);
            }

            waitForMySQL(databaseURI);

            //lets wait for good measure
            Thread.sleep(7200);
        } catch (IOException e){
            e.printStackTrace();
        }

        SpringApplication application =
                new SpringApplicationBuilder(Server.class)
                .initializers(new ApplicationContextInitializer<ConfigurableApplicationContext>() {
                    @Override
                    public void initialize(ConfigurableApplicationContext ctx) {
                        String cassandraURI = ctx.getEnvironment().getProperty("cassandra.uri");
                        String databaseURI = ctx.getEnvironment().getProperty("spring.datasource.url");

                        ping(cassandraURI);
                        ping(databaseURI);
                    }


                })
                .application();

        application.run(args);
    }

    private static boolean waitForMySQL(String uri) {

        while(!pingMySQL(uri)){
            System.out.println("wait for "+uri.substring(0,uri.lastIndexOf(":")));
            try {
                Thread.sleep(3600);
            } catch (InterruptedException e) {}

        }
        System.out.println(uri+" reachable");
        return true;

    }

    private static boolean pingMySQL(String databaseURI) {
        try {
            Connection connection = DriverManager.getConnection(databaseURI);
            connection.close();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }

    private static boolean ping(String uri) {
        try {
            InetAddress addresse = InetAddress.getByName(uri);
            while(!addresse.isReachable(3600)){
                System.out.println("wait for "+uri);
            }
            System.out.println(uri+" reachable");
            return true;
        } catch (IOException e) {
            System.out.println("failed to wait for "+uri);
            return false;
        }
    }
}
