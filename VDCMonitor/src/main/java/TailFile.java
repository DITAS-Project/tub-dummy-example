
package cryptoproxy.LogTester;
        import java.io.IOException;
        import java.nio.file.Files;
        import java.nio.file.Path;
        import java.nio.file.Paths;
        import org.apache.commons.io.input.Tailer;
        import org.apache.commons.io.input.TailerListenerAdapter;
public class TailFile implements Runnable {
    private static String tail= "C:\\Users\\Paavo.Camps\\Desktop\\CodeStuff\\Log.txt";


    public void run() {
        //try {
        //    Files.createFile(Paths.get(tail));
        //} catch (IOException e) {
         //   e.printStackTrace();
        //}
        Path logFile = Paths.get(tail);
        MyListener listener = new MyListener();
        Tailer.create(logFile.toFile(), listener, 50);

        //while(true){
        //}
    }
    private static class MyListener extends TailerListenerAdapter {
        @Override
        public void handle(String line) {
            System.out.println(line);
        }
    }
}
