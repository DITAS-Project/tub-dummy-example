package de.tub.trace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TracingInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    Tracer tracer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Trace trace = Trace.extractFromRequest(request);

        if(!trace.isTrace()){
            return true;
        } else {
            //fork request preHandle
            //WHY you no kotlin! .?
            if(handler != null && ((HandlerMethod)handler).getMethod() != null){
                trace = trace.ChildOf(((HandlerMethod)handler).getMethod().getName());
            } else {
                trace = trace.ChildOf();
            }

            //inject trace into request-object afterwards reachable using Trace.extractFromThread()
            request.setAttribute("trace",trace);
            tracer.push(trace);
            return  true;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        tracer.finish(Trace.extractFromThread());
    }
}
