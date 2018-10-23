package main

import (
	"fmt"
	"net/http"
	"net/http/httputil"
	"os"
)

func handler(w http.ResponseWriter, req *http.Request) {
	dump, _ := httputil.DumpRequest(req, true)
	fmt.Printf("%s\n\n", string(dump))
}

func main() {
	host := "0.0.0.0"
	port := "80"

	listen := host + ":" + port

	fmt.Fprintf(os.Stderr, "Server listening on %s\n\n", listen)
	http.HandleFunc("/", handler)
	http.ListenAndServe(listen, nil)
}
