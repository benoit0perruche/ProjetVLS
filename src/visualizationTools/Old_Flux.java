package visualizationTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import dataRecovery.Parser;
import dataRecovery.Sort;
import dataRecovery.FormatDefaut;

//import java.util.ArrayList;
//import java.util.List;


public class Old_Flux {

	/**
	 * @param path
	 * @return the start date in the file
	 * @throws IOException
	 * @throws ParseException
	 */
	public static long getStartDate(String path) throws IOException, ParseException{

		long startDate = 0;   
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		Date dateIn;

		BufferedReader fichier_source = new BufferedReader(new FileReader(path));
		String chaine;
		int i = 1;

		while((chaine = fichier_source.readLine())!= null)
		{
			if(i>1){
				String[] tabChaine = chaine.split(",");
				dateIn = dateFormat.parse(tabChaine[3]);
				startDate = dateIn.getTime();
				break;
			}
			i++;
		}

		fichier_source.close(); 
		return startDate;

	}

	/**
	 * @param idStation
	 * @param path
	 * @return a long[][] of all the trips finishing with this station
	 * @throws Exception
	 */
	public static long[][] getBikesIn(long idStation, String path) throws Exception{//, int cityInt) throws Exception{

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		DateFormat hourFormat = new SimpleDateFormat("HH");
		long [][] tab;
		long hour = 1;
		long time;
		Date dateIn;
		long startDate = 0; 
		//long startDate = Long.parseLong(hourFormat.format(getStartDate(path)));

		List<String[]> tripsStr = new ArrayList<String[]>();

		//create a temp file, sorted by end station
		Sort.sort(path,
				path+"bikesIn",
				4);

		//parse the sorted file
		tripsStr = Parser.parse(path+"bikesIn");

		//complete stationStr with the bikes arriving
		List<String[]> stationStr = new ArrayList<String[]>();

		for (int i = 1; i < (tripsStr.size()-1); i++){

			if ((tripsStr.get(i)[4]).equals(String.valueOf(idStation))){
				stationStr.add(tripsStr.get(i));
			}
		}
		int size = stationStr.size();

		long[][] event = new long[size][2];
		int y=0;

		for(String[] str : stationStr) {

			dateIn = dateFormat.parse(str[3]);
			time = dateIn.getTime();
			event[y][0] = 1;
			event[y][1] = Long.parseLong(hourFormat.format(time));
			y++;

		}

		long[][] tabPeriod = new long [size] [2];
		int flux = 0; // the number of intervals with something
		int done = 0; // the number of event treated
		int l = 0; // the intervals
		int m = 0; // the tab to fill
		int nbBikes = 0; //the number of bikes for an interval

		while(done<size){
			for(int k=0; k<size; k++){
				if(event[k][1] == (startDate+l*hour)%24){
					nbBikes++;
				}
				//				System.out.println(" nbBikes : " + nbBikes);
			}
			if(nbBikes != 0){
				tabPeriod[m][0]= nbBikes;
				tabPeriod[m][1]= (startDate+l*hour)%24;
				m++;
				done = done + nbBikes;
				flux = flux + 1;
				nbBikes = 0;
			}
			l++;
		}

		tab = new long [flux][2];
		for(int j=0; j<flux; j++){
			tab[j][0] = tabPeriod[j][0];
			//			System.out.println(" tab["+j+"][0] : " + tab[j][0]);
			tab[j][1] = tabPeriod[j][1];
			//			System.out.println(" tab["+j+"][1] : " + tab[j][1]);
		}


		//delete the temp file
		Parser.deleteFile(path+"bikesIn");

		//		System.out.println("Fin getBikesIn Flux2");
		return tab;
	}
	/**
	 * @param idStation
	 * @param path
	 * @return a long[][] of all the trips starting with this station
	 * @throws Exception
	 */
	public static long[][] getBikesOut(long idStation, String path) throws Exception{

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		DateFormat hourFormat = new SimpleDateFormat("HH");
		long [][] tab;
		long hour = 1;
		long time;
		Date dateOut;
		long startDate = 0; // Long.parseLong(hourFormat.format(getStartDate(path)));

		List<String[]> tripsStr = new ArrayList<String[]>();

		//create a temp file, sorted by start station
		Sort.sort(path,
				path+"bikesOut",
				2);

		//parse the sorted file
		tripsStr = Parser.parse(path+"bikesOut");

		//complete stationStr with the bikes arrivOutg
		List<String[]> stationStr = new ArrayList<String[]>();
		for (int i = 1; i < (tripsStr.size()-1); i++){  					// WHY -1 ?
			if ((tripsStr.get(i)[2]).equals(String.valueOf(idStation))){
				stationStr.add(tripsStr.get(i));
			}
		}
		int size = stationStr.size();
		//		System.out.println(" size : " + size);

		long[][] event = new long[size][2];
		int y=0;
		for(String[] str : stationStr) {

			dateOut = dateFormat.parse(str[1]);       					// start Date for bikes Out
			time = dateOut.getTime();
			event[y][0] = 1;
			event[y][1] = Long.parseLong(hourFormat.format(time));
			y++;
		}


		long[][] tabPeriod = new long [size] [2];
		int flux = 0; // the number of intervals with somethOutg
		int done = 0; // the number of event treated
		int l = 0; // the intervals
		int m = 0; // the tab to fill
		int nbBikes = 0; //the number of bikes for an interval

		while(done<size){
			for(int k=0; k<size; k++){
				if(event[k][1] == (startDate+l*hour)%24){
					nbBikes++;
				}
				//				System.out.println(" nbBikes : " + nbBikes);
			}
			if(nbBikes != 0){
				tabPeriod[m][0]= nbBikes;
				tabPeriod[m][1]= (startDate+l*hour)%24;
				m++;
				done = done + nbBikes;
				flux = flux + 1;
				nbBikes = 0;
			}
			l++;
		}
		tab = new long [flux][2];
		for(int j=0; j<flux; j++){
			tab[j][0] = tabPeriod[j][0];
			tab[j][1] = tabPeriod[j][1];
		}

		//delete the temp file
		Parser.deleteFile(path+"bikesOut");

		//		System.out.println("Fin getBikesOut Flux2");
		return tab;
	}




