package de.tub.ditas.job;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import de.tub.ditas.util.IPTrafNgPars;
import de.tub.ditas.Job;
import de.tub.ditas.MonitorConfig;
import de.tub.ditas.util.CollectionJson;
import de.tub.ditas.util.PacketObject;
import de.tub.ditas.util.atFieldNamingStrategy;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IPTrafNg extends Job {

    private static int count = 0;
    private static String logFilePath = "/opt/vdc/mnt/testLog";

    private static final String COMMAND = "iptraf-ng";
    static String inter = "all";

    public IPTrafNg(MonitorConfig config) {
        super(config);
    }

    /**
     * Calls a commandline tool to measure traffic and uses de.tub.ditas.util.IPTrafNgPars to parse the data
     * Then it sends the measure data to the elasticseach DB
     * @throws IOException
     */
    private void CallIPTraf() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(COMMAND, "-i", inter,
                "-B", "-L", logFilePath);
        Process process = builder.start();
        while (process.isAlive()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) { }
        }
        ArrayList<PacketObject> ipObjects = IPTrafNgPars.getInstance().readFile(logFilePath);
        sendList(ipObjects);
        new File(logFilePath).delete();
    }

    @Override
    public void run() {
        try {
            CallIPTraf();
            Logger.debug("traffic...");
        } catch (IOException ignored) {
            Logger.error(ignored,"failed to collect traffic data");
        }
    }


    /**
     * sends a a list of PacketObjects to the elastic search DB as json
     * @param ob
     */
    private void sendList(ArrayList<PacketObject> ob) {
        Gson gs = new GsonBuilder()
                .setFieldNamingStrategy(new atFieldNamingStrategy())
                .create();
        String finish = gs.toJson(new CollectionJson(gs.toJson(ob)));
        sendToElastic(finish,"iptraf");
    }

    /**
     * sends a single  PacketObject to the elastic search DB as json
     * @param ob
     */
    public  void sendIPEntity(PacketObject ob) {
        Gson gs = new GsonBuilder()
                .setFieldNamingStrategy(new atFieldNamingStrategy())
                .create();
        String jString = gs.toJson(ob);
        sendToElastic(jString,"iptraf");
    }


}
