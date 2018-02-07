package de.tub.ditas.job;

import de.tub.ditas.util.IPTrafNgPars;
import de.tub.ditas.util.PacketObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;


public class IPTrafNgTest {

    @Test
    public void parsingTest(){
        try {
            ArrayList<PacketObject> ipObjects = IPTrafNgPars.getInstance().readFile("test.log");
            System.out.println(ipObjects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}