package dataAnalysis;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;

import visualizationTools.CsvWeekDay;

public class StationType {

	private static final String ARFF_FILE_PATH = "/home/benoit/Documents/Polytech4A/Stage/Weka/Washington";
	private static final String FILE_NAME_IN = "/bikesInWeek.arff";
	private static final String FILE_NAME_OUT = "/bikesOutWeek.arff";
	static String RESOURCES_PATH = "/home/benoit/Documents/Polytech4A/Stage/Donnees/Washington";
	static String csv_File = "/Trajets/2014-Washington-January.csv";
	static String static_File = "/config_Washington.json";

	static DecimalFormat numberFormat = new DecimalFormat("###.##");

	/**
	 * @param clustersIn
	 * @param clustersOut
	 * @return an array with for each station its clusterIn and clusterOut
	 */
	public static int[][] clusterStats (int[] clustersIn, int[] clustersOut){
		int taille = Math.max(clustersIn.length, clustersOut.length);
		int[][] res = new int[taille][2];
		for (int i=0; i<taille;i++){
			res[i][0] = clustersIn[i];
			res[i][1] = clustersOut[i];
		}
		return res;
	}

	/**
	 * Return an int[] with the number of stations in each cluster
	 * 
	 * @param nbClusters
	 * @param clusters
	 * @return int[nbCLusters]
	 */
	public static int[] nbStatCluster (int nbClusters, int[] clusters){
		int[] res = new int[nbClusters];
		for(int i = 0; i<clusters.length; i++){
			res[clusters[i]] ++;
		}
		return res;
	}

	/**
	 * @param clusterStat
	 * @return int[] clusters out
	 */
	public static int[] getClustersOut (int[][] clusterStat){
		int[] res = new int[clusterStat.length];
		for (int i = 0; i<clusterStat.length; i++){
			res[i] = clusterStat[i][1];
		}
		return res;
	}
	
	/**
	 * @param clusterStat
	 * @return int[] clusters in
	 */
	public static int[] getClustersIn (int[][] clusterStat){
		int[] res = new int[clusterStat.length];
		for (int i = 0; i<clusterStat.length; i++){
			res[i] = clusterStat[i][0];
		}
		return res;
	}

	/**
	 * @param nbClusters
	 * @param numCluster
	 * @param clusterStat
	 * @return the proportion of the clusterIn numCluster in the clustersOut 
	 */
	public static double[] proportClusterIn (int nbClusters, int numCluster, int[][] clusterStat) {
		double[] res = new double[nbClusters];

		int[] nbStatsClust = nbStatCluster(nbClusters, getClustersOut(clusterStat));
		for (int i = 0; i<clusterStat.length; i++){
			if (clusterStat[i][0] == numCluster){
				res[clusterStat[i][1]] += (1/ (double) nbStatsClust[clusterStat[i][1]]) * 100;
			}
		}

		return res;
	}
	
	/**
	 * @param nbClusters
	 * @param numCluster
	 * @param clusterStat
	 * @return the proportion of the clusterOut numCluster in the clustersIn 
	 */
	public static double[] proportClusterOut (int nbClusters, int numCluster, int[][] clusterStat) {
		double[] res = new double[nbClusters];

		int[] nbStatsClust = nbStatCluster(nbClusters, getClustersIn(clusterStat));
		for (int i = 0; i<clusterStat.length; i++){
			if (clusterStat[i][1] == numCluster){
				res[clusterStat[i][0]] += 100 / (double) nbStatsClust[clusterStat[i][0]];
			}
		}

		return res;
	}
	
	/**
	 * @param pathInCSV
	 * @param clustersStat
	 * @param nbClustersIn
	 * @param nbClustersOut
	 * @return a matrix with the trips between the different clusters
	 * @throws IOException
	 * @throws org.json.simple.parser.ParseException 
	 * @throws ParseException 
	 */
	public static int[][] createMatType (String pathInCSV, int[][] clustersStat, int nbClustersIn, int nbClustersOut) throws IOException, ParseException, org.json.simple.parser.ParseException{
		int[][] res = new int[nbClustersIn][nbClustersOut];
//		double[][] bikesInArray = MeanStdDev.StringtoDouble(Parser.parse(pathInCSV));
		
		int[] indiceStations = MatriceTrajets.index(RESOURCES_PATH+static_File);
		CsvWeekDay.getWeek(pathInCSV, RESOURCES_PATH+"/weekTraject.csv");
		double[][] matrice = MatriceTrajets.createMat(indiceStations, RESOURCES_PATH+"/weekTraject.csv");
		
		for (int i = 0; i<matrice.length; i++){
			for (int j = 0; j<matrice[0].length; j++){
				res[clustersStat[i][1]][clustersStat[j][0]] += matrice[i][j];
			}
		}
		
		return res;
	}
	
