package de.tub.ditas.util;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class HeartbeatObject {
    private static long time;
    private long interval;

    private String timestamp;
    private transient DateTimeFormatter dateParser = ISODateTimeFormat.dateTimeNoMillis();



    public HeartbeatObject(){
        long cTime = System.currentTimeMillis();
        this.timestamp = dateParser.print(System.currentTimeMillis());
        if(time == 0){
            this.interval= 0;
        }else{
            this.interval = (cTime - time);
        }
        this.time =cTime;
    }

    public long getInterval(){
        return interval;
    }

    public String getTime(){
        return timestamp;
    }

}
