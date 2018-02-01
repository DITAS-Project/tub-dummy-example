import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import util.CollectionJson;
import util.PacketObject;
import util.atFieldNamingStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class IPTrafNg implements Runnable {
    private static String elastic;
    private static String path;
    private static int count = 0;
    private static String logFilePath = "/opt/vdc/mnt/testLog";
    //private static String logOptions1 = "-i all -B -L ";
    //private static int temp = 0;
    private static String COMMAND = "iptraf-ng";
    private static int interval;
    static String inter = "all";

    public IPTrafNg(String elastic, String path, int interval) {
        this.elastic = elastic;
        this.path = path;
        this.interval = interval;
        System.out.println("Interval" + interval);
    }

    /**
     * Calls a commandline tool to measure traffic and uses IPTrafNgPars to parse the data
     * Then it sends the measure data to the elasticseach DB
     * @throws IOException
     */
    public void CallIPTraf() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(COMMAND, "-i", inter,
                "-B", "-L", logFilePath);
        Process process = builder.start();
        while (process.isAlive()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
        ArrayList<PacketObject> ipObjects = IPTrafNgPars.getInstance().readFile(logFilePath);
        sendList(ipObjects);
        File delete = new File(logFilePath);
        delete.delete();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(interval);
                CallIPTraf();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * sends a a list of PacketObjects to the elastic search DB as json
     * @param ob
     */
    public static void sendList(ArrayList<PacketObject> ob) {
        Gson gs = new GsonBuilder()
                .setFieldNamingStrategy(new atFieldNamingStrategy())
                .create();
        String jString = gs.toJson(ob);
        HttpRequestWithBody request = Unirest.post("http://" + elastic + path + "/iptraf/" +
                Integer.toHexString(Calendar.getInstance().hashCode()));
        String finish = gs.toJson(new CollectionJson(jString));
        request.body(finish);
        request.header("content-type", "application/json; charset=UTF-8 ");
        try {
            HttpResponse<JsonNode> jResponse = request.asJson();
            System.out.println(jResponse.getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    /**
     * sends a single  PacketObject to the elastic search DB as json
     * @param ob
     */
    public static void sendIPEntity(PacketObject ob) {
        Gson gs = new GsonBuilder()
                .setFieldNamingStrategy(new atFieldNamingStrategy())
                .create();
        String jString = gs.toJson(ob);
        HttpRequestWithBody request = Unirest.post("http://" + elastic + path + "/iptraf/" +
                Integer.toHexString(Calendar.getInstance().hashCode()));
        request.body(jString);
                Integer.toHexString(Calendar.getInstance().hashCode());
        request.header("content-type", "application/json; charset=UTF-8 ");
        try {
            HttpResponse<JsonNode> jResponse = request.asJson();
            System.out.println(jResponse.getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    public void setLogFilePath(String LOGFILEPATH) {
        this.logFilePath = LOGFILEPATH;
    }

    public void setInterface(String logOptions) {
        this.inter = logOptions;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
