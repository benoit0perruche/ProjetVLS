package visualizationTools;

import dataAnalysis.MatriceTrajets;

public class BalanceScore {

	public static double[][] Score(int[] stationIndex, double[][] matrice){

		int size = matrice.length;
		double[][] score = new double[size+1][3];
		double diff;
		double pop;

		for (int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				score[i][0] += matrice[i][j];	//out
				score[i][1] += matrice[j][i];	//in
			}

			diff = score[i][1] - score[i][0]; 	// diff = in - out
			pop	= score[i][1] + score[i][0]; 	// pop = in + out

			if(pop == 0){
				score[i][2] = 0;
			}
			else{
				score[i][2]	= diff / pop; 			// balance = diff / pop
			}		

		}

		return score;
	}

	public static double[][] ScoreLOG(int[] stationIndex, double[][] matrice){

		int size = matrice.length;
		double[][] score = new double[size+1][3];
		double diff;
		double pop;

		for (int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				score[i][0] += matrice[i][j];	//out
				score[i][1] += matrice[j][i];	//in
			}

			diff = score[i][1] - score[i][0]; 	// diff = in - out
			pop	= score[i][1] + score[i][0]; 	// pop = in + out

			if(pop == 0){
				score[i][2] = 0;
			}
			else{
				if (diff>0){
					score[i][2]	= Math.log(diff) /  Math.log(pop); 			// balance = diff / pop
				}
				else if(diff<0){
					score[i][2]	= -Math.log(-diff) /  Math.log(pop);	// balance = diff / pop
				}
			}		
		}

		return score;
	}

	public static double[][] ScoreSQRT(int[] stationIndex, double[][] matrice){

		int size = matrice.length;
		double[][] score = new double[size+1][3];
		double diff;
		double pop;

		for (int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				score[i][0] += matrice[i][j];	//out
				score[i][1] += matrice[j][i];	//in
			}

			diff = score[i][1] - score[i][0]; 	// diff = in - out
			pop	= score[i][1] + score[i][0]; 	// pop = in + out

			if(pop == 0){
				score[i][2] = 0;
			}
			else{

				score[i][2]	= diff /  Math.sqrt(pop); 			// balance = diff / sqrt(pop)

			}		

		}

		return score;
	}

	public static double[][] ScoreRAW(int[] stationIndex, double[][] matrice){

		int size = matrice.length;
		double[][] score = new double[size+1][3];
		double diff;

		for (int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				score[i][0] += matrice[i][j];	//out
				score[i][1] += matrice[j][i];	//in
			}

			diff = score[i][1] - score[i][0]; 	// diff = in - out	
			score[i][2]	= diff;
		}

		return score;
	}



	public static void main(String args[]){
		String StaticPath = "static.json";
		String CSVPath = "aaa.csv";
		int[] stationIndex = MatriceTrajets.index(StaticPath);
		double[][] matrice = MatriceTrajets.createMat(stationIndex, CSVPath);

		double[][] score = Score(stationIndex, matrice);
		double max = 0;
		double min = 0;
		for (int i=0; i<score.length; i++){
			max = Math.max(max, score[i][2]);
			min = Math.min(min, score[i][2]);
			System.out.println(score[i][0]+" | "+score[i][1]+" | "+score[i][2]);
		}	
		System.out.println("max "+max+"  min "+min);
	}
}