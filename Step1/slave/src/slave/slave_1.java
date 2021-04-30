package slave;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class slave_1 {
	public static void main(String[] args) throws Exception {
		String mode;
		String fileName;
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		
		mode = args[0];
		System.out.print(mode);
		switch (mode) {
		// 0 - split
		// 1 - shuffle
		
			case "0": 
				fileName = "./splits/s"+args[1]+".txt";
				Scanner inFile1 = new Scanner(new File(fileName)).useDelimiter(" ");
		        List<String> temps = new ArrayList<String>();
		        String token1 = "";
	
		        while (inFile1.hasNext()) {
		          token1 = inFile1.next();
		          temps.add(token1);
		        }
				System.out.println(wordMap);
				File fout = new File("./maps/UM"+args[1]+".txt");
			    try (FileOutputStream fos = new FileOutputStream(fout); BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));) {
			        for (String s : temps) {
			            bw.write(s);
			            bw.newLine();
			        }
			    } 
				
			case "1":
				fileName = "./shuffles/UM"+args[1]+".txt";
				Scanner shuffleFile = new Scanner(new File(fileName)).useDelimiter("\n");
		        List<String> shuffle = new ArrayList<String>();
		        shuffle.hashCode();
				
		}
		
		
	}

}