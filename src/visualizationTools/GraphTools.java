package visualizationTools;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.parser.ParseException;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;

import dataRecovery.FormatDefaut;




public class GraphTools {

	/**
	 * @param idStation
	 * @param path
	 * @return a double[N][M] with the N station states, M information for each one
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static double[][] getStationDyn(long idStation,String path) throws FileNotFoundException, IOException, ParseException{

		double[][] states = FormatDefaut.parserDyn(path);
		int length = states.length;
		double[][] tab = new double[length][6];
		int j = 0;

		//fill tab with the states of the station
		for(int i=0; i<length; i++){
			if(states[i][0] == idStation){
				for(int k=0; k<6; k++){
					tab[j][k] = states[i][k];
				}
				j++;
			}
		}

		//a double[][] shorter than tab
		double[][] tabShort = new double[j][6];
		//fill tabShort with useful tab values
		for(int i=0; i<j; i++){
			for(int k=0; k<6; k++){
				tabShort[i][k] = tab[i][k];
			}
		}

		return tabShort;

	}

	/**
	 * @param idStation
	 * @param path
	 * @return the static information about this station 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static double[][] getStationStat(long idStation, String path) throws FileNotFoundException, IOException, ParseException{

		double[][] stations = FormatDefaut.parserStat(path);
		int length = stations.length;
		double[][] tab = new double[length][6];
		int j = 0;

		//fill tab with the states of the station
		for(int i=0; i<length; i++){
			if(stations[i][0] == idStation){
				for(int k=0; k<6; k++){
					tab[j][k] = stations[i][k];
				}
				j++;
			}
		}

		//a double[][] shorter than tab
		double[][] tabShort = new double[j][6];
		//fill tabShort with useful tab values
		for(int i=0; i<j; i++){
			for(int k=0; k<6; k++){
				tabShort[i][k] = tab[i][k];
			}
		}

		return tabShort;

	}
	
	

	/**
	 * @param time
	 * @param states double[][]
	 * @return the station state at that time
	 */
	public static double[] getState(long time, double[][] states){

		int j = 0; //int to remember the position in the array
		double min; //to calculate the min
		int length = states.length;
		//long to store the delta between the timestamp and the last_update field
		double[] delta = new double[length];
		//Calculate the delta for each state
		for(int i=0; i<length; i++){
			delta[i] = time - states[i][2];
		}
		//find the state at the time moment
		int i = 0;
		while(delta[i]<0){i++;}
		//if the time asked is prior to the last update date
		if (delta[i]<0){
			System.out.println("too early, choose an other station states file");
			return null;
		}

		else{
			min = delta[i];
			for(int k=i; k<length; k++)	{
				if (delta[k]<=0 && delta[k]<=min){
					j=k;
					min = delta[k];
				}
			}
			return states[j];
		}

	}


	/**
	 * @param state
	 * @return the slot number
	 */
	public static double getSlots(double[] state){

		return state[2];

	}

	/**
	 * @param state
	 * @return the number of free bikes
	 */
	public static double getFreebikes(double[] state){

		return state[3];

	}

	/**
	 * @param state
	 * @return the number of empty slots
	 */
	public static double getEmptySlots(double[] state){

		return state[4];

	}


	/**
	 * @param state
	 * @return the number of slots in maintenance
	 */
	public static double getMaintenance(double[] state){

		return state[5];

	}




	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException  {


		//DateFormat yearFormat = new SimpleDateFormat("yyyy");
		//DateFormat montFormat = new SimpleDateFormat("MM");
		//DateFormat dayFormat = new SimpleDateFormat("dd");
		//DateFormat hourFormat = new SimpleDateFormat("HH");
		//DateFormat minFormat = new SimpleDateFormat("mm");
		//
		//long timestamp = 0; 
		//
		//timestamp = System.currentTimeMillis();
		//
		//int yearInt = Integer.parseInt(yearFormat.format(timestamp));
		//int montInt = Integer.parseInt(montFormat.format(timestamp));
		//int dayInt = Integer.parseInt(dayFormat.format(timestamp));
		//int hourInt = Integer.parseInt(hourFormat.format(timestamp));
		//int minInt =  Integer.parseInt(minFormat.format(timestamp));
		//
		//System.out.println(dayInt+"/"+montInt+"/"+yearInt+" "+hourInt+":"+minInt);

//		double[][] states = getStationDyn(7006,"velib_dyna1.json");
//		double[] state = getState(System.currentTimeMillis(),states);
		double[][] stations = getStationStat(7006,"velib/velib_06-06-14_12h15_Static.json");
		//trips = dataToTrips(tripsStr);

		/*TEST*/			
				for(double[] str : stations){
					for (int j = 0; j < 6; j++){
					System.out.println(str[j]);
					}
				}


		//int length = states.length;
		//
		//for(int i=0; i<length; i++){
//		for(int k=0; k<6; k++){
//			System.out.println(state/*s[i]*/[k]);}
		//	
		//}

	}
}
