package destroy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class destroy {
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
        
    	ArrayList<Process> destroy = new ArrayList<Process>();
        for(String ip: ips) {
        	System.out.println("##"+ip+"**");
            ProcessBuilder pb = new ProcessBuilder ("ssh", "-T", "-o", "StrictHostKeyChecking=no", "yukliu@"+ip, "rm", "-rf", "/tmp/yukliu");
            destroy.add(pb.start());
//            pb.command(); 
        }
        
        for(Process p: destroy) {
        	System.out.println("treating process:"+p.isAlive());
//        	System.out.println(p.exitValue());
        	boolean b = p.waitFor(10, TimeUnit.SECONDS);
        	if(b==false) {
        		System.out.println("Timeout in server"+p);
        		p.destroy();
        	}
        	
        	InputStream is = p.getInputStream();
        	BufferedReader br = new BufferedReader(new InputStreamReader(is));
        	String line;
            
            while ((line = br.readLine()) != null) {
                System.out.println("standard line:" + line);
            }
        	
        	InputStream es = p.getErrorStream();
        	BufferedReader errMsg = new BufferedReader(new InputStreamReader(es));
            while ((line = errMsg.readLine()) != null) {
                System.out.println("error line:" + line);
            }            
                        
        }        

        System.out.print("Finished");
	}
	

}
