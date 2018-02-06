package de.tub.ditas;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MonitorConfig {
    public final String elasticSearchURI;
    public final String indexName;
    public final int invocationInverval;

    private MonitorConfig(String elasticSearchURI, String indexName, int invocationInverval) {
        this.elasticSearchURI = elasticSearchURI;
        this.indexName = indexName;
        this.invocationInverval = invocationInverval;
    }

    public static MonitorConfig fromEnvironment() {


        String index = System.getenv("elasticPathURI");
        String ipinterval = System.getenv("IP-Interval");
        String elastic = System.getenv("elasticURI");
        if (elastic == null) {
            elastic = "0.0.0.0:9200";
        }
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        if (index == null) {
            index = "vdcmonitor";
        }
        index = String.format("%s-%s",index,formater.format(Calendar.getInstance().getTime()));

        int interval;
        if (ipinterval == null) {
            interval = 5000;
        } else {
            interval = Integer.parseInt(ipinterval);
        }

        return new MonitorConfig(elastic, index, interval);
    }

    public String getElasticSearchURL() {
        return "http://" + elasticSearchURI + "/" + indexName;
    }
}
