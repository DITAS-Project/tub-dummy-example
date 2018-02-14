package de.tub.ditas.util;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;

public class IPPacketObject{

        private transient DateTimeFormatter dateParser = ISODateTimeFormat.dateTimeNoMillis();
        private String date;
        private int bytes;
        private String component;
        private String timestamp = dateParser.print(System.currentTimeMillis());


        public IPPacketObject(String date, int bytes , String sender, String receiver){
            ArrayList<WhitelistObject> whitelistObjects = IPTrafNgPars.getInstance().getWhitelist();
            if(whitelistObjects.contains(sender)) component = sender;
            else component = receiver;
            this.date= date;
            this.bytes = bytes;
        }

        public String getDate() {
            return date;
        }

        public String getComponent(){
            return component;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getBytes() {
            return bytes;
        }

        public void setBytes(int bytes) {
            this.bytes = bytes;
        }

        public String getTimestamp() {
            return timestamp;
        }
}


