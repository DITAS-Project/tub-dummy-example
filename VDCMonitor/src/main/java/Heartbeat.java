import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import util.HeartbeatObject;
import util.atFieldNamingStrategy;


import java.util.Calendar;

public class Heartbeat implements Runnable {
    private String elastic;
    private String index;
    private int interval;

    public Heartbeat(String elastic, String index) {
        this.elastic = elastic;
        this.index = index;
    }

    /**
     * Sending an Heartbeat to the given url
     */
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HeartbeatObject ob = new HeartbeatObject();
            Gson gs = new GsonBuilder()
                    .setFieldNamingStrategy(new atFieldNamingStrategy())
                    .create();
            String jString = gs.toJson(ob);
            HttpRequestWithBody request = Unirest.post("http://" + elastic + index +"/heartbeat/"+
                    Integer.toHexString(Calendar.getInstance().hashCode()));
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
