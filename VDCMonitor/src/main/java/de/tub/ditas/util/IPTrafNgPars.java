package de.tub.ditas.util;

import org.pmw.tinylog.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class IPTrafNgPars {
    private ArrayList<WhitelistObject> whitelist = new ArrayList<>();
    private static IPTrafNgPars ngPars = null;

    public static synchronized IPTrafNgPars getInstance(){
        if(ngPars==null){
            ngPars = new IPTrafNgPars();
        }
        return ngPars;

    }
    private IPTrafNgPars(){
    }
    /**
     * Reads the given file and uses methods createObject and reduceObjects
     * to create a list of all connections in the file
     * @param filePath
     * @return
     * @throws IOException
     */
    public  ArrayList<PacketObject> readFile(String filePath) throws IOException {
        Logger.debug("Read file");
        File auszug = new File(filePath);
        FileReader g = new FileReader(auszug);
        BufferedReader br = new BufferedReader(g);
        ArrayList<PacketObject> Objects = new ArrayList<>();
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] arg = line.split(";");

            if (arg.length == 2) {
                //Iptraf started
            } else if (arg.length > 2) {
                //TCP, ICMP and UDP
                PacketObject temp = createObject(arg);
                Objects.add(temp);
            }
        }

        return reduceObjects(Objects);


    }

    /**
     * reduces verbose lines from the objects list with the help of other methods
     * @param objects
     * @return
     */
    public ArrayList<PacketObject> reduceObjects(ArrayList<PacketObject> objects) {
        ArrayList<PacketObject> build = reduceHelper(objects);
        build = (ArrayList<PacketObject>) build.stream().sorted(new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                return compare((PacketObject) o1, (PacketObject) o2);
            }

            public int compare(PacketObject o1, PacketObject o2) {

                int x1 = (o1.getSender()).hashCode();
                int x2 = (o2.getSender()).hashCode();

                int sComp = 0;
                if (x1 < x2) sComp = 1;
                if (x1 > x2) sComp = -1;

                if (sComp != 0) {
                    return sComp;
                } else {
                    int j = o1.getReceiver().hashCode();
                    int i = o2.getReceiver().hashCode();
                    int tComp = 0;
                    if (j < i) tComp = 1;
                    if (j > i) tComp = -1;
                    return tComp;
                }
            }
        }).collect(Collectors.toList());
        try {
            build = reduceWhitelist(build);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        build = reduceHelper(build);
        return build;
    }
    /**
     * reduces verbose lines from the objects list and sums up the bytes send
     * @param objects
     * @return
     */
    public static ArrayList<PacketObject> reduceHelper(ArrayList<PacketObject> objects) {
        objects.add(new PacketObject(null, null, 0, null, null));
        ArrayList<PacketObject> build = new ArrayList<>();
        PacketObject temp;
        for (int i = 0; i < objects.size() - 1; i++) {
            if (objects.get(i).getSender().equals(objects.get(i + 1).getSender())
                    && objects.get(i).getReceiver().equals(objects.get(i + 1).getReceiver())
                    ) {
                objects.get(i + 1).setBytes(objects.get(i + 1).getBytes() + objects.get(i + 1).getBytes());
            } else {
                temp = objects.get(i);
                build.add(temp);
            }
        }
        return build;
    }

    /**
     * Throws out all the elements that are not part of the whitelist
     * @param objects
     * @return
     */
    public ArrayList<PacketObject> reduceWhitelist(ArrayList<PacketObject> objects) throws NullPointerException {
        ArrayList<PacketObject> temp = new ArrayList<>();
        for (PacketObject x : objects) {
            for (WhitelistObject y : ngPars.whitelist) {
                if (x.getReceiver().contains(y.getIp())) {
                    //System.out.println("Erste If Anweisung");
                    x.setReceiver(y.getName());
                    if (x.getSender().contains(y.getIp())) {
                        //System.out.println("Zweite If Anweisung");

                        x.setSender(y.getName());
                    }
                    temp.add(x);
                }else if(x.getSender().contains(y.getIp())){
                    //System.out.println("Else If Anweisung");
                    x.setSender(y.getName());
                    temp.add(x);
                }
            }
        }
        return temp;
    }


    /**
     * creates an data storing object for everyline of logged packet transactions
     *
     * @param line
     * @return
     */
    public static PacketObject createObject(String[] line) {
        String[] temp = line[4].split(" ");
        String sender = temp[2];
        String receiver = temp[4];
        String[] tempBytes = line[3].split(" ");
        int bytes = Integer.parseInt(tempBytes[1]);
        return new PacketObject(line[1], line[0], bytes, sender, receiver);
    }

    public ArrayList<WhitelistObject> getWhitelist() {
        return ngPars.whitelist;
    }

    public void setWhitelist(ArrayList<WhitelistObject> whitelist) {

        ngPars.whitelist = whitelist;
    }
}
