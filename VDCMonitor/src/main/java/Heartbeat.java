import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Heartbeat implements Runnable {
    private String elastic;
    public Heartbeat(String elastic){
        this.elastic = elastic;
    }

    public void run() {
        int count = 0;
        while (true) {
            System.out.println("Test Elastic URI:"+ elastic);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
            HeartbeatObject ob = new HeartbeatObject();
            Gson gs = new Gson();
            String jString =gs.toJson(ob);
            HttpRequestWithBody request = Unirest.put("http://" + elastic +"/test/heartbeat/" + Integer.toString(count));
            request.body(jString);
            request.header("content-type", "application/json; charset=UTF-8 ");
            try {
                HttpResponse<JsonNode> jResponse = request.asJson();
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }

    }



}
