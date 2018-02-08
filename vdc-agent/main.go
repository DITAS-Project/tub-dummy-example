package main

import (
	"context"
	"encoding/json"
	"flag"
	"fmt"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"time"

	"github.com/gorilla/mux"
	log "github.com/sirupsen/logrus"

	opentracing "github.com/opentracing/opentracing-go"
	"github.com/opentracing/opentracing-go/ext"
	zipkin "github.com/openzipkin/zipkin-go-opentracing"
	"github.com/openzipkin/zipkin-go-opentracing/types"
)

func main() {
	//read cmd options
	portPtr := flag.Int("port", 8484, "port that the agent should listen on")
	zipkinAddressPtr := flag.String("zipkin", "http://localhost:9411/api/v1/spans", "zipkin address")
	vdcAddressPtr := flag.String("vdc", "http://0.0.0.0:0", "vdc address to be send to zipkin")
	var wait time.Duration
	flag.DurationVar(&wait, "wait", 15, "the duration for which the server gracefully wait for existing connections to finish in secounds")

	flag.Parse()

	log.SetLevel(log.DebugLevel)

	agent, err := newAgent(*zipkinAddressPtr, true, *vdcAddressPtr)

	if err != nil {
		log.Errorf("Failed to init agent %s", err)
		os.Exit(-1)
	}

	startServer(agent, *portPtr, wait)

}

func startServer(agent *agent, port int, waitTime time.Duration) {
	//setup routing
	apiRouter := mux.NewRouter()
	apiRouter.NotFoundHandler = http.HandlerFunc(notFound)

	v1 := apiRouter.PathPrefix("/v1").Subrouter()
	v1.PathPrefix("/close").Methods("POST").Handler(http.HandlerFunc(agent.close))
	v1.PathPrefix("/trace").Methods("PUT").Handler(http.HandlerFunc(agent.trace))

	//start server
	api := &http.Server{
		Addr:         fmt.Sprintf(":%d", port),
		WriteTimeout: time.Second * 15,
		ReadTimeout:  time.Second * 15,
		IdleTimeout:  time.Second * 60,
		Handler:      apiRouter,
	}

	go func() {
		log.Infof("Listening on :%d", port)
		if err := api.ListenAndServe(); err != nil {
			log.Fatal(err)
		}
	}()

	//gracefull shutdown @see mux github.com
	c := make(chan os.Signal, 1)
	signal.Notify(c, os.Interrupt)

	<-c

	ctx, cancel := context.WithTimeout(context.Background(), waitTime)
	defer cancel()
	agent.Shutdown()
	api.Shutdown(ctx)
	log.Info("shutting down")
	os.Exit(0)
}

type traceMessage struct {
	TraceId      string `json:"traceId"`
	ParentSpanId string `json:"parentSpanId"`
	SpanId       string `json:"spanId"`
	Operation    string `json:"operation"`
	Message      string `json:"message"`
}

func (t traceMessage) build() *zipkin.SpanContext {
	var pid *uint64
	ppid, err := strconv.ParseUint(t.ParentSpanId, 16, 64)
	if err != nil {
		pid = nil
	} else {
		pid = &ppid
	}
	sid, err := strconv.ParseUint(t.SpanId, 16, 64)
	if err != nil {
		log.Errorf("did not parse sid %s - %s", t.SpanId, err)
		return nil
	}

	tid, err := types.TraceIDFromHex(t.TraceId)
	if err != nil {
		log.Errorf("did not parse tid %s - %s", t.TraceId, err)
		return nil
	}

	context := zipkin.SpanContext{
		TraceID:      tid,
		ParentSpanID: pid,
		SpanID:       sid,
		Sampled:      true,
	}

	return &context
}

type agent struct {
	spans     map[string]opentracing.Span
	collector zipkin.Collector
}

func newAgent(zipkinAddressPtr string, debug bool, vdcAddress string) (*agent, error) {
	// Create our HTTP collector.
	collector, err := zipkin.NewHTTPCollector(zipkinAddressPtr)

	if err != nil {
		log.Errorf("unable to create Zipkin HTTP collector: %+v\n", err)
		return nil, err
	}

	// Create our recorder.
	recorder := zipkin.NewRecorder(collector, debug, vdcAddress, "vdc-agent")

	tracer, err := zipkin.NewTracer(recorder,
		zipkin.WithLogger(zipkin.LoggerFunc(func(kv ...interface{}) error {
			log.Info(kv)
			return nil
		})),
		zipkin.DebugMode(debug),
		zipkin.DebugAssertUseAfterFinish(debug),
		zipkin.DebugAssertUseAfterFinish(debug),
	)

	if err != nil {
		log.Errorf("unable to create Zipkin tracer: %+v\n", err)
		return nil, err
	}

	opentracing.InitGlobalTracer(tracer)

	var ctx = agent{
		spans:     make(map[string]opentracing.Span),
		collector: collector,
	}

	return &ctx, nil
}

func (a *agent) Shutdown() {
	a.collector.Close()
}

func notFound(w http.ResponseWriter, req *http.Request) {
	log.Infof("request not found", req.URL)
}
func (a *agent) getSpan(trace traceMessage) opentracing.Span {

	if span, ok := a.spans[trace.TraceId+trace.SpanId]; ok {
		log.Infof("updateing trace %s", trace.SpanId)
		return span
	}

	log.Infof("building trace %s", trace.SpanId)
	var context = trace.build()

	if context != nil {
		span := opentracing.StartSpan(trace.Operation, ext.RPCServerOption(*context))
		a.spans[trace.TraceId+trace.SpanId] = span
		log.Infof("trace %s build", trace.SpanId)
		return span
	}

	return opentracing.StartSpan(trace.Operation)

}

func (a *agent) trace(w http.ResponseWriter, req *http.Request) {
	log.Info("got trace request")

	var trace traceMessage
	_ = json.NewDecoder(req.Body).Decode(&trace)

	log.Infof("trace request for %s : %s", trace.SpanId, trace.Operation)

	span := a.getSpan(trace)

	if trace.Message != "" {
		span.LogEvent(trace.Message)
	}
}

func (a *agent) close(w http.ResponseWriter, req *http.Request) {
	log.Info("got trace finish request")

	var trace traceMessage
	_ = json.NewDecoder(req.Body).Decode(&trace)

	log.Infof("trace request for %s : %s", trace.SpanId, trace.Operation)

	a.getSpan(trace).Finish()
}
