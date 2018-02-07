package de.tub.ditas.job;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import de.tub.ditas.util.IPTrafNgPars;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import de.tub.ditas.util.WhitelistObject;

public class TailFile implements Runnable {
    private static String tail = "/opt/vdc/mnt/whitelist";
    //private static String tailOutSide = "/home/paavo/IdeaProjects/dummy-example/vdc/mnt/whitelist";
    private static ArrayList<WhitelistObject> whitelistObjects;

    public TailFile() {
        whitelistObjects = IPTrafNgPars.getInstance().getWhitelist();
    }

    public void run() {
        Path logFile = Paths.get(tail);
        MyListener listener = new MyListener();
        Tailer blu = new Tailer(logFile.toFile(), listener, 100);
        blu.run();
    }

    public static class MyListener extends TailerListenerAdapter {


        public MyListener() {
            ArrayList<WhitelistObject> whitelist = IPTrafNgPars.getInstance().getWhitelist();
        }

        @Override
        public void handle(String line) {
            line = line.replace('(', ' ').replace(')', ' ').replace('\'', ' ').trim();
            String[] args = line.split(",");
            WhitelistObject wl = new WhitelistObject(args[1], args[0]);
            whitelistObjects.add(wl);
        }
    }
}
