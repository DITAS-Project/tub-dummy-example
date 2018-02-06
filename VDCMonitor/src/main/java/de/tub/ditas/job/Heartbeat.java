package de.tub.ditas.job;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import de.tub.ditas.Job;
import de.tub.ditas.MonitorConfig;
import de.tub.ditas.util.HeartbeatObject;
import de.tub.ditas.util.atFieldNamingStrategy;
import org.pmw.tinylog.Logger;


import java.util.Calendar;

public class Heartbeat extends Job {


    public Heartbeat(MonitorConfig config) {
        super(config);
    }

    /**
     * Sending an de.tub.ditas.job.Heartbeat to the given url
     */
    public void run() {
        HeartbeatObject ob = new HeartbeatObject();
        String jString = toJSONString(ob);
        sendToElastic(jString,"heartbeat");
        Logger.debug("beat...");
    }



    private String toJSONString(HeartbeatObject ob) {
        Gson gs = new GsonBuilder()
                .setFieldNamingStrategy(new atFieldNamingStrategy())
                .create();
        return gs.toJson(ob);
    }


}
