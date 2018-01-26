import org.apache.commons.io.input.Tailer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VDCMonitor {
    private static String elasticDefaultURI = "0.0.0.0:9200/";
    public static void main(String[] args) throws InterruptedException {
        String elastic = System.getenv("elasticURI");
        String index = System.getenv("elasticPathURI");
        String ipinterval = System.getenv("IP-Interval");

        if(elastic==null){
            elastic=elasticDefaultURI;
        }
        if(index==null){
            index= "/vdcmonitor";
        }
        int interval;
        if(ipinterval == null){
            interval= 5000;
        }else{
            interval= Integer.parseInt(ipinterval);
        }

        //TailFile tf = new TailFile("/var/log/mnt/whitelist");
        Heartbeat hb = new Heartbeat(elastic, index);
        IPTrafNg ipTraf = new IPTrafNg(elastic,index, interval);
       // Thread tft = new Thread(tf);
        Thread hbt =  new Thread(hb);
        Thread ipt = new Thread(ipTraf);
        hbt.start();
        ipt.start();
        //tft.start();
    }
}
