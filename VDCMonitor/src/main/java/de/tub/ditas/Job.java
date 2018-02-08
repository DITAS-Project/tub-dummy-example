package de.tub.ditas;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.pmw.tinylog.Logger;

import java.util.Calendar;

public abstract class Job implements Runnable{

    protected final MonitorConfig config;

    public Job(MonitorConfig config){
        this.config = config;
    }

    public void sendToElastic(String payload,String type) {
        HttpRequestWithBody request = Unirest.post(config.getElasticSearchURL()+"/"+type+"/"+
                Integer.toHexString(Calendar.getInstance().hashCode()));
        request.body(payload);
        request.header("content-type", "application/json; charset=UTF-8 ");
        try {
            HttpResponse<JsonNode> jResponse = request.asJson();
            Logger.debug("send "+type+" data got"+jResponse.getStatus() );
        } catch (UnirestException e) {
            Logger.error(e,"could not send "+type+" data");
        }
    }

    public void sendToElasticBulk(String payload,String type) {
        HttpRequestWithBody request = Unirest.post(config.getElasticSearchURL()+"/_bulk");
        request.body(payload);
        request.header("content-type", "application/json; charset=UTF-8 ");
        try {
            HttpResponse<JsonNode> jResponse = request.asJson();
            Logger.debug("send "+type+" data got"+jResponse.getStatus() );
            System.out.println(jResponse.getBody());
            System.out.println(payload);
        } catch (UnirestException e) {
            Logger.error(e,"could not send "+type+" data");
        }
    }

}
