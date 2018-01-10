public class VDCMonitor {
    private static String elasticDefaultURI = "0.0.0.0:9200/";
    public static void main(String[] args){
        String elastic = System.getenv("elasticURI");
        if(elastic==null){
            elastic=elasticDefaultURI;
        }
        Heartbeat hb = new Heartbeat(elastic);
        IPTrafNg ipTraf = new IPTrafNg();
        Thread hbt =  new Thread(hb);
        Thread ipt = new Thread(ipTraf);
        hbt.start();
        ipt.start();
    }
}
