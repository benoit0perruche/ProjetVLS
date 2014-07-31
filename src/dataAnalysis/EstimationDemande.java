package dataAnalysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import visualizationTools.CsvWeekDay;

public class EstimationDemande {
	private static final String ARFF_FILE_PATH = "/home/benoit/Documents/Polytech4A/Stage/Weka/Washington";
	private static final String FILE_NAME_IN = "/clustersIn.arff";
	private static final String FILE_NAME_OUT = "/clustersOut.arff";
	static String RESOURCES_PATH = "/home/benoit/Documents/Polytech4A/Stage/Donnees/Washington";
	static String csv_File = "/week.csv";
	static String static_File = "/config_Washington.json";
	static int nbClustersOut = 5, nbClustersIn = 5;

	static DecimalFormat numberFormat = new DecimalFormat("###.##");

	/**
	 * @return the matrix clusterOut->clusterIn, with the trips mean by day / the number of stations in the 
	 * cluster Out
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 * @throws org.json.simple.parser.ParseException
	 */
	public static double[][] moyWeekTripsOut () throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
		int[][] clustersStat = StationType.clusterStats(LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_IN,24),
				LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_OUT,24));
		double[][] matType = StationType.createDoubleMatType(RESOURCES_PATH+csv_File, clustersStat, nbClustersIn, nbClustersOut);
		int[] nbStatsClustOut = StationType.nbStatCluster(nbClustersOut, StationType.getClustersOut(clustersStat));

		for (int i = 0; i<nbClustersOut; i++){
			for (int j = 0; j<nbClustersIn; j++){
				matType[i][j] = (matType[i][j]/21)/nbStatsClustOut[i];
			}
		}
		return matType;
	}

	/**
	 * @return the matrix clusterOut->clusterIn, with the trips mean by day / the number of stations in the 
	 * cluster In
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 * @throws org.json.simple.parser.ParseException
	 */
	public static double[][] moyWeekTripsIn () throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
		int[][] clustersStat = StationType.clusterStats(LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_IN,24),
				LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_OUT,24));
		double[][] matType = StationType.createDoubleMatType(RESOURCES_PATH+csv_File, clustersStat, nbClustersIn, nbClustersOut);
		int[] nbStatsClustIn = StationType.nbStatCluster(nbClustersIn, StationType.getClustersIn(clustersStat));

		for (int i = 0; i<nbClustersOut; i++){
			for (int j = 0; j<nbClustersIn; j++){
				matType[i][j] = (matType[i][j]/21)/nbStatsClustIn[i];
			}
		}
		return matType;
	}

	/**
	 * Get a trips' array with : station beginning, station ending, timestamp begin
	 * 
	 * @param csvTrips
	 * @param indiceStations
	 * @return
	 * @throws IOException
	 */
	public static int[][] getTripsOut (String csvTrips, int[] indiceStations) throws IOException{
		List<String[]> trips = dataRecovery.Parser.parse(csvTrips);
		int[][] res = new int[trips.size()][3];
		int i = 0, statB, statE;
		for (String[] trip : trips) {
			statB = Integer.parseInt(trip[2]);
			statE = Integer.parseInt(trip[4]);
			res[i][2] = Integer.parseInt(trip[1]); //visualisationTools.ComparaisonDates.getHeure(trip[1]);

			for (int k = 0; k < indiceStations.length; k++){
				if (indiceStations[k] == statB) {
					res[i][0] = k;
				}
				if (indiceStations[k] == statE) {
					res[i][1] = k;
				}
			}	
			i++;
		}
		return res;
	}
	
	
	/**
	 * @param clustersStat
	 * @return res[0] = number of clusters In, res[1] = number of clusters Out
	 */
	public static int[] getNumClusters (int[][] clustersStat) {
		int[] res = new int[2];
		int maxIn = 0, maxOut = 0;
		for (int i = 0; i<clustersStat.length; i++){
			maxIn = Math.max(maxIn, clustersStat[i][0]);
			maxOut = Math.max(maxOut, clustersStat[i][1]);
		}
		res[0] = maxIn + 1; res[1] = maxOut + 1;
		return res;
	}

	/**
	 * @param nbClusterOut
	 * @param nbClusterIn
	 * @param clustersStat
	 * @return the double[clustersIn][hour of the day][clustersOut][1] 
	 * the demand for each clusterOut / time / clusterIn
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 * @throws org.json.simple.parser.ParseException
	 */
	public static double[][][][] clusterDemandeHoraire (int[][] clustersStat) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
		int[] numClust = getNumClusters(clustersStat);
		int nbClusterOut = numClust[1]; int nbClusterIn = numClust[0];
		double[][][][] res = new double[nbClusterOut][][][];
		for (int l = 0; l<nbClusterOut; l++){
		res[l] = new double[24][][];
			for (int m = 0; m<24; m++) {
				res[l][m] = new double[nbClusterIn][1];
			}
		}
		int[] indiceStations = MatriceTrajets.index(RESOURCES_PATH+static_File);
		CsvWeekDay.getWeek(RESOURCES_PATH+csv_File, RESOURCES_PATH+"/weekTraject.csv");
		int[][] trips = getTripsOut(RESOURCES_PATH+"/weekTraject.csv", indiceStations);
		int[][] clusterStat = StationType.clusterStats(LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_IN,24),
				LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_OUT,24));
		int clusterDep, clusterEnd;
		for (int i = 0; i<trips.length; i++){
			clusterDep = clusterStat[trips[i][0]][1];
			clusterEnd = clusterStat[trips[i][1]][0];
			res[clusterDep][trips[i][2]][clusterEnd][0] += 1./21.; 
		}
		return res;
	}
	
	/**
	 * @param pathCSV
	 * @param pathArff
	 * @param nbTimeInterval
	 * @param nbClusterOut
	 * @param nbClusterIn
	 * @param nbJours
	 * @return the double[clustersIn][hour of the day][clustersOut][1] 
	 * the demand for each clusterOut / time / clusterIn
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 * @throws org.json.simple.parser.ParseException
	 */
	public static double[][][][] matriceDemande (String pathCSV, String pathArffOut, String pathArffIn, 
			int nbTimeInterval, int nbJours) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
		
		int[][] clusterStat = StationType.clusterStats(LireArff.lireArff(pathArffIn,24),
				LireArff.lireArff(pathArffOut,24));
		
		int[] numClust = getNumClusters(clusterStat);
		int nbClusterOut = numClust[1]; int nbClusterIn = numClust[0];
		double[][][][] res = new double[nbClusterOut][][][];
		for (int l = 0; l<nbClusterOut; l++){
		res[l] = new double[nbTimeInterval][][];
			for (int m = 0; m<nbTimeInterval; m++) {
				res[l][m] = new double[nbClusterIn][1];
			}
		}		
		
		List<String[]> trips = dataRecovery.Parser.parse(pathCSV);
		int clusterStart = 0, clusterEnd = 0;
		int statB = 0, statE = 0;
		
		for (String[] trip : trips) {
			statB = Integer.parseInt(trip[2]);
			statE = Integer.parseInt(trip[4]);
			if (statB <= clusterStat.length && statE <= clusterStat.length) {
			clusterStart = clusterStat[Integer.parseInt(trip[2])][1];
			clusterEnd = clusterStat[Integer.parseInt(trip[4])][0];
			
			res[clusterStart][Integer.parseInt(trip[1])][clusterEnd][0] += 1./(double) nbJours; 
//			System.out.println("Fonctionne pour " + clusterStart + " vers " + 
//			clusterEnd + " à " + Integer.parseInt(trip[1]));
			}
		}
		return res;
	}
	
	/**
	 * @param clusterOut
	 * @param horaire
	 * @param clusterIn
	 * @param clusterDemande
	 * @return a double res, the demand on bikes for these clusters at that time
	 */
	public static double getDemande (int clusterOut, int horaire, int clusterIn, double[][][][] clusterDemande){
		double res = clusterDemande[clusterOut][horaire][clusterIn][0];		
		return res;
	}

	/**
	 * @param moyWeekTrip
	 * @param clusterOut
	 * @param clusterIn
	 * @return a double res, the mean of trips from this clusterOut to this clusterIn
	 */
	public static double estim (double[][] moyWeekTrip, int clusterOut,int clusterIn){
		double res = moyWeekTrip[clusterOut][clusterIn];		
		return res;
	}

	public static void main(String[] args) throws IOException, ParseException, org.json.simple.parser.ParseException{

		double[][][][] matriceDem = matriceDemande(RESOURCES_PATH+csv_File,ARFF_FILE_PATH+FILE_NAME_OUT,
				ARFF_FILE_PATH+FILE_NAME_IN, 24, 21);
		
		@SuppressWarnings("unused")
		int[][] clusterStat = StationType.clusterStats(LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_IN,24),
				LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_OUT,24));
		int clusterIn = 3, clusterOut = 5, heure = 0;
		// double[][][][] clusterDemande = clusterDemandeHoraire(5,5,clusterStat);
		double res = getDemande(clusterOut,heure,clusterIn,matriceDem);
		System.out.println("La demande à " + heure +"h du clusterOut " + clusterOut +
				" vers le clusterIn " + clusterIn + " est de : " + res);
	}
}
