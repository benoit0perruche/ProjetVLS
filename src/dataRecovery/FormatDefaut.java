package dataRecovery;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class FormatDefaut extends Format{

	/**
	 * @param path the path to the file to parse (a json)
	 * @return the data contained in the file in a double[][]
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static double[][] parserStat(String path) throws FileNotFoundException, IOException, ParseException{

		double[][] tableau = null;
		String banking,bonus = null;
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(path));
		JSONObject jsonObject = (JSONObject) obj;

		// get an array from the JSON object
		city = (String) jsonObject.get("city");
		country = (String) jsonObject.get("country");
		JSONArray stat = (JSONArray) jsonObject.get("stations");

		int length = stat.size();
		tableau = new double[length][6];

		// take the elements of the json array
		for(int i=0; i<length; i++){
			JSONObject station = (JSONObject) stat.get(i);

			tableau[i][0] = (Long) station.get("id");
			tableau[i][1] = (Double) station.get("latitude");
			tableau[i][2] = (Double) station.get("longitude");
			tableau[i][3] = (Long) station.get("slots");	

			//tableau[i][4]

			//			bonus =  java.lang.Boolean.toString((Boolean) station.get("bonus"));
			//			if (bonus != null){
			//				if (bonus.equals(true)){tableau[i][4] = 1;}
			//				//else if(bonus.equals(false)){tableau[i][4] = 0;}}
			//				else{tableau[i][4] = 0;}}
			//			else{tableau[i][4] = -1;}

			bonus = (String) station.get("bonus");
			if (bonus != null){
				if (bonus.equals("true")){tableau[i][4] = 1;}
				else if(bonus.equals("false")){tableau[i][4] = 0;}}
			else{tableau[i][4] = -1;}


			//tableau[i][5]
			//			banking = java.lang.Boolean.toString((Boolean) station.get("banking"));
			//			if (banking != null){
			//				if (banking.equals(true)){tableau[i][5] = 1;}
			//				//else if(banking.equals(false)){tableau[i][5] = 0;}}
			//				else{tableau[i][5] = 0;}}
			//			else{tableau[i][5] = -1;}

			banking = (String) station.get("banking");
			if (banking != null){
				if (banking.equals("true")){tableau[i][5] = 1;}
				else if(banking.equals("false")){tableau[i][5] = 0;}}
			else{tableau[i][5] = -1;}

		}
		return tableau;
	}
	/**
	 * @param path the path to the file to parse (a json)
	 * @return the data contained in the file in a double[][]
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static double[][] parserDyn(String path) throws FileNotFoundException, IOException, ParseException{

		double[][] tableau = null;
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(path));
		JSONObject jsonObject = (JSONObject) obj;

		// get an array from the JSON object
		city = (String) jsonObject.get("city");
		JSONArray stat = (JSONArray) jsonObject.get("stations");
		int length = stat.size();
		tableau = new double[length][6];

		// take the elements of the json array
		for(int i=0; i<length; i++){
			JSONObject station = (JSONObject) stat.get(i);

			tableau[i][0] = (Long) station.get("id");
			if (station.get("last_update") != null){
				tableau[i][1] = (Long) station.get("last_update");}
			else {tableau[i][1] = -1;}			
			tableau[i][2] = (Long) station.get("slots");
			tableau[i][3] = (Long) station.get("free_bikes");
			tableau[i][4] = (Long) station.get("empty_slots");
			if (station.get("maintenance") != null){
				tableau[i][5] = (Long) station.get("maintenance");}
			else {tableau[i][5] = -1;}
		}
		return tableau;
	}

	/** 
	 * merge two files in one
	 * @param file1 path to the first file to merge
	 * @param file2 path to the second file to merge
	 * @param file3 path to the result file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void merge(String file1, String file2, String file3) throws FileNotFoundException, IOException, ParseException{

		double[][] tab1 = null;
		double[][] tab2 = null;

		int length1;
		int length2;

		tab1 = parserDyn(file1);
		tab2 = parserDyn(file2);


		length1 = tab1.length;
		length2 = tab2.length;		

		double[][] tab = new double[length1+length2-1][6];

		for(int i=0; i<length1; i++){
			for(int j=0; j<5; j++){
				tab[i][j] = tab1[i][j];
			}		
		}
		for(int i=0; i<length2; i++){
			for(int j=0; j<5; j++){
				tab[length1-1+i][j] = tab2[i][j];
			}		
		}

		writeDyn(tab,file3);

	}	


	/**
	 * delete the unnecessary lines in a json station feed file
	 * @param path the path to the file
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void deleteDoubles(String path/*, double[][] tab*/) throws FileNotFoundException, IOException, ParseException{

		double tab[][] = null;
		double tabNoRep[][] = null;
		boolean[] stationBool = null;
		int size;
		int finalSize;
		int k;
		boolean res = true;

		//create a temp copy of the file
		File file = new File(path);
		File temp = new File(path+"temp");
		file.renameTo(temp);

		//parse the file
		tab = parserDyn(path+"temp");
		size = tab.length;

		//check if there is same last_update for same stations
		stationBool = new boolean[size];
		for (int i=0; i<size; i++){
			for (int j=0; j<i; j++){
				//test if there is a last_update field (-1 if not)
				// if there is one, test the date
				// esle if there is not, test the values of the states
				// here we lose some information because if there is a bike that leave and an other one
				// that come to the station in a minute, there is an update, but if there is no last_update field
				// we can't detect that with the station feed
				if ((tab[i][1] != -1 && tab[i][0] == tab[j][0] && tab[i][1] == tab[j][1]) ||
						(tab[i][1] == -1 && tab[i][0] == tab[j][0] && tab[i][3] == tab[j][3] && tab[i][4] == tab[j][4])){
					res = false;
				}
			}
			stationBool[i]=res;
			res = true;
		}

		//determine the size of tabNoRep
		finalSize = 0;
		for(int i=0; i<size; i++){
			if (stationBool[i]){
				finalSize++;
			}
		}

		//build tabNoRep thanks to stationBool
		tabNoRep = new double[finalSize][6];
		k = 0;
		for(int i=0; i<size; i++){
			if (stationBool[i]){
				tabNoRep[k] = tab[i];
				k++;
			}
		}
		//write tabNoRep in the file
		writeDyn(tabNoRep,path);
		temp.delete(); 
	}


	//	public void writeStat(double[][] tab){}
	//	public void writeDyn(double[][] tab){}

}
