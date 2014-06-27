package visualization;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import Jama.Matrix;
import dataAnalysis.MatriceTrajets;
import visualizationTools.OutilsDemande;

public class Legend extends JPanel {

	private static final long serialVersionUID = 1L;

	
	String path = "/home/ricky/VLS/Washington/";
	
	static String RESOURCES_PATH = "/home/ricky/VLS/Washington/";
	static String csv_File = "2014-1st-quarter_FormatOk.csv";
	static String static_File = "capital-bikeshare_12-06-14_04h41_Static.json";
	
//	int[][] matrice = Demande.matriceDemandeSommeLignesColonnes(
//	path + "2014-1st-quarter_FormatOk.csv",	//file for the first quarter 2014
////	path + "Washington_Trips_7-3-2014(copie)_FormatOk.csv",	//file for 7th March
//	path + "capital-bikeshare_12-06-14_04h41_Static.json");


//(int)Math.round(x);

int[] indiceStations = MatriceTrajets.index(RESOURCES_PATH+static_File);
//int nbStat = indiceStations.length;
//double[][] matrice = MatriceTrajets.createMat(indiceStations, RESOURCES_PATH+csv_File);
static double[][] matrice1 = { 
{4,12,1,0,14},
{4,8,3,2,13},
{1,4,9,10,2},
{2,3,12,7,2}, 
{4,15,3,1,12}
};
double[][] Sym = MatriceTrajets.createSym(matrice1);

Matrix[] decomp = MatriceTrajets.diago(new Matrix(Sym));
//		Matrix[] decomp = diago(new Matrix(matrice2));
double [][] matriceToDraw = decomp[0].getArray();

//Matrix[] decomp = MatriceTrajets.getMatrix(RESOURCES_PATH+static_File, RESOURCES_PATH+csv_File);
//double [][] matriceToDraw = decomp[0].getArray();

	double max = OutilsDemande.getMaxMatrice(matrice1);
	
	double alpha = 1./16.;

	int[][] matrice = new int[18][2];
	
	

	

	public void paint(Graphics g) {
		int lignes = 18;
		int colonnes = 2;
		int width = 40;
		char[] data;


		g.drawRect(0, 40, colonnes*width, (lignes)*width);

		Color[][] colorArray = new Color[16][1];
		
		for (int i=0;i<16;i++){
			matrice[i+2][0] = (int)(Math.exp(alpha*(16-i)*Math.log(max)));
			matrice[i+2][1] = matrice[i+2][0];
			colorArray[i][0] = OutilsDemande.getColor(max,matrice[i+2][0]);
		}
		
		data = OutilsDemande.getChar(max);
		g.drawChars(data, 0, data.length, (width), 1*width+(9*width/10));
		
		data = OutilsDemande.getChar(0);
		g.drawChars(data, 0, data.length, (width), 18*width+(3*width/10));
		
		for (int l = 2; l<lignes; l++){
			data = OutilsDemande.getChar(matrice[l][0]);
			g.drawChars(data, 0, data.length, (width/8), l*width+(2*width/3));
		}

		for (int i = 0; i<colonnes-1; i++){
			for (int j = 0; j<lignes-2; j++){
				g.setColor(colorArray[j][i]);
				g.fillRect((i+1)*width, (j+2)*width, width, width);
			}
		}
	}


}