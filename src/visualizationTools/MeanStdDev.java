package visualizationTools;

import java.util.List;
import java.io.IOException;

import dataAnalysis.LireArff;
import dataRecovery.Parser;

public class MeanStdDev {

	/**
	 * @param clusterArray the array of the stations and there clusters
	 * @param cluster the cluster number
	 * @return the array of the stations in this cluster
	 */
	public static int[] getStationsCluster (int[] clusterArray, int cluster){
		int taille = 0;
		for (int l = 0; l<clusterArray.length; l++){
			if (clusterArray[l] == cluster){
				taille++;
			}
		}
		int[] stationsCluster = new int[taille];
		int j = 0;
		for (int i = 0; i<clusterArray.length; i++){
			if (clusterArray[i] == cluster){
				stationsCluster[j] = i;
				j++;
			}
		}		
		return stationsCluster;
	}

	/**
	 * @param bikes the array of bikesIn for an hour for each station
	 * @return the mean of the bikesIn for one hour
	 */
	public static double getMean (double[] bikes){
		double all = 0;
		double mean = 0.;
		int nbStations = bikes.length;
		for (int i = 0; i < nbStations; i++){
			all += bikes[i];
		}
		mean = all / ((double) nbStations);
		return mean;
	}

	/**
	 * @param clusterArray the array of the stations and there clusters
	 * @param bikesInArray the array of all the stations and their mean for each hour
	 * @param cluster the cluster number
	 * @param nbParam the number of column of the matrix used to make the clusters
	 * @return an array with the mean of the stations on this cluster for each hour
	 */
	public static double[][] getMeanStd (int[] clusterArray, double[][] bikesInArray, int cluster, int nbParam){
		int[] stationsCluster = getStationsCluster(clusterArray, cluster);
		int nbStations = stationsCluster.length;       
		double[] meanArray = new double[nbParam];
		double[] stdDev = new double[nbStations];
		double[] stdDevglob = new double[nbParam];
		double[] bikes = new double [nbStations];
		double[][] array = new double[nbParam+1][2];

		for (int i = 0; i<nbParam; i++){
			for (int j = 0; j<nbStations; j++){
				bikes[j] = bikesInArray[stationsCluster[j]][i];
			}
			meanArray[i] = getMean(bikes);
			array[i][0] = meanArray[i];
			for (int j = 0;j < nbStations; j++){
				stdDev[j] = (meanArray[i]-bikes[j]);
				stdDevglob[i] += stdDev[j] * stdDev[j];
			}
			stdDevglob[i] = Math.sqrt(stdDevglob[i]/nbStations);
			array[i][1] = stdDevglob[i];
		}
		array[nbParam][0] = nbStations;
		return array;
	}

	/**
	 * @param stringTab
	 * @return 
	 */
	public static double[][] StringtoDouble(List<String[]> stringTab){
		double[][] doubletab = new double[stringTab.size()][stringTab.get(0).length];

		for(int i=0; i<stringTab.size(); i++){
			for(int j=0; j<stringTab.get(0).length; j++){
				doubletab[i][j] = Double.parseDouble(stringTab.get(i)[j]);
			}
		}
		return doubletab;
	}


	private static final String ARFF_FILE_PATH = "";
	private static final String FILE_NAME = "save.csv";
//	static String RESOURCES_PATH = "/home/benoit/Documents/Polytech4A/Stage/Donnees/Washington/";
	static String Week_File = "Vweek_noid_In.csv";
	

	public static void main(String[] args) throws IOException {
		int[] clusterArray = LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME,24);

		double[][] bikesInArray = StringtoDouble(Parser.parse(ARFF_FILE_PATH+Week_File));

		int numCluster = 0;
		double[][] mean = getMeanStd(clusterArray, bikesInArray, numCluster, 5);
		for (int i = 0; i < mean.length-1; i++){
			System.out.println("Horaire : " + i + " Moyenne : " + mean[i][0]);
		}
		System.out.println("Nombre de stations du cluster " + numCluster + 
				" : " + (int)mean[mean.length-1][0]);
		
		double[][] array = getMeanStd(clusterArray, bikesInArray, 0, 24);
		for (int i = 0; i < array.length; i++){
			System.out.println("Horaire : " + i + " Moyenne : " + array[i][0] + " stdDev : " + array[i][1]);
		}
	}
}
