package de.tub.trace;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Tracer {

    private final OkHttpClient client;
    private final String uri;
    private final ObjectMapper mapper;

    public Tracer(@Value("${tracing.uri}") String uri){
        client = new OkHttpClient();
        mapper = new ObjectMapper();
        this.uri = uri;
    }

    public void push(Trace trace) {
        try {
            Request.Builder builder = new Request.Builder();
            builder.url(uri);
            builder.put(RequestBody.create(MediaType.parse("application/json"), mapper.writeValueAsBytes(trace)));
            Request request = builder.build();
            client.newCall(request);
        } catch (JsonProcessingException e) {}
    }

    public void finish(Trace trace) {
        Request.Builder builder = new Request.Builder();
        builder.url(uri);
        try {
            builder.post(RequestBody.create(MediaType.parse("application/json"), mapper.writeValueAsBytes(trace)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Request request = builder.build();
        client.newCall(request);
    }
}