	/**
	 * @param pathInCSV
	 * @param clustersStat
	 * @param nbClustersIn
	 * @param nbClustersOut
	 * @return a matrix with the trips between the different clusters
	 * @throws IOException
	 * @throws org.json.simple.parser.ParseException 
	 * @throws ParseException 
	 */
	public static double[][] createDoubleMatType (String pathInCSV, int[][] clustersStat, int nbClustersIn, int nbClustersOut) throws IOException, ParseException, org.json.simple.parser.ParseException{
		double[][] res = new double[nbClustersIn][nbClustersOut];
//		double[][] bikesInArray = MeanStdDev.StringtoDouble(Parser.parse(pathInCSV));
		
		int[] indiceStations = MatriceTrajets.index(RESOURCES_PATH+static_File);
		CsvWeekDay.getWeek(pathInCSV, RESOURCES_PATH+"/weekTraject.csv");
		double[][] matrice = MatriceTrajets.createMat(indiceStations, RESOURCES_PATH+"/weekTraject.csv");
		
		for (int i = 0; i<matrice.length; i++){
			for (int j = 0; j<matrice[0].length; j++){
				res[clustersStat[i][1]][clustersStat[j][0]] += matrice[i][j];
			}
		}
		
		return res;
	}
	
	/**
	 * @param pathInCSV
	 * @param clustersStat
	 * @param nbClustersIn
	 * @param nbClustersOut
	 * @return a normalized matrix with the trips between the different clusters 
	 * (divided by the number of stations in the clusterOut * nb stations clusterIn)
	 * @throws IOException
	 * @throws org.json.simple.parser.ParseException 
	 * @throws ParseException 
	 */
	public static double[][] createNormMatType (String pathInCSV, int[][] clustersStat, int nbClustersIn, int nbClustersOut) throws IOException, ParseException, org.json.simple.parser.ParseException{
		double[][] res = new double[nbClustersIn][nbClustersOut];
		
		int[] nbStatsClustOut = nbStatCluster(nbClustersOut, getClustersOut(clustersStat));
		int[] nbStatsClustIn = nbStatCluster(nbClustersIn, getClustersIn(clustersStat));
//		double[][] bikesInArray = MeanStdDev.StringtoDouble(Parser.parse(pathInCSV));
		
		int[] indiceStations = MatriceTrajets.index(RESOURCES_PATH+static_File);
		CsvWeekDay.getWeek(pathInCSV, RESOURCES_PATH+"/weekTraject.csv");
		double[][] matrice = MatriceTrajets.createMat(indiceStations, RESOURCES_PATH+"/weekTraject.csv");
		
		for (int i = 0; i<matrice.length; i++){
			for (int j = 0; j<matrice[0].length; j++){
				res[clustersStat[i][1]][clustersStat[j][0]] += matrice[i][j]/(nbStatsClustOut[clustersStat[i][1]]*nbStatsClustIn[clustersStat[j][0]]);
			}
		}
		
		return res;
	}

	public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException{
		int[][] clustersStat = clusterStats(LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_IN,24),
				LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_OUT,24));
		int numC = 4, nbClusters = 5;
		double[] proport = proportClusterOut(nbClusters,numC, clustersStat);
		int[] nbStatsClustOut = nbStatCluster(nbClusters, getClustersOut(clustersStat));
		int[] nbStatsClustIn = nbStatCluster(nbClusters, getClustersIn(clustersStat));
		
		double[][] matType = createNormMatType(RESOURCES_PATH+csv_File, clustersStat, nbClusters, nbClusters);
		
		for (int i = 0; i<proport.length; i++){
			System.out.println("Proportion du clusterOut " + numC + " dans le clusterIn " + i + " = " + proport[i]);
			System.out.println("ClusterIn " + i + " : " + nbStatsClustIn[i] + " , " +
					"et clusterOut " + i + " : " + nbStatsClustOut[i]);
		}
		for (int i = 0; i<matType.length; i++){
			for (int j = 0; j<matType[0].length; j++){
				System.out.print(numberFormat.format(matType[i][j]) + "\t");
			}
			System.out.println("");
		}
		
	}
}
