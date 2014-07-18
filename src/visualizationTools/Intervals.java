package visualizationTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Intervals {

	protected static String[] startTimeIntervals;
	protected static String[] endTimeIntervals;
	protected static double[] distIntervals;

	public static String[] getStartTimeIntervals(){
		return startTimeIntervals;
	}	
	public static String[] getEndTimeIntervals(){
		return endTimeIntervals;
	}	
	public static double[] getDistIntervals(){
		return distIntervals;
	}

	/**
	 * @param pathTrips light trip history file .csv (no station name) from Washington without distances
	 * @param nbIntervals the number of intervals wanted
	 * @throws Exception 
	 */
	public static void getDistIntervalsNoDist(String pathTrips, String pathStatic, int nbIntervals) throws Exception {
		dataRecovery.FormatCsv.addDistance(pathTrips, pathStatic);
		getDistIntervalsDist(pathTrips, nbIntervals);
	}



	/**
	 * Define bounds of the intervals
	 * @param pathTrips trip history file .csv with distances
	 * @param nbIntervals
	 * @throws IOException 
	 */
	public static void getDistIntervalsDist(String pathTrips, int nbIntervals) throws IOException {
		distIntervals = new double[nbIntervals+1];

		List<String[]> distStr = new ArrayList<String[]>();

		String chaine = "";

		BufferedReader bufferedReader = new BufferedReader(new FileReader(pathTrips));
		int nbLines = -1;
		while((chaine=bufferedReader.readLine()) != null)
		{
			if(nbLines >= 0)
			{
				String[] tabChaine = chaine.split(",");
				distStr.add(tabChaine);
			}
			nbLines++;
		}
		bufferedReader.close(); 

		double[] dist = new double[nbLines];
		int line = 0;
		for(String[] str : distStr){
			dist[line] = Double.parseDouble(str[7]);
			line++;
		}

		Arrays.sort(dist);
		for(int i=0; i<nbIntervals; i++){
			distIntervals[i]=dist[(dist.length/nbIntervals)*i];
		}
		distIntervals[nbIntervals]=dist[dist.length-1];
	}

	/**
	 * modify the ditance column in the trip file remplacing them by the number of the interval
	 * @param pathTrips trip history file .csv with distances
	 * @param Intervals bounds of the intervals
	 * @throws IOException 
	 */
	public static void changeDistIntervals(String pathTrips, double[] Intervals) throws IOException {
		int nbIntervals = Intervals.length - 1;
		FileWriter writer = null;
		File temp = null;

		//create a temp copy of the file
		File file = new File(pathTrips);
		temp = new File(pathTrips+"temp");
		file.renameTo(temp);	
		writer = new FileWriter(pathTrips);
		writer.write("Duration,Start date,Start terminal,End date,End terminal,Bike#,Member Type,Distance\n");

		String chaine = "";

		BufferedReader bufferedReader = new BufferedReader(new FileReader(pathTrips+"temp"));

		int i = 0;

		while((chaine = bufferedReader.readLine()) != null){

			if(i>0){
				String[] tabChaine = chaine.split(",");
				for (int k = 0; k < tabChaine.length-1; k++){
					writer.write(tabChaine[k] + ",");
				}
				int j = 0;
				while(j < nbIntervals-1){
					if (Double.parseDouble(tabChaine[tabChaine.length-1]) >= Intervals[j] &&
							Double.parseDouble(tabChaine[tabChaine.length-1]) <= Intervals[j+1]){
						break;
					}
					j++;		
				}
				writer.write(j + "\n");
			}
			i++;
		}
		bufferedReader.close();  
		writer.close();
		temp.delete();
	}

	/**
	 * Define bounds of the intervals
	 * @param pathTrips trip history file .csv
	 * @param nbIntervals
	 * @throws IOException
	 */
	public static void getTimeIntervalsStart(String pathTrips, int nbIntervals) throws IOException {
		startTimeIntervals = new String[nbIntervals+1];

		List<String[]> timeStr = new ArrayList<String[]>();

		String chaine = "";

		BufferedReader bufferedReader = new BufferedReader(new FileReader(pathTrips));
		int nbLines = -1;
		while((chaine=bufferedReader.readLine()) != null)
		{
			if(nbLines >= 0)
			{
				String[] tabChaine = chaine.split(",");
				timeStr.add(tabChaine);
			}
			nbLines++;
		}
		bufferedReader.close(); 

		String[] time = new String[nbLines];
		int line = 0;
		for(String[] str : timeStr){
			String[] date = str[1].split(" ");
			if(date[1].length()<5){
				date[1]="0"+date[1];
			}
			time[line] = date[1];
			line++;
		}

		Arrays.sort(time);

		for(int i=0; i<nbIntervals; i++){
			startTimeIntervals[i]=time[(time.length/nbIntervals)*i];
		}
		startTimeIntervals[nbIntervals]=time[time.length-1];
	}


	/**
	 * Replace the time of departure by the number of the intervals in which the time is
	 * @param pathTrips trip history file .csv
	 * @param Intervals bounds of the intervals
	 * @throws IOException
	 */
	public static void changeTimeIntervalsStart(String pathTrips, String[] Intervals) throws IOException {
		int nbIntervals = Intervals.length - 1;

		FileWriter writer = null; 
		File temp = null; 

		//create a temp copy of the file
		File file = new File(pathTrips);
		temp = new File(pathTrips+"temp");
		file.renameTo(temp);	
		writer = new FileWriter(pathTrips);
		writer.write("Duration,Start date,Start terminal,End date,End terminal,Bike#,Member Type,Distance\n");

		String chaine = "";

		BufferedReader bufferedReader = new BufferedReader(new FileReader(pathTrips+"temp"));

		int i = 0;

		while((chaine = bufferedReader.readLine()) != null){

			if(i>0){
				String[] tabChaine = chaine.split(",");
				writer.write(tabChaine[0] + ",");

				String[] date = tabChaine[1].split(" ");
				if(date[1].length()<5){
					date[1]="0"+date[1];
				}
				int j = 0;
				while(j < nbIntervals-1){
					//ne garder que l'heure !!!!!
					if ((date[1]).compareTo(Intervals[j])>=0 &&
							(date[1]).compareTo(Intervals[j+1])<=0){
						break;
					}
					j++;		
				}
				writer.write(j+"");
				for (int k = 2; k < tabChaine.length; k++){
					writer.write("," + tabChaine[k]);
				}
				writer.write("\n");
			}
			i++;
		}
		bufferedReader.close();  
		writer.close();
		temp.delete();
	}


	/**
	 * Define bounds of the intervals
	 * @param pathTrips trip history file .csv
	 * @param nbIntervals
	 * @throws IOException
	 */
	public static void getTimeIntervalsEnd(String pathTrips, int nbIntervals) throws IOException {
		endTimeIntervals = new String[nbIntervals+1];

		List<String[]> timeStr = new ArrayList<String[]>();

		String chaine = "";

		BufferedReader bufferedReader = new BufferedReader(new FileReader(pathTrips));
		int nbLines = -1;
		while((chaine=bufferedReader.readLine()) != null)
		{
			if(nbLines >= 0)
			{
				String[] tabChaine = chaine.split(",");
				timeStr.add(tabChaine);
			}
			nbLines++;
		}
		bufferedReader.close(); 

		String[] time = new String[nbLines];
		int line = 0;
		for(String[] str : timeStr){
			String[] date = str[3].split(" ");
			if(date[1].length()<5){
				date[1]="0"+date[1];
			}
			time[line] = date[1];
			line++;
		}

		Arrays.sort(time);

		for(int i=0; i<nbIntervals; i++){
			endTimeIntervals[i]=time[(time.length/nbIntervals)*i];
		}
		endTimeIntervals[nbIntervals]=time[time.length-1];
	}


	/**
	 * Replace the time of departure by the number of the intervals in which the time is
	 * @param pathTrips trip history file .csv
	 * @param Intervals bounds of the intervals
	 * @throws IOException
	 */
	public static void changeTimeIntervalsEnd(String pathTrips, String[] Intervals) throws IOException {

		int nbIntervals = Intervals.length - 1;
		FileWriter writer = null;
		File temp = null;

		//create a temp copy of the file
		File file = new File(pathTrips);
		temp = new File(pathTrips+"temp");
		file.renameTo(temp);	
		writer = new FileWriter(pathTrips);
		writer.write("Duration,Start date,Start terminal,End date,End terminal,Bike#,Member Type,Distance\n");

		String chaine = "";

		BufferedReader bufferedReader = new BufferedReader(new FileReader(pathTrips+"temp"));

		int i = 0;

		while((chaine = bufferedReader.readLine()) != null){

			if(i>0){
				String[] tabChaine = chaine.split(",");
				writer.write(tabChaine[0]);
				for (int k = 1; k < 3; k++){
					writer.write("," + tabChaine[k]);
				}
				String[] date = tabChaine[3].split(" ");
				if(date[1].length()<5){
					date[1]="0"+date[1];
				}
				int j = 0;
				while(j < nbIntervals-1){
					if ((date[1]).compareTo(Intervals[j])>=0 &&
							(date[1]).compareTo(Intervals[j+1])<=0){
						break;
					}
					j++;		
				}
				writer.write("," + j);
				for (int k = 4; k < tabChaine.length; k++){
					writer.write("," + tabChaine[k]);
				}
				writer.write("\n");
			}
			i++;
		}
		bufferedReader.close();  
		writer.close();
		temp.delete();
	}


	public static void putIndex(String pathTrips, String pathStatic) throws IOException {
		FileWriter writer = null; 
		File temp = null; 

		int[] index = dataAnalysis.MatriceTrajets.index(pathStatic);

		String chaine = "";

		//create a temp copy of the file
		File file = new File(pathTrips);
		temp = new File(pathTrips+"temp");
		file.renameTo(temp);	
		//write header
		writer = new FileWriter(pathTrips);
		writer.write("Duration,Start date,Start terminal,End date,End terminal,Bike#,Member Type,Distance\n");

		chaine = "";

		BufferedReader bufferedReader = new BufferedReader(new FileReader(pathTrips+"temp"));

		int i = 0;

		while((chaine = bufferedReader.readLine()) != null){

			if(i>0){
				String[] tabChaine = chaine.split(",");
				int j = 0;
				boolean foundS = false;
				boolean foundE = false;
				while(j<index.length){

					if(!foundS && Integer.parseInt(tabChaine[2]) == index[j]){
						tabChaine[2] = ""+j;
						foundS = true;
					}
					if(!foundE && Integer.parseInt(tabChaine[4]) == index[j]){
						tabChaine[4] = ""+j;
						foundE = true;
					}
					j++;
					if(foundE && foundS){break;}
				}

				writer.write(tabChaine[0]);
				for (int k = 1; k < tabChaine.length; k++){
					writer.write("," + tabChaine[k]);
				}
				writer.write("\n");
			}
			i++;
		}
		bufferedReader.close();  
		writer.close();
		temp.delete();
	}

	public static double[] timeIntervalsToDouble(String[] timeIntervals) throws ParseException{
		double[] timeIntervalsDouble = new double[timeIntervals.length];
		double hours;
		double minutes;
		for (int i =0; i<timeIntervals.length; i++){
			String[] tabChaine = timeIntervals[i].split(":");
			hours = Double.parseDouble(tabChaine[0]);
			minutes = Double.parseDouble(tabChaine[1])/60.;
			timeIntervalsDouble[i] = hours + minutes;
		}
		return timeIntervalsDouble;
	}


	public static double[] distanceStat(String pathStatic) throws FileNotFoundException, IOException, org.json.simple.parser.ParseException{
		double[][] stat = dataRecovery.FormatDefaut.parserStat(pathStatic);
		double[] distanceBig = new double[stat.length*(stat.length-1)/2+stat.length]; //2 parmis nbStations + nbStations
		int[] indexStation = new int[stat.length];
		int k = 0;
		for (int i=0;i<stat.length;i++){
			indexStation[i] = (int) stat[i][0];
			for (int j=0;j<=i;j++){
				distanceBig[k] = dataRecovery.FormatCsv.distanceVolOiseau(stat[i][1],stat[i][2],stat[j][1],stat[j][2]);
				k++;
			}
		}
		k = 0;
		for(int i=0;i<distanceBig.length;i++){
			if (distanceBig[i] != 0){
				k++;
			}
		}

		double[] distance = new double[k];
		k = 0;
		for(int i=0;i<distanceBig.length;i++){
			if (distanceBig[i] != 0){
				distance[k] = distanceBig[i];
				k++;
			}
		}
		return distance;
	}

	public static int[] distanceCount(double[] distance,double[] intervals){
		int[] count = new int[intervals.length - 1];
		int i;
		int j = 0;
		while(j < distance.length){
			i = 0;
			while(i < intervals.length-1){
				//				if(count[i]>=distance.length/intervals.length){
				//					break;
				//					}
				if (distance[j] >= intervals[i] && distance[j] < intervals[i+1]){ //problems with intervals 0-0
					count[i]++;
					break;
				}
				i++;
			}
			j++;
		}
		return count;
	}

	/**
	 * @param pathCSV
	 * @param boite the distance of each interval
	 * @return the number of trajects for each distance interval
	 * @throws IOException
	 */
	public static int[] trajects (String pathCSV, int boite) throws IOException{
		List<String[]> trips = dataRecovery.Parser.parse(pathCSV);
		int distMax = maxDist(trips);
		int taille = distMax/boite + 1;
		int[] res = new int[taille];
		for (String[] trip : trips) {
			res[(int) (Double.parseDouble(trip[7])/(boite))]++;
		}
		return res;
	}

	public static int[] tripCount(String pathCSV, double[] intervals) throws IOException{

		List<String[]> trips = dataRecovery.Parser.parse(pathCSV);
		int[] count = new int[intervals.length - 1];
		for (String[] trip : trips) {
			for (int i = 0; i<intervals.length-1; i++){
				if (Integer.parseInt(trip[7])>intervals[i] && Integer.parseInt(trip[7])<=intervals[i+1]){
					count[i]++;
				}
			}
		}
		return count;
	}

	/**
	 * @param trips
	 * @return the distance max from the trajects file
	 */
	public static int maxDist (List<String[]> trips){
		int res = 0;
		for (String[] trip : trips) {
			res = Math.max(res, (int) (Double.parseDouble(trip[7])));
		}
		return res;
	}

	public static double[] deletezeroIntervals(double[] intervals){
		double[] intervalsNew;
		int k=0;
		for(int i = 0; i<intervals.length; i++){
			if (intervals[i] != 0){
				k++;
			}
		}
		intervalsNew = new double[k];
		k = 0;
		for(int i = 0; i<intervals.length; i++){
			if (intervals[i] != 0){
				intervalsNew[k] = intervals[i];
				k++;
			}
		}
		return intervalsNew;
	}

	public static void main(String[] arguments) throws Exception {
		//		//getTimeIntervalsEnd("/home/ricky/VLS/Washington/2014-1st-quarterclusterdisttime(copie).csv",24);
		//		dataRecovery.FormatCsv.formaterFichierCsv("/home/ricky/VLS/Washington/2014-1st-quarterclusterdisttimeboth(copie).csv", "aaa.csv");

		getDistIntervalsDist("trips(copie).csv", 274);
		double[] yop = distanceStat("static.json");
		int[] a = distanceCount(yop,deletezeroIntervals(distIntervals));
		int[] b = tripCount("trips(copie).csv",deletezeroIntervals(distIntervals));
		for(int i=0; i<deletezeroIntervals(distIntervals).length-1;i++){
						double c = b[i]/a[i] ;
						System.out.println(c);
//			if(a[i]==0){System.out.println("ZERO");}
//			System.out.println(a[i]);
			
		}




	}
}
