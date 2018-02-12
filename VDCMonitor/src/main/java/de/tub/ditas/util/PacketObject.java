package de.tub.ditas.util;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class PacketObject {

    private transient DateTimeFormatter dateParser = ISODateTimeFormat.dateTimeNoMillis();
    private String date;
    private int bytes;
    private String sender;
    private String receiver;
    private String timestamp = dateParser.print(System.currentTimeMillis());


    public PacketObject(String date, int bytes , String sender, String receiver){
        this.date= date;
        this.bytes = bytes;
        this.sender= sender;
        this.receiver = receiver;
    }

    public String getDate() {
        return date;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
