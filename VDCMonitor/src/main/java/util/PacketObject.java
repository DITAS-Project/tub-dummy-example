package util;

public class PacketObject {


    private String date;
    private int bytes;
    private String sender;
    private String receiver;

    public PacketObject(String protocol, String date, int bytes , String sender, String receiver){
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

}
