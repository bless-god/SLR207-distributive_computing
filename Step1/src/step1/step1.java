package step1;

import java.awt.List;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class step1 {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File file = new File("D:\\Telecom 1e anne\\Telecom Period 3\\SLR207\\Project\\input.txt");
		Scanner readfile = new Scanner(file);
		String data = new String("");
		while ( readfile.hasNextLine() ) {
			String line = readfile.nextLine();
			data += line + " ";
		}
		String splitedline[] = data.split(" ");
		HashMap map = new HashMap();
		for(String part: splitedline) {
			if (map.containsKey(part)) {
				map.put(part, (int)map.get(part) + 1);
			}
			else {
				map.put(part, 1);
			}				
		}
		System.out.print(map);
	}

}
