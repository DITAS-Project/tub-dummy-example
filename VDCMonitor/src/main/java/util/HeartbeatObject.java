package util;

public class HeartbeatObject {
    private static long timestamp;
    private long interval;
    private long time;

    public HeartbeatObject(){
        this.time =System.currentTimeMillis();
        if(timestamp == 0){
            this.interval= 0;
        }else{
            this.interval = (time - timestamp);
        }
        this.timestamp =time;
    }

    public long getInterval(){
        return interval;
    }

    public long getTime(){
        return time;
    }



}
