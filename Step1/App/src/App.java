import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main(String[] args) throws Exception {
        System.out.println("starting");
        Scanner inFile1 = new Scanner(new File("../deploy/servers.txt")).useDelimiter("\r\n");
        List<String> temps = new ArrayList<String>();
        String token1 = "";
        // while loop
        while (inFile1.hasNext()) {
          // find next line
          token1 = inFile1.next();
          temps.add(token1);
//          System.out.print(token1);
        }
        
        String[] ips = temps.toArray(new String[0]);
        
        ProcessBuilder pb = new ProcessBuilder ("ssh",  "-o", "StrictHostKeyChecking=no", "yukliu@"+ips[1], "mkdir", "/tmp/yukliu");
//        ProcessBuilder scp = new ProcessBuilder ("scp",  "-r", "../deploy/slave_1.jar",  "yukliu@tp-1a201-08:/tmp/yukliu/");

        //pb.redirectErrorStream(true);
        //pb.inheritIO();
        Process p = pb.start();
                
        boolean b = p.waitFor(10, TimeUnit.SECONDS);
        if (b==false){
            System.out.println("There was a timeout!");
            p.destroy();
        }
        System.out.println("treating process:"+p.getErrorStream());
        InputStream is = p.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        
        while ((line = br.readLine()) != null) {
            System.out.println("standard line:" + line);
        }
        InputStream es = p.getErrorStream();
        BufferedReader ber = new BufferedReader(new InputStreamReader(es));
        String eline;

        while ((eline = ber.readLine()) != null) {
            System.out.println("error line:" +eline);
        }
        
        ProcessBuilder pb2 = new ProcessBuilder ("ssh", "-o", "StrictHostKeyChecking=no", "yukliu@"+ips[1], "rm", "-rf", "/tmp/yukliu");
        Process p2 = pb2.start();
        InputStream es2 = p2.getErrorStream();
        BufferedReader bes2 = new BufferedReader(new InputStreamReader(es2));
        while ((eline = bes2.readLine()) != null) {
            System.out.println("error line in destroy:" +eline);
        }
        
        // SCP
//        Process s = scp.start();
//        boolean bs = s.waitFor(10, TimeUnit.SECONDS);
//        if (bs == false){
//            System.out.println("There was a timeout!");
//            p.destroy();
//        }
//        
//        InputStream inScp = s.getInputStream();
//        
//        InputStream errScp = s.getErrorStream();
//        BufferedReader berScp = new BufferedReader(new InputStreamReader(errScp));
//        String eline2;
//
//        while ((eline2 = berScp.readLine()) != null) {
//            System.out.println("error line:" +eline2);
//        }
//        BufferedReader buScp = new BufferedReader(new InputStreamReader(inScp));
//        String lineScp;
//        
//        while ((lineScp = buScp.readLine()) != null) {
//            System.out.println("standard line:" + lineScp);
//        }

        System.out.print("Finished");
    }
}
