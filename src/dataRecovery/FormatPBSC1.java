package dataRecovery;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Cities like New York without latestUpdateTime or update_time
public class FormatPBSC1 extends Format{

	/**
	 * @param path the path to the file to parse (a json)
	 * @return the data contained in the file in a double[][]
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static double[][] parserStat(String path) throws FileNotFoundException, IOException, ParseException{

		double[][] tableau = null;
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(path));
		JSONObject jsonObject = (JSONObject) obj;

		// get an array from the JSON object
		JSONObject net= (JSONObject) jsonObject.get("network");
		JSONObject location = (JSONObject) net.get("location");
		city = (String) location.get("city");
		country = (String) location.get("country");
		JSONArray stat = (JSONArray) net.get("stations");

		int length = stat.size();
		tableau = new double[length][6];

		// take the elements of the json array
		for(int i=0; i<length; i++){
			JSONObject station = (JSONObject) stat.get(i);
			JSONObject extra = (JSONObject) station.get("extra");


			tableau[i][0] = (Long) extra.get("uid");
//			tableau[i][1] = (Double) station.get("name");
			tableau[i][1] = (Double) station.get("latitude");
			tableau[i][2] = (Double) station.get("longitude");
			//here the number of slots is an estimation.
			tableau[i][3] = (Long) extra.get("totalDocks");
			tableau[i][4] = -1; //bonus
			tableau[i][5] = -1; //banking
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
		JSONObject net= (JSONObject) jsonObject.get("network");
		JSONObject location = (JSONObject) net.get("location");
		city = (String) location.get("city");
		JSONArray stat = (JSONArray) net.get("stations");
		int length = stat.size();
		tableau = new double[length][6];

		// take the elements of the json array
		for(int i=0; i<length; i++){
			JSONObject station = (JSONObject) stat.get(i);
			JSONObject extra = (JSONObject) station.get("extra");


			tableau[i][0] = (Long) extra.get("uid");
			tableau[i][1] = -1;
			tableau[i][2] = (Long) extra.get("totalDocks");
			tableau[i][3] = (Long) station.get("free_bikes");
			tableau[i][4] = (Long) station.get("empty_slots");			
			//maintenance
			//tableau[i][5] = -1; //we have no indication on maintenance 
			//but we guess it is a simple calculus : 
			//total number of slots - empty_slots - number of available bikes
			tableau[i][5] = tableau[i][2]-tableau[i][3]-tableau[i][4];



		}
		return tableau;
	}

	//	public void writeStat(double[][] tab){}
	//	public void writeDyn(double[][] tab){}

}
