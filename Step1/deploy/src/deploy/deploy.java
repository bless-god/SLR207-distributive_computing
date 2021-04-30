package deploy;

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

public class deploy {

	public static void main(String[] args) throws Exception {
        System.out.println("starting");
        
        Scanner inFile1 = new Scanner(new File("servers.txt")).useDelimiter("\r\n");
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
        ArrayList<Process> processes = new ArrayList<Process>();
        ArrayList<Process> sendJar = new ArrayList<Process>();        
        
        for(String ip: ips) {
        	System.out.println("##"+ip+"**");
            ProcessBuilder pb = new ProcessBuilder ("ssh", "-o", "StrictHostKeyChecking=no", "yukliu@"+ip, "mkdir -p", "/tmp/yukliu");
            ProcessBuilder scp = new ProcessBuilder ("scp",  "-r", "slave_1.jar", "yukliu@"+ip+":/tmp/yukliu/");
            pb.redirectErrorStream(true);
            processes.add(pb.start());
            sendJar.add(scp.start());  
            
        }
        waitForProcesses(processes, 10);
        waitForProcesses(sendJar, 10);          
        
        System.out.print("Finished");
    }

	public static void waitForProcesses(ArrayList<Process> processes, long timeout) {
		ArrayList<Thread> threads = new ArrayList<>();
        for(Process p: processes) {
        	Runnable task = new Runnable() {
        		@Override
        		public void run() {
		        	System.out.println("treating process:"+p.isAlive());
		//        	System.out.println(p.exitValue());
		        	
		        	boolean b = false;
					try {
						b = p.waitFor(timeout, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.exit(1);
					}
		        	if(b==false) {
		        		System.out.println("Timeout in server"+p);
		        		p.destroy();
		        	}
		        	
		        	
		        	InputStream is = p.getInputStream();
		        	BufferedReader br = new BufferedReader(new InputStreamReader(is));
		        	String line = null;
		            
		        	try {
						line = br.readLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        	
		            while (line != null) {
		                System.out.println("standard line:" + line);
		                
		                try {
							line = br.readLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		            }          
		                        
        		}
    		};
    		Thread thread = new Thread(task);
    		threads.add(thread);
    	}
        
        for (Thread t : threads) { 
        	t.start(); 
    	}
        
        for (Thread t : threads) {
        	try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
    
    	
    
}
