
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import de.tub.ditas.job.Heartbeat;
import de.tub.ditas.job.IPTrafNg;
import de.tub.ditas.MonitorConfig;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VDCMonitor {
    public static void main(String[] args) {
        Configurator.defaultConfig().writer(new ConsoleWriter()).level((args.length > 0?Level.TRACE:Level.OFF)).activate();
        MonitorConfig config = MonitorConfig.fromEnvironment();

        waitForConnection(config);


        ScheduledExecutorService jobRunner = Executors.newScheduledThreadPool(2);

        jobRunner.scheduleAtFixedRate(new Heartbeat(config),5,config.invocationInverval, TimeUnit.MILLISECONDS);
        jobRunner.scheduleAtFixedRate(new IPTrafNg(config),5,config.invocationInverval, TimeUnit.MILLISECONDS);
        Logger.info("started VDC Monitor");

    }

    private static void waitForConnection(MonitorConfig config) {
        while (!connectToElastic(config)){
            Logger.info("waiting for elasticsearch connection");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {}
        }
    }

    private static boolean connectToElastic(MonitorConfig config){
        try {
             Unirest.head(config.getElasticSearchURL()).asString();
            return true;
        } catch (UnirestException e) {
            return false;
        }
    }
}
