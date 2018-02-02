package de.tub.trace;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.github.kristofa.brave.SpanId;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;

public class Trace implements Serializable {

    public static final long serialVersionUID = 354894341;

    private final String traceId;
    private final String parentSpanId;
    private final String spanId;
    private final String sampled;
    private final String operation;

    @JsonInclude @NotNull
    public String getTraceId() {
        return traceId;
    }

    @JsonInclude
    public String getParentSpanId() {
        return parentSpanId;
    }

    @JsonInclude
    public String getSpanId() {
        return spanId;
    }

    @JsonInclude
    public String getSampled() {
        return sampled;
    }

    @JsonInclude
    public String getOperation() {
        return operation;
    }

    public Trace(String traceId, String parentSpanId, String spanId, String sampled) {
        this(traceId,parentSpanId,spanId,sampled,null);
    }

    public Trace(String traceId, String parentSpanId, String spanId, String sampled, String operation) {
        this.traceId = traceId;
        this.parentSpanId = parentSpanId;
        this.spanId = spanId;
        this.sampled = sampled;
        this.operation = operation;
    }

    public boolean isTrace() {
        return (traceId != null && spanId != null && ((sampled == null) || sampled.equals("1")));
    }

    @Override
    public String toString() {
        return String.format("(%s)[%s] %s", parentSpanId, spanId, traceId);
    }

    public static Trace extractFromRequest(HttpServletRequest request){
        return new Trace(request.getHeader("X-B3-TraceId"),
                request.getHeader("X-B3-ParentSpanId "),
                request.getHeader("X-B3-SpanId"),
                request.getHeader("X-B3-Sampled"));
    }

    public static Trace extractFromThread(){
        Object trace = RequestContextHolder.currentRequestAttributes().getAttribute("trace", RequestAttributes.SCOPE_REQUEST);
        if(trace != null){
            return (Trace) trace;
        } else {
            return null;
        }
    }

    public Trace ChildOf(String operation){
        return new Trace(traceId,spanId, id(),sampled,operation);
    }

    public Trace ChildOf(){
        return new Trace(traceId,spanId, id(),sampled,operation);
    }

    private String id(){
        return Long.toHexString(new Random().nextLong());
    }

    public ByteBuffer toBuffer(){
        SpanId.Builder builder = SpanId.builder().sampled(true);
        BigInteger traceId = new BigInteger(this.getTraceId(), 16);

        if(traceId.bitLength() > 64) {
            builder.traceIdHigh(traceId.and(BigInteger.valueOf(Long.MAX_VALUE).shiftLeft(Long.SIZE)).shiftRight(Long.SIZE).longValue()); //get the upper part
            builder.traceId(traceId.and(BigInteger.valueOf(Long.MAX_VALUE)).longValue());//lower part
        } else {
            builder.traceId(traceId.longValue());
        }

        builder.parentId(new BigInteger(this.getParentSpanId(),16).longValue());
        builder.spanId(new BigInteger(this.getParentSpanId(),16).longValue());

        return ByteBuffer.wrap(builder.build().bytes());
    }
}