package dataAnalysis;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.parser.ParseException;

import visualizationTools.Intervals;

public class RExport {
	
	static String RESOURCES_PATH = "";
	static String csv_File = "trips(copie).csv";
	static String config_File = "static.json";
	
	void writeCSV(int nbIntervals) throws IOException, ParseException{
		Intervals.getDistIntervalsDist(RESOURCES_PATH+csv_File, nbIntervals);
		double[] yop = Intervals.distanceStat(RESOURCES_PATH+config_File);
		double[] intervals = Intervals.deletezeroIntervals(Intervals.getDistIntervals());
		int[] a = Intervals.distanceCount(yop,intervals);
		int[] b = Intervals.tripCount(RESOURCES_PATH+csv_File,intervals);

		FileWriter writer = new FileWriter("Rdata");
		writer.write("X,Y\n");   	

		for(int i=0; i<intervals.length-1;i++){
			System.out.println(a[i] + " intervalle " + i);
			if ((b[i]/a[i])>0){

				writer.write(Math.log(((intervals[i+1]-intervals[i])/2)+intervals[i]+1)+","+Math.log(b[i]/a[i])+"\n");

			}
		}		
		writer.close();
	}

}