package de.tub.ditas.util;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class CollectionJson {
    private DateTimeFormatter dateParser = ISODateTimeFormat.dateTimeNoMillis();

    private String timestamp;
    private String throughput;

    public CollectionJson(String throughput) {
        timestamp = dateParser.print(System.currentTimeMillis());
        this.throughput = throughput;
    }
}
