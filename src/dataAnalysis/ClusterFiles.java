package dataAnalysis;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

public class ClusterFiles {

	private static final String ARFF_FILE_PATH = "/home/benoit/Documents/Polytech4A/Stage/Weka/Washington";
	private static final String FILE_NAME_IN = "/clustersIn.arff";
	private static final String FILE_NAME_OUT = "/clustersOut.arff";
	static String RESOURCES_PATH = "/home/benoit/Documents/Polytech4A/Stage/Donnees/Washington";
	static String demand_File = "/demand.csv";
	static String csv_File = "/week.csv";

	/**
	 * @param demandeMat
	 * @param pathInArff
	 * @param pathOut
	 * @throws IOException 
	 */
	public static void createCLustersFiles (double[][][][] demandMat, String pathInArff, 
			String pathOutArff, String pathOut) throws IOException{ 
		/*
		 * - open arff (get the clusters)
		 * - for each cluster get its stations ? 
		 * 	ou écrire au fur et à mesure en fonction du cluster de la station
		 * - get the demandeMat[File'clusterOut][hour i][clusterIn][0]
		 * - write the config file createConfig
		 * 
		 */
		FileWriter writer = new FileWriter(pathOut);
		@SuppressWarnings("unused")
		int[][] clustersStat = StationType.clusterStats(LireArff.lireArff(pathInArff,24),
				LireArff.lireArff(pathOutArff,24));
		writer.close();
	}

	public static void createDemandFile (double[][][][] demandMat, String path) throws IOException{
		FileWriter writer = new FileWriter(path);
		writer.write("ClusterOut,ClusterIn,DayPart,Demand\n");
		for(int i = 0; i< demandMat.length; i++){
			for (int j = 0; j<demandMat[0].length; j++){
				for (int k = 0; k<demandMat[0][0].length; k++){
					writer.write(i +"," + j + "," + k + "," + demandMat[i][j][k][0] + "\n");
				}
			}
		}
		writer.close();
	}

	/**
	 * @param clusterStats
	 */
	public static void createConfig (int[][] clusterStats, String pathConfig){
		/*
		 * - get the config file, put the cluster number for each station
		 */

	}

	public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException{
		double[][][][] demandMat = EstimationDemande.matriceDemande(RESOURCES_PATH+csv_File,ARFF_FILE_PATH+FILE_NAME_OUT,
				ARFF_FILE_PATH+FILE_NAME_IN, 24, 21);
		createDemandFile(demandMat,RESOURCES_PATH+demand_File);
	}
}
