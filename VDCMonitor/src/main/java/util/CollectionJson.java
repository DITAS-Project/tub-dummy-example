package util;

public class CollectionJson {
    private long timestamp = System.currentTimeMillis();
    private String throughput;

    public CollectionJson(String throughput){
        this.throughput = throughput;
    }
}