	/**
	 * @param trajets
	 * @return
	 */
	public static long[] trajetStation (long[][] trajets){ 
		long[] tab = new long[24]; 
		int hourTrip = 0; 
		for (int l = 0; l < trajets.length; l++){ 
			hourTrip = (int) trajets[l][1]; 
			tab[hourTrip] += trajets[l][0]; 
		} 
		return tab; 
	}

	/**
	 * @param pathTrips
	 * @param pathStatic
	 * @param idStation
	 * @param nbDays
	 * @return
	 * @throws Exception
	 */
	public static double[][] FluxStation(String pathTrips, String pathStatic, int idStation, int nbDays)throws Exception{
		double[][] fluxStat = new double[24][2];
		double[][] station = GraphTools.getStationStat(idStation, pathStatic);
		double slots = station[0][3];
		long[] BikesIn = trajetStation(getBikesIn(idStation, pathTrips));
		long[] BikesOut = trajetStation(getBikesOut(idStation, pathTrips));

		for (int l = 0; l < 24; l++){ 
			fluxStat[l][0] = (double) BikesIn[l] /(double) slots / (double) nbDays * 100.;
			fluxStat[l][1] = (double) BikesOut[l] /(double) slots / (double) nbDays * 100.;

		}

		return fluxStat;
	}


	/**
	 * @param pathTrips
	 * @param pathStatic
	 * @param idStation
	 * @param nbDays
	 * @return
	 * @throws Exception
	 */
	public static double[] meanFluxStation(String pathTrips, String pathStatic, int idStation, int nbDays) throws Exception{
		double[] meanFlux = new double[24];

		double[][] station = GraphTools.getStationStat(idStation, pathStatic);
		double slots = station[0][3];
		System.out.println("Test avant getBikesIn");
		long[] BikesIn = trajetStation(getBikesIn(idStation, pathTrips));
		System.out.println("Test apres getBikesIn et avant getBikesOut");
		long[] BikesOut = trajetStation(getBikesOut(idStation, pathTrips));
		System.out.println("Test apres getBikesOut");

		for (int l = 0; l < 24; l++){ 
			meanFlux[l] = ((double) (BikesIn[l] - BikesOut[l])/(double) slots)/ (double) nbDays * 100.;
			//			System.out.println("meanFlux["+l+"] : " + meanFlux[l]);

		}

		return meanFlux;
	}


	/**
	 * @param pathTrips
	 * @param pathStatic
	 * @param nbDays
	 * @return
	 * @throws Exception
	 */
	public static double[] meanFluxStations(String pathTrips, String pathStatic, int nbDays) throws Exception{
		double[] meanFlux = new double[24];
		double[][] stations = FormatDefaut.parserStat(pathStatic);
		double slots; 
		int idStation; 
		for(int i = 0; i < stations.length; i++){
			idStation = (int) stations[i][0];
			slots = stations[i][3];
			long[] BikesIn = trajetStation(getBikesIn(idStation, pathTrips));
			long[] BikesOut = trajetStation(getBikesOut(idStation, pathTrips));
			for (int l = 0; l < 24; l++){ 
				meanFlux[l] = (meanFlux[l]*((double) (i+1.))
						+ ((double) (BikesIn [l] - BikesOut[l])/(double) slots)/ (double) nbDays * 100.)
						/((double)(i+2.));
			}
		}
		return meanFlux;

	}
	public static void main(String[] arguments) throws Exception {

		//        String RESOURCES_PATH = "/home/ricky/VLS/Washington/";
		//        String TRIPS_FILE_NAME = "Washington_Trips_7-3-2014(copie)";




		double[] tab = meanFluxStations("/home/ricky/VLS/Washington/2014-1st-quarter.csv",
				"/home/ricky/VLS/Washington/capital-bikeshare_12-06-14_04h41_Static.json",
				90);

		//DEBUG


		for(int i=0;i<10;i++){
			System.out.println("---|---------------------");
			System.out.print("0" + i + " | ");
			System.out.println(tab[i]);
		}
		for(int i=10;i<24;i++){
			System.out.println("---|---------------------");
			System.out.print(i + " | ");
			System.out.println(tab[i]);


		}
	}
}

