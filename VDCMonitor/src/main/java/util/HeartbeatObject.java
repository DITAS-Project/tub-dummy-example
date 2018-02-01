package util;

public class HeartbeatObject {
    private static long time;
    private long interval;

    private long timestamp;


    public HeartbeatObject(){
        this.timestamp =System.currentTimeMillis();
        if(time == 0){
            this.interval= 0;
        }else{
            this.interval = (timestamp - time);
        }
        this.time =timestamp;
    }

    public long getInterval(){
        return interval;
    }

    public long getTime(){
        return timestamp;
    }



}
