package de.tub.trace;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
@Component
public class Tracer implements Callback{

    private final Logger log = LoggerFactory.getLogger(this.getClass());

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
        log.info("started vdc java tracer @%s",uri);
    }

    public void push(Trace trace) {
        try {
            Request.Builder builder = new Request.Builder();
            builder.url(uri+"/trace");
            builder.put(RequestBody.create(MediaType.parse("application/json"), mapper.writeValueAsString(trace)));
            Request request = builder.build();
            client.newCall(request).enqueue(this);
            log.debug("send trace request for %s",trace.getSpanId());
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
                log.debug("send trace close request for %s",trace.getSpanId());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        log.debug("failed to send trace",e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        log.debug("trace send %d",response.code());
    }
}
