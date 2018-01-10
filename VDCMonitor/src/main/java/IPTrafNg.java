import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class IPTrafNg implements Runnable {
    private static String logFilePath = "/var/log/testLog.txt";
    //private static String logOptions1 = "-i all -B -L ";
    private static int temp = 0;

    private static String COMMAND = "iptraf-ng";
    private static int sleepTime = 1000;


    static String inter = "all";

    public static void CallIPTraf() throws IOException {


        ProcessBuilder builder = new ProcessBuilder(COMMAND, "-i", inter,
                "-B", "-L", logFilePath.concat(Integer.toString(temp)));
        Process process = builder.start();
        while(process.isAlive()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}
        }

        readFromFiles(logFilePath.concat(Integer.toString(temp)));

        new File(logFilePath.concat(Integer.toString(temp))).delete();

        temp++;


    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(sleepTime);
                CallIPTraf();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void readFromFiles(String path){
        //TODO
    }

    public void setLogFilePath(String LOGFILEPATH) {
        this.logFilePath = LOGFILEPATH;
    }

    public void setInterface(String logOptions) {
        this.inter = logOptions;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }
}
