package de.tub.ditas.job;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.tub.ditas.util.*;
import de.tub.ditas.Job;
import de.tub.ditas.MonitorConfig;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IPTrafNg extends Job {

    private static int count = 0;
    private static String logFilePath = "/opt/vdc/mnt/testLog";

    private static final String COMMAND = "iptraf-ng";
    static String inter = "all";
    private transient DateTimeFormatter dateParser = ISODateTimeFormat.dateTimeNoMillis();


    public IPTrafNg(MonitorConfig config) {
        super(config);
    }

    /**
     * Calls a commandline tool to measure traffic and uses de.tub.ditas.util.IPTrafNgPars to parse the data
     * Then it sends the measure data to the elasticseach DB
     *
     * @throws IOException
     */
    private void CallIPTraf() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(COMMAND, "-i", inter,
                "-B", "-L", logFilePath);
        Process process = builder.start();
        while (process.isAlive()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {
            }
        }
        ArrayList<PacketObject> ipObjects = IPTrafNgPars.getInstance().readFile(logFilePath);
        sendBulk(ipObjects);
        new File(logFilePath).delete();
    }

    @Override
    public void run() {
        try {
            CallIPTraf();
            Logger.debug("traffic...");
        } catch (IOException ignored) {
            Logger.error(ignored, "failed to collect traffic data");
        }
    }

    /**
     * sends a a list of PacketObjects to the elastic search DB as json
     *
     * @param ob
     */
    private void sendBulk(ArrayList<PacketObject> ob) {
        //testBulk(ob);
        Gson gs = new GsonBuilder()
                .setFieldNamingStrategy(new atFieldNamingStrategy())
                .create();
        String jTemp = "";
        for (PacketObject o : ob) {
            IPPacketObject temp = new IPPacketObject(o.getDate(), o.getBytes(), o.getSender(), o.getReceiver());
            jTemp = jTemp + "{ \"create\" : { \"_index\" : \"" + config.indexName + "\", \"_type\" : \"" + temp.getComponent() + "\", \"_id\" : \"" + Integer.toHexString(Calendar.getInstance().hashCode()) + "\" } }\n";
            jTemp = jTemp + gs.toJson(temp) + "\n";
        }
        sendToElasticBulk(jTemp, "iptraf");
    }
}
