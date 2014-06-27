package dataRecovery;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// Cities like Washington, Boston, with latestUpdateTime
public class FormatPBSC2 extends Format{

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

			//tableau[i][0] = (Long) extra.get("uid");
			//to remove the '"' at the beginning and at the end of the string
			String id = (String) extra.get("terminalName");
			int l = id.length();
			id.substring(1,l);
			l = id.length()-1;
			id.substring(0,l);

			tableau[i][0] = Long.parseLong(id);
			//tableau[i][1] = (Double) station.get("name");
			tableau[i][1] = (Double) station.get("latitude");
			tableau[i][2] = (Double) station.get("longitude");
			//here the number of slots is an estimation.
			tableau[i][3] = (Long) station.get("empty_slots")+ (Long) station.get("free_bikes"); 
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
	//TODO ?? todo what ?
		for(int i=0; i<length; i++){
			JSONObject station = (JSONObject) stat.get(i);
			JSONObject extra = (JSONObject) station.get("extra");

			//tableau[i][0] = (Long) extra.get("uid");
			//to remove the '"' at the beginning and at the end of the string
			String id = (String) extra.get("terminalName");
			int l = id.length();
			id.substring(1,l);
			l = id.length()-1;
			id.substring(0,l);

			tableau[i][0] = Long.parseLong(id);

			//to remove the '"' at the beginning and at the end of the string
			String lastup = (String) extra.get("latestUpdateTime");
			if (lastup.length()>2){ //problem with "" (no date only the string """")
			l = lastup.length();
			lastup.substring(1,l);
			l = lastup.length()-1;
			lastup.substring(0,l);
			tableau[i][1] = Long.parseLong(lastup);}
			else{tableau[i][1] = -1;}


			tableau[i][3] = (Long) station.get("free_bikes");
			tableau[i][4] = (Long) station.get("empty_slots");
			//slots
			tableau[i][2] = tableau[i][3]+tableau[i][4]; //here the number of slots is an estimation.
			//maintenance
			tableau[i][5] = -1; //we have no indication on maintenance, that is why the number of slot is an estimation.



		}
		return tableau;
	}

	//	public void writeStat(double[][] tab){}
	//	public void writeDyn(double[][] tab){}

}
