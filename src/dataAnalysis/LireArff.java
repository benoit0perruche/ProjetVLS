package dataAnalysis;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import weka.core.Instances;
import weka.core.converters.ArffLoader.ArffReader;

public class LireArff {

	/**
	 * @param pathIn the path to the arff file
	 * @param nbParam the number of column of the matrix used to make the clusters
	 * @return 
	 */
	public static int[] lireArff (String pathIn, int nbData) {

		BufferedReader reader;
		ArffReader arff = null;
		try {
			reader = new BufferedReader(
					new FileReader(pathIn));
			arff = new ArffReader(reader);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Instances data = arff.getData();

		int taille = data.numInstances();
		int[] clusters = new int[taille];

		// setting class attribute
		data.setClassIndex(data.numAttributes() - 1);

		for (int i=0; i<taille; i++){
			clusters[i] = (int) data.instance(i).value(nbData + 1);
		}

		return clusters;
	}

	public static int[][] stationsCluster(int[] clusters, String staticPath) throws FileNotFoundException, IOException, ParseException{
		int[][] stationsCluster = new int[clusters.length][2] ;

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(staticPath));
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray stat = (JSONArray) jsonObject.get("stations");
		int length = stat.size();
		if (length != clusters.length){System.out.println("wrong size of cluster compared to static file");}
		for(int i=0; i<length; i++){
			JSONObject station = (JSONObject) stat.get(i);
			stationsCluster[i][0] = ((Long) station.get("id")).intValue();
			stationsCluster[i][1] = (int) clusters[i];
		}


		return stationsCluster;
	}

	public static double[][] StringtoDouble(List<String[]> stringTab){
		double[][] doubletab = new double[stringTab.size()][stringTab.get(0).length];
		
		for(int i=0; i<stringTab.size(); i++){
			for(int j=0; j<stringTab.get(0).length; j++){
				doubletab[i][j] = Double.parseDouble(stringTab.get(i)[j]);
			}
		}
		return doubletab;
	}


	public static void main(String[] args) throws IOException, ParseException {

		String ARFF_FILE_PATH = "";
		String FILE_NAME = "save.csv";


		int[] test = lireArff(ARFF_FILE_PATH+FILE_NAME,24);
		int[][] stationsCluster = stationsCluster(test,"static.json");

		for (int i = 0; i < stationsCluster.length; i++){
			System.out.println(stationsCluster[i][0] + " | " + stationsCluster[i][1]);
		}
	}



}
