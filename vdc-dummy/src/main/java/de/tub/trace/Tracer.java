package de.tub.trace;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class Tracer implements Callback{

    private final OkHttpClient client;
    private final String uri;
    private final ObjectMapper mapper;

    public Tracer(@Value("${tracing.uri}") String uri) {
        client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.SECONDS)
                .followRedirects(false)
                .connectionPool(new ConnectionPool(2,2,TimeUnit.MINUTES))
                .build();
        mapper = new ObjectMapper();
        this.uri = uri;
    }

    public void push(Trace trace) {
        try {
            Request.Builder builder = new Request.Builder();
            builder.url(uri+"/trace");
            builder.put(RequestBody.create(MediaType.parse("application/json"), mapper.writeValueAsString(trace)));
            Request request = builder.build();
            client.newCall(request).enqueue(this);
        } catch (JsonProcessingException e) {
        }
    }

    public void finish(Trace trace) {
        if(trace != null) {
            try {
                Request.Builder builder = new Request.Builder();
                builder.url(uri + "/close");
                builder.post(RequestBody.create(MediaType.parse("application/json"), mapper.writeValueAsString(trace)));

                Request request = builder.build();
                client.newCall(request).enqueue(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        e.printStackTrace();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

    }
}
