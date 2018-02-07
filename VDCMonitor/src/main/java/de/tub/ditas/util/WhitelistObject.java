package de.tub.ditas.util;

public class WhitelistObject {
    private String ip;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public WhitelistObject(String ip, String name) {
        this.ip = ip;
        this.name = name;

    }
}
