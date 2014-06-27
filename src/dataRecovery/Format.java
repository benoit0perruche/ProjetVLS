package dataRecovery;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


public class Format {

	static String city = null;
	static String country = null;
	static String comma = ",";
	static String newLine = "\n";
	static String jsonCity = null;
	static String jsonCountry = null;
	static String jsonBegin = "{";
	static String jsonAftBegin = "\"stations\" : [";
	static String jsonBefEnd = "]";
	static String jsonEnd = "}";

	/**
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static double[][] parserStat(String path) throws FileNotFoundException, IOException, ParseException{
		return null;}
	/**
	 * @param path
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static double[][] parserDyn(String path) throws FileNotFoundException, IOException, ParseException{
		return null;}


	/**
	 * write a static file from data
	 * @param tab the data obtained after a parseStat for example
	 * @param path where to write the file
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void writeStat(double[][] tab,String path) throws IOException{
		
		int length = tab.length;
		JSONObject answ = new JSONObject();
		FileWriter fileWriter = new FileWriter(path);
		jsonCity = "\"city\" : \"" + city + "\"" + comma;

		fileWriter.write(jsonBegin+newLine+jsonCity+newLine+jsonAftBegin+newLine);

		for(int i=0; i<length; i++){
			answ.put("id", (long) tab[i][0]);
			//answ.put("name", (long) tab[i][1]);
			answ.put("latitude", tab[i][1]);
			answ.put("longitude", tab[i][2]);
			answ.put("slots", (long) tab[i][3]);

			//bonus
			if (tab[i][4]==1){answ.put("bonus", "true");}
			else if (tab[i][4]==0){answ.put("bonus", "false");}
			else {answ.put("bonus", null);}

			//banking
			if (tab[i][5]==1){answ.put("banking", "true");}
			else if (tab[i][5]==0){answ.put("banking", "false");}
			else {answ.put("banking", null);}
			
			//to put the comma between the stations
			if (i==0){fileWriter.write(answ.toJSONString());}
			else{fileWriter.write(comma+newLine+answ.toJSONString());}

		}
		fileWriter.write(newLine+jsonBefEnd+newLine+jsonEnd);
		fileWriter.close();
	}


	/**
	 * write a dynamic file from data
	 * @param tab the data obtained after a parseDyn for example
	 * @param path where to write the file
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void writeDyn(double[][] tab,String path) throws IOException{
		
		int length = tab.length;
		JSONObject ans = new JSONObject();
		FileWriter fileWriter = new FileWriter(path);
		jsonCity = "\"city\" : \"" + city + "\"" + comma;
		jsonCountry = "\"country\" : \"" + country + "\"" + comma;

		fileWriter.write(jsonBegin+newLine+jsonCity
				+newLine+jsonCountry+newLine+jsonAftBegin+newLine);
		

		for(int i=0; i<length; i++){ //le "-1" de washington
			ans.put("id", (long) tab[i][0]);
			
			//last update
			if (tab[i][1]>=0){ans.put("last_update", (long) (tab[i][1]));}
			else {ans.put("last_update", null);}
			
			ans.put("slots", (long) tab[i][2]);
			ans.put("free_bikes", (long) tab[i][3]);
			ans.put("empty_slots", (long) tab[i][4]);
			
			//maintenance
			if (tab[i][5]>=0){ans.put("maintenance", (long) (tab[i][5]));}
			else {ans.put("maintenance", null);}
			
			//to put the comma between the station states
			if (i==0){fileWriter.write(ans.toJSONString());}
			else{fileWriter.write(comma+newLine+ans.toJSONString());}

		}
		fileWriter.write(newLine+jsonBefEnd+newLine+jsonEnd);
		fileWriter.close();
	}
}
