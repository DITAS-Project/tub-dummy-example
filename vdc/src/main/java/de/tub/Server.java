package de.tub;

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
import java.net.InetAddress;
import java.net.UnknownHostException;

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
        Thread.sleep(7200);
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

                    private void ping(String uri) {
                        try {
                            InetAddress addresse = InetAddress.getByName(uri);
                            while(!addresse.isReachable(3600)){
                                System.out.println("wait for "+uri);
                            }
                            System.out.println(uri+" reachable");
                        } catch (IOException e) {
                            System.out.println("failed to wait for "+uri);
                        }
                    }
                })
                .application();

        application.run(args);
    }

    class ExitException extends RuntimeException implements ExitCodeGenerator {
        private static final long serialVersionUID = 1L;

        @Override
        public int getExitCode() {
            return 10;
        }

    }
}
