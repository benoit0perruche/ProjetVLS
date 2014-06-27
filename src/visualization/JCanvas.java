package visualization;


import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import visualizationTools.OutilsDemande;
import dataAnalysis.MatriceTrajets;
import Jama.Matrix;


public class JCanvas extends JPanel {

	private static final long serialVersionUID = 1L;

	//DEBUG	
	//	int[][] matrice = {
	//			{ 0, 1, 2, 3, 4},
	//			{ 1, 10, 16, 10, 9},
	//			{ 2, 20, 8, 9, 8},
	//			{ 3, 8, 15, 7, 8},
	//			{ 4, 15, 7, 10, 5}
	//	};

	String path = "/home/ricky/VLS/Washington/";

	static String RESOURCES_PATH = "/home/ricky/VLS/Washington/";
	static String csv_File = "2014-1st-quarter_FormatOk.csv";
	static String static_File = "capital-bikeshare_12-06-14_04h41_Static.json";

	//	int[][] matrice = Demande.matriceDemandeSommeLignesColonnes(
	//			path + "2014-1st-quarter_FormatOk.csv",	//file for the first quarter 2014
	////			path + "Washington_Trips_7-3-2014(copie)_FormatOk.csv",	//file for 7th March
	//			path + "capital-bikeshare_12-06-14_04h41_Static.json");


	//(int)Math.round(x);

	int[] indiceStations = MatriceTrajets.index(RESOURCES_PATH+static_File);
	//	int nbStat = indiceStations.length;
	//	double[][] matrice = MatriceTrajets.createMat(indiceStations, RESOURCES_PATH+csv_File);
	static double[][] matrice = { 
		{8,0,0},
		{0,5,0}, 
		{0,0,5}
	};
	double[][] Sym = MatriceTrajets.createSym(matrice);

	Matrix[] decomp = MatriceTrajets.diago(new Matrix(Sym));
	//		Matrix[] decomp = diago(new Matrix(matrice2));
	double [][] matriceToDraw = decomp[0].getArray();

	//	Matrix[] decomp = MatriceTrajets.getMatrix(RESOURCES_PATH+static_File, RESOURCES_PATH+csv_File);
	//	double [][] matriceToDraw = decomp[0].getArray();



	public void paint(Graphics g) {
		int lignes = matriceToDraw.length;
		int colonnes = matriceToDraw[0].length;
		int width = 40;
		//		char[] data;

		g.drawRect(0, 0, lignes*width+3, colonnes*width+3);

		//		for (int k = 1; k<colonnes; k++){
		//			data = OutilsDemande.getChar(matrice[0][k]);
		//			g.drawChars(data, 0, data.length, k*width + (width/8), (width/2));
		//		}
		//		for (int l = 1; l<lignes; l++){
		//			data = OutilsDemande.getChar(matrice[l][0]);
		//			g.drawChars(data, 0, data.length, (width/8), l*width+(width/2));
		//		}

		for (int i = 0; i<colonnes; i++){
			for (int j = 0; j<lignes; j++){
				System.out.print(matriceToDraw[i][j]+" ");
			}
			System.out.println();
		}
		Color[][] colorArray = OutilsDemande.getColoredArray(matriceToDraw);
		for (int i = 0; i<colonnes; i++){
			for (int j = 0; j<lignes; j++){
				g.setColor(colorArray[j][i]);
				g.fillRect((i)*width, (j)*width, width, width);
			}
		}
	}


}
