public class HeartbeatObject {
    private static long lastTimestamp;
    private long interval;
    private long time;

    public HeartbeatObject(){
        this.time =System.currentTimeMillis();
        if(lastTimestamp == 0){
            this.interval= 0;
        }else{
            this.interval = (time - lastTimestamp);
        }
        this.lastTimestamp =time;
    }

    public long getInterval(){
        return interval;
    }

    public long getTime(){
        return time;
    }



}
