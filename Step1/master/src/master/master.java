package master;

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

public class master {

    public static void main(String[] args) throws Exception {
        System.out.println("starting");
        
        Scanner inFile1 = new Scanner(new File("../deploy/servers.txt")).useDelimiter("\r\n");
        List<String> temps = new ArrayList<String>();
        String token1 = "";
        while (inFile1.hasNext()) {
          token1 = inFile1.next();
          temps.add(token1);
        }
        
        String[] ips = temps.toArray(new String[0]);
        ArrayList<Process> splitPaths = new ArrayList<Process>();
        ArrayList<Process> splitSends = new ArrayList<Process>();
        ArrayList<Process> splitRuns = new ArrayList<Process>();
        ArrayList<Process> mapFolders = new ArrayList<Process>();
        ArrayList<Process> shuffleFolders = new ArrayList<Process>();
        
        int i = 0;
        for(String ip: ips) {
        	System.out.println("##"+ip+"**");
//            ProcessBuilder pb = new ProcessBuilder ("ssh", "-o", "StrictHostKeyChecking=no", "yukliu@"+ip, "cd /tmp/yukliu; java -jar slave_1.jar");
            ProcessBuilder splitPath = new ProcessBuilder ("ssh", "-o", "StrictHostKeyChecking=no", "yukliu@"+ip, "mkdir -p", "/tmp/yukliu/splits/");
            ProcessBuilder splitSend = new ProcessBuilder ("scp", "-r", "s" +i+ ".txt", "yukliu@"+ip+":/tmp/yukliu/splits/");
            ProcessBuilder splitRun = new ProcessBuilder ("ssh", "-o", "StrictHostKeyChecking=no", "yukliu@"+ip, 
            		"cd /tmp/yukliu; java -jar slave_1.jar 0 "+i);
            // ./splits/s"+i+".txt");
            ProcessBuilder mapFolder = new ProcessBuilder ("ssh", "-o", "StrictHostKeyChecking=no", "yukliu@"+ip, "mkdir -p", "/tmp/yukliu/maps/");
            
            ProcessBuilder serversFile = new ProcessBuilder ("scp", "-r", "../deploy/servers.txt", "yukliu@"+ip+"/tmp/yukliu/machines.txt");       
            
            ProcessBuilder shuffleFolder = new ProcessBuilder ("ssh", "-o", "StrictHostKeyChecking=no", "yukliu@"+ip, "mkdir -p", "/tmp/yukliu/shuffles/");          
            
            i += 1;
            
            splitPath.redirectErrorStream(true);
            splitPaths.add(splitPath.start());   
            
            splitSend.redirectErrorStream(true);
            splitSends.add(splitSend.start());
            
            splitRun.redirectErrorStream(true);
            splitRuns.add(splitRun.start());
            
            mapFolder.redirectErrorStream(true);
            mapFolders.add(mapFolder.start());
            shuffleFolders.add(shuffleFolder.start());
        }
        
        waitForProcesses(splitPaths, 10);
        waitForProcesses(splitSends, 10);
        waitForProcesses(splitRuns, 10);
        waitForProcesses(mapFolders, 10);

        System.out.print("Finished");
    }
    
    public static void waitForProcesses(ArrayList<Process> processes, long timeout) {
		ArrayList<Thread> threads = new ArrayList<>();
        for(Process p: processes) {
        	Runnable task = new Runnable() {
        		@Override
        		public void run() {
//		        	System.out.println("treating process:"+p.info());
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
		                System.out.println("Info:" + line);
		                
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
