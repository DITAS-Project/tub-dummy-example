package de.tub.trace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class TraceHelper {

    @Autowired
    Tracer tracer;
    public <R> R wrapCall(Supplier<R> function,String operation){
        Trace trace = Trace.extractFromThread();

        if(trace != null){
            trace = trace.ChildOf(operation);
            tracer.push(trace);
        }

        R result =  function.get();

        if(trace != null) {
            tracer.finish(trace);
        }

        return result;
    }
    public <R> R wrapCall(Supplier<R> function){
       return wrapCall(function,null);
    }

}
