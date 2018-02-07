package de.tub.trace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class TraceHelper {

    @Autowired
    Tracer tracer;
    public <R> R wrapCall(Supplier<R> function,String operation){

        Trace parentTrace = Trace.extractFromThread();
        Trace trace = parentTrace;
        if(parentTrace != null){
            trace = parentTrace.ChildOf(operation);
            //all subsequent calls to trace will use me as a parent (at least in this thread)
            Trace.updateThread(trace);
            tracer.push(trace);
        }

        R result =  function.get();

        if(trace != null) {
            tracer.finish(trace);
            //as we finished this trace set it back to the parent (warning this could lead to race conditions and such)
            Trace.updateThread(parentTrace);
        }

        return result;
    }
    public <R> R wrapCall(Supplier<R> function){
       return wrapCall(function,null);
    }

    public void push(Trace trace){
        tracer.push(trace);
    }

    public void finish(Trace trace){
        tracer.finish(trace);
    }

}
