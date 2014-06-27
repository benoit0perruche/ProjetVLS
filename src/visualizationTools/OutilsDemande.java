package visualizationTools;

import java.awt.Color;

public class OutilsDemande {

	int min = 0;

	/**
	 * @param int[][] matrice
	 * @return the max of the integers
	 */
	public static int getMaxMatrice (int[][] matrice) {
		int max = 0;
		for (int i = 0; i< matrice.length; i++){
			for (int j = 0; j < matrice[i].length; j++){
				max = Math.max(max, matrice[i][j]);
			}
		}

		return max;
	}


	/**
	 * @param double[][] matrice
	 * @return the max of the doubles
	 */
	public static double getMaxMatrice (double[][] matrice) {
		double max = 0;
		for (int i = 0; i< matrice.length; i++){
			for (int j = 0; j < matrice[i].length; j++){
				max = Math.max(max, matrice[i][j]);
			}
		}

		return max;
	}

	/**
	 * @param max the maximum
	 * @param val the value
	 * @return the alpha of the color for this value (log feature)
	 */
	public static double getAlphaColor (int max, int val){
		double alpha = 0.0;
		//		// alpha = (255*val)/max
		//		alpha = ((((Integer) (255)).doubleValue()) / (((Integer) (max)).doubleValue())) 
		//				* (((Integer) (val)).doubleValue());

		alpha = Math.log(val+1) / Math.log(max+1) * 255;

		return alpha;
	}

	/**
	 * @param max the maximum
	 * @param val the value
	 * @return the alpha for this value (log feature)
	 */
	public static double getAlpha (double max, double val){
		double alpha = 0.0;
		if (val == 0){
		}
		else if (val<0){	
			alpha =  Math.log(-val+1) / Math.log(max+1) * 255;
//			alpha =  - Math.log(-val+1) / Math.log(max+1) * 255;
		}
		else {
			alpha = Math.log(val+1) / Math.log(max);
		}

		return alpha;
	}

	/**
	 * @param max the maximum
	 * @param val the value
	 * @return the alpha of the color for this value (log feature)
	 */
	public static double getAlphaColor (double max, double val){
		double alpha = 0.0;
		//		// alpha = (255*val)/max
		//		alpha = ((((Integer) (255)).doubleValue()) / (((Integer) (max)).doubleValue())) 
		//				* (((Integer) (val)).doubleValue());
		if (val == 0){
		}
		else if (val<0){	
			alpha =  Math.log(-val+1) / Math.log(max+1) * 255;
//			alpha =  - Math.log(-val+1) / Math.log(max+1) * 255;
		}
		else {
			alpha = Math.log(val+1) / Math.log(max+1) * 255;
		}

		return alpha;
	}

	/**
	 * @param max (int)
	 * @param val (int)
	 * @return the color of the value val
	 */
	public static Color getColor (int max, int val) {
		double alpha = getAlphaColor(max, val);

		//vert et noir
		Color color = new Color (0, (int) (alpha) ,0);

		//rouge et blanc
		//Color color = new Color (255, (int) (255-alpha),(int) (255-alpha));

		//rouge & noir
		//Color color = new Color ((int) (alpha), 0,0);

		return color;
	}

	/**
	 * @param max (double)
	 * @param val (double)
	 * @return the color of the value val
	 */
	public static Color getColor (double max, double val) {
		double alpha = getAlphaColor(max, val);
		Color color;
		if (alpha<0){		//rouge et blanc
			color = new Color ((int) (-alpha), 0,0);
		}
		else{
			//vert et noir
			color = new Color (0, (int) (alpha),0);
		}
		//rouge et blanc
		//Color color = new Color (255, (int) (255-alpha),(int) (255-alpha));

		//rouge & noir
		//Color color = new Color ((int) (alpha), 0,0);

		return color;
	}

	/**
	 * @param matrice int[][]
	 * @return an array with the color of each value in the matrix 
	 */
	public static Color[][] getColoredArray (int[][] matrice){
		int lignes = matrice.length;
		int colonnes = matrice[0].length;
		Color[][] colorArray = new Color[lignes][colonnes];
		int max = getMaxMatrice(matrice);
		for (int i = 0; i<lignes; i++){
			for (int j = 0; j<colonnes; j++){
				colorArray[i][j] = getColor(max, matrice[i][j]);
			}
		}
		return colorArray;
	}

	/**
	 * @param matrice double[][]
	 * @return an array with the color of each value in the matrix 
	 */
	public static Color[][] getColoredArray (double[][] matrice){
		int lignes = matrice.length;
		int colonnes = matrice[0].length;
		Color[][] colorArray = new Color[lignes][colonnes];
		double max = getMaxMatrice(matrice);
		for (int i = 0; i<lignes; i++){
			for (int j = 0; j<colonnes; j++){
				colorArray[i][j] = getColor(max, matrice[i][j]);
			}
		}
		return colorArray;
	}

	/**
	 * Create a char[] from the int idStation
	 * 
	 * @param int id
	 * @return a character array with the conversion of an integer id
	 */
	public static char[] getChar (int id){
		char[] data = ("" + id).toCharArray();

		return data;
	}

	/**
	 * Create a char[] from the double idStation
	 * 
	 * @param double id
	 * @return a character array with the conversion of a double id
	 */
	public static char[] getChar (double id){
		char[] data = ("" + (int)id).toCharArray();

		return data;
	}

	public static void main(String[] arguments) {
		int[][] mat  = {
				{ 0, 1, 4, 6},
				{ 4, 8, 0, 4},
				{ 3, 5, 4, 8}
		};
		int max = getMaxMatrice(mat);
		System.out.println("Le max est : " + max);
		double alpha = getAlphaColor(max, 6);
		System.out.println("alpha : " + alpha);
		Color color = getColor(max, 6);
		System.out.println("couleur : " + color);
		Color[][] colorArray = getColoredArray(mat);
		System.out.println("matrice couleur : " + colorArray);
	}

}
