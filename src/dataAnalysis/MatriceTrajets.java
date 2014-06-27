package dataAnalysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

public class MatriceTrajets {
	static String RESOURCES_PATH = "/home/ricky/VLS/Washington/";
	static String csv_File = "2014-1st-quarter_FormatOk.csv";
	static String static_File = "capital-bikeshare_12-06-14_04h41_Static.json";

	// Fichiers crees
	static String diago_File = "matDiag.csv";
	static String passage_File = "matPassage.csv";


	static double a = 8.; static double e = 1.; static double c = 10.;
	static double[][] matrice2 = { 
		{a,a,e,e},
		{a,a,e,e},
		{e,e,c,c},
		{e,e,c,c}};
	static DecimalFormat numberFormat = new DecimalFormat("#.0000");



	/**
	 * @param indiceStations
	 * @param pathCsv
	 * @return the matrix of trips between stations from the CSV file
	 * @throws IOException
	 */
	public static double[][] createMat (int[] indiceStations, String pathCsv){
		int nbStations = indiceStations.length;
		double[][] matrice = new double[nbStations][nbStations];
		BufferedReader fichier_source = null;
		try {
			fichier_source = new BufferedReader(new FileReader(pathCsv));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		String chaine;
		int i = 1, idStart = 0, idEnd = 0;
		int stationStart, stationEnd;
		try {
			while((chaine = fichier_source.readLine())!= null)
			{
				if(i>1){//avoid reading the first line
					String[] tabChaine = chaine.split(",");
					stationStart = Integer.parseInt(tabChaine[2]);
					stationEnd = Integer.parseInt(tabChaine[4]);

					for (int k = 0; k < nbStations; k++){
						if (indiceStations[k] == stationStart) {
							idStart = k;
						}
						if (indiceStations[k] == stationEnd) {
							idEnd = k;
						}
					}
					matrice[idStart][idEnd]++;
				}
				i++;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fichier_source.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return matrice;
	}

	/**
	 * @param matrice
	 * @return a symetrical matrix 
	 */
	public static double[][] createSym (double[][] matrice){
		int nbStat = matrice.length;
		double[][] Sym = matrice;
		for (int i = 0; i < nbStat; i++){
			for (int j = 0; j < i; j++){
				Sym[i][j] += matrice[j][i];
				Sym[j][i] = Sym[i][j];
			}
		}
		return Sym;
	}

	/**
	 * @param A matrix
	 * @return V and D with A = V*D*(V^-1), D diag
	 */
	public static Matrix[] diago(Matrix A){

		// compute the spectral decomposition
		EigenvalueDecomposition e = A.eig();
		Matrix V = e.getV();
		Matrix D = e.getD();

		// check that A V = D V
		System.out.print("||AV - DV|| = ");
		System.out.println(A.times(V).minus(V.times(D)).normInf());

		Matrix[] decomposition = new Matrix[2];
		decomposition[0] = V;
		decomposition[1] = D;

		return decomposition;
	}

	/**
	 * @param tab
	 * @return the index of the maximum value on tab
	 */
	public static int getIndiceMax (double[] tab){
		double max = 0, test = 0;
		int indice = 0;
		for (int i = 0; i < tab.length; i++){
			test = Math.max(max, tab[i]);
			if (test > max){
				max = test;
				indice = i;
			}
		}
		return indice;
	}

	/**
	 * @param pop
	 * @param indice
	 * @return the double[] pop without pop[indice]
	 */
	public static double[] stationDone (double[] pop, int indice){
		double[] res = new double[pop.length - 1];
		for (int i = 0; i < indice; i++){
			res[i] = pop[i];
		}
		for (int j = indice; j<pop.length - 1; j++){
			res[j] = pop[j+1];
		}		
		return res;
	}


	/**
	 * @param matrice
	 * @param indice
	 * @return the column of this matrix index indice
	 */
	public static double[] getColonne (double[][] matrice, int indice){
		double[] colonne = new double[matrice.length];
		for (int i = 0; i<matrice.length; i++){
			colonne[i] = matrice[i][indice];
		}		
		return colonne;
	}

	/**
	 * @param matrice
	 * @param indice
	 * @return the line of this matrix index indice
	 */
	public static double[] getLigne (double[][] matrice, int indice){
		double[] ligne = new double[matrice.length];
		for (int i = 0; i<matrice.length; i++){
			ligne[i] = matrice[indice][i];
		}		
		return ligne;
	}

	/**
	 * @param matrice
	 * @param colonne
	 * @param indice
	 * @return the new matrix with this column index indice
	 */
	public static double[][] putColonne (double[][] matrice, double[] colonne, int indice){
		for (int i = 0; i < colonne.length; i++){
			matrice[i][indice] = colonne[i];
		}
		return matrice;
	}

	/**
	 * @param A
	 * @return the matrix A sorted
	 */
	public static Matrix triMatrix (Matrix A){
		double[][] matrice = A.getArray();
		int nbStat = matrice.length;
		double[] popularite = new double[nbStat];

		int indice = 0, tried = 0; double[] ligne, colonne, oldColonne;
		while(tried != nbStat){
			popularite = new double[nbStat];
			for (int i = tried; i<nbStat; i++){
				for (int j = 0; j<nbStat; j++){
					popularite[i] += matrice[i][j];
				}
			}		
			indice = getIndiceMax(popularite);
			popularite = stationDone(popularite, indice);

			ligne = matrice[indice];
			colonne = getColonne(matrice,indice);
			oldColonne = getColonne(matrice, tried);

			matrice = putColonne(matrice, colonne, tried);
			matrice = putColonne(matrice, oldColonne, indice);
			matrice[indice] = matrice[tried];
			matrice[tried] = ligne;			
			tried ++;
		}		
		Matrix res = new Matrix(matrice);
		return res;
	}

	/**
	 * @param staticPath
	 * @return the index of the stations
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 * @throws org.json.simple.parser.ParseException
	 */
	public static int[] index(String staticPath) {
		int[] index = null;

		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader(staticPath));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray stat = (JSONArray) jsonObject.get("stations");
			int length = stat.size();
			index = new int[length];
			for(int i=0; i<length; i++){
				JSONObject station = (JSONObject) stat.get(i);
				index[i] = ((Long) station.get("id")).intValue();
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {

			e.printStackTrace();
		}	
		return index;
	}

	/**
	 * Write the matrix on a CSV file
	 * 
	 * @param Path
	 * @param Mat
	 * @throws IOException
	 */
	public static void writeMat (String Path, Matrix Mat) throws IOException{
		FileWriter writer = new FileWriter(Path);
		int taille = Mat.getColumnDimension();
		double[][] matrice = Mat.getArray();

		for(int i = 0; i<taille; i++){
			for (int j = 0; j < taille-1; j++){
				//				writer.write(numberFormat.format(matrice[i][j])+",");
				writer.write(matrice[i][j]+",");
			}
			//			writer.write(numberFormat.format(matrice[i][taille-1]) + "");
			writer.write(matrice[i][taille-1] + "");
			writer.write("\n");
		}
		writer.close();	
	}
	//	System.out.println(numberFormat.format(number));

	/**
	 * @param pathInStatic
	 * @param pathInCSV
	 * @return les deux matrices (passage [0] et diagonale [1])
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 * @throws org.json.simple.parser.ParseException
	 */
	public static Matrix[] getMatrix (String pathInStatic, String pathInCSV) {
		int[] indiceStations = index(pathInStatic);
		double[][] matrice = createMat(indiceStations, pathInCSV);
		double[][] Sym = createSym(matrice);
		Matrix A = new Matrix(Sym);
		A = triMatrix(A);
		Matrix[] decomp = diago(A);
		return decomp;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException {
		int[] indiceStations = index(RESOURCES_PATH+static_File);
		double[][] matrice = createMat(indiceStations, RESOURCES_PATH+csv_File);
		double[][] Sym = createSym(matrice);
		@SuppressWarnings("unused")
		int nbStat = Sym.length;

		//		for (int i = 0; i < nbStat; i++){
		//			for (int j = 0; j < nbStat-1; j++){
		//				System.out.print(Sym[i][j] + "  ");
		//			}
		//			System.out.println(Sym[i][nbStat-1]);
		//		}

		Matrix A = new Matrix(Sym);
		A = triMatrix(A);

		//		double[][] test = A.getArray();

		System.out.println("Manque plus que la diagonalisation : "); 

		Matrix[] decomp = diago(A);
		//		Matrix[] decomp = diago(new Matrix(matrice2));
		writeMat(RESOURCES_PATH+diago_File, decomp[1]);
		writeMat(RESOURCES_PATH+passage_File, decomp[0]);
		//		System.out.println(decomp.length);
		//		System.out.print(decomp[1]);

		//		System.out.println("Tri : "); 

		//		for (int i = 0; i < nbStat; i++){
		//			for (int j = 0; j < nbStat-1; j++){
		//				System.out.print(test[i][j] + "  ");
		//			}
		//			System.out.println(test[i][nbStat-1]);
		//		}
	}

}