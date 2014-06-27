package dataRecovery;
// adapted from  from Stack Overflow 
// http://stackoverflow.com/questions/1894060
// user : dksrathore  http://stackoverflow.com/users/194764/dksrathore

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Sort {
    /**
     * To sort a csv file
     * @param pathIn the file to sort
     * @param pathOut where to save the file
     * @param indiceToSort number of the column used to sort (first column : 0)
     * @throws Exception
     */
    public static void sort(String pathIn, String pathOut, int indiceToSort) throws Exception {
    	BufferedReader reader = new BufferedReader(new FileReader(pathIn));
    	Map<String, List<String>> map = new TreeMap<String, List<String>>();
    	String line = reader.readLine();//read header
    	while ((line = reader.readLine()) != null) {
    		String key = getField(line,indiceToSort);
    		List<String> l = map.get(key);
    		if (l == null) {
    			l = new LinkedList<String>();
    			map.put(key, l);
    		}
    		l.add(line);

    	}
    	reader.close();
    	FileWriter writer = new FileWriter(pathOut);
    	writer.write("Duration,Start date,Start Station,Start terminal,End date,End Station,End terminal,Bike#,Member Type\n");
    	for (List<String> list : map.values()) {
    		for (String val : list) {
    			writer.write(val);
    			writer.write("\n");
    		}
    	}
    	writer.close();
    }
   
    void write(String pathOut, List<String[]> data) throws IOException{  
 	FileWriter writer = new FileWriter(pathOut);
 	//write the first line in the csv (the title of the columns)
         writer.write("Duration,Start date,Start Station,Start terminal,End date,End Station,End terminal,Bike#,Member Type\n");
         for (String[] str : data) {
             writer.write(str[0]);
             for (int i = 1; i<str.length;i++) {
                  writer.write(", ");
                  writer.write(str[i]);
             }
             writer.write("\n");
         }
         writer.close();
     }

    public static String getField(String line, int indiceToSort) {
    	return line.split(",")[indiceToSort]; // extract the (indiceToSort+1)th column
    }
}
