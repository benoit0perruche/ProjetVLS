package visualizationTools;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dataAnalysis.LireArff;

public class Flux {

	protected static int[] indexStation;

	public static int[] getIndex(){
		return indexStation;}
	public static void setIndex(int i, int val) {
		indexStation[i] = val;}

	public static double[][] stationBikesFlux(long idStation, String path) throws Exception {
		double[][] tab = new double[2][24];
		DateFormat hourFormat = new SimpleDateFormat("HH");
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		Date dateIn;
		long time;

		BufferedReader fichier_source = new BufferedReader(new FileReader(path));
		String chaine;

		int i = 1;
		while((chaine = fichier_source.readLine())!= null)
		{
			if(i>1){//avoid reading the first line
				String[] tabChaine = chaine.split(",");
				if(Long.parseLong(tabChaine[4])==idStation){
					dateIn = dateFormat.parse(tabChaine[3]);
					time = dateIn.getTime();
					tab[0][Integer.parseInt(hourFormat.format(time))]++;
				}
				if(Long.parseLong(tabChaine[2])==idStation){
					dateIn = dateFormat.parse(tabChaine[1]);
					time = dateIn.getTime();
					tab[1][Integer.parseInt(hourFormat.format(time))]++;
				}				
			}
			i++;
		}
		fichier_source.close();
		return tab;
	}
	
	public static double[] index(String staticPath) throws FileNotFoundException, IOException, ParseException{
		double[] index;
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(staticPath));
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray stat = (JSONArray) jsonObject.get("stations");
		int length = stat.size();
		index = new double[length];
		for(int i=0; i<length; i++){
			JSONObject station = (JSONObject) stat.get(i);
			index[i] = (Long) station.get("id");
		}
		return index;
	}

	public static double[][] bikesFlux(String tripsPath, String staticPath, double nbDay, int id) throws Exception{
		double[][] sizeStations;
		double[][] tab;

		//get the size of each station, store it in sizeStations
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader(staticPath));
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray stat = (JSONArray) jsonObject.get("stations");
		int length = stat.size();
		sizeStations = new double[length][2];
		for(int i=0; i<length; i++){
			JSONObject station = (JSONObject) stat.get(i);
			sizeStations[i][0] = (Long) station.get("id");
			sizeStations[i][1] = (Long) station.get("slots");
		}

		tab = new double[length][49];
		DateFormat hourFormat = new SimpleDateFormat("HH");
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		Date dateIn;
		long time;
		double[] inMean = new double[length];
		double[] outMean = new double[length];

		BufferedReader fichier_source = new BufferedReader(new FileReader(tripsPath));
		String chaine;

		int in = 0;
		int out = 0;
		int i = 1;
		while((chaine = fichier_source.readLine())!= null)
		{
			if(i>1){//avoid reading the first line
				String[] tabChaine = chaine.split(",");
				indexStation = new int[length];
				//loop !
				for(int j=0; j<length; j++){
					if(id == 1){tab[j][0] = sizeStations[j][0];}
					indexStation[j] = (int) sizeStations[j][0];
					if(Long.parseLong(tabChaine[4])==sizeStations[j][0]){
						in = j;
					}
					if(Long.parseLong(tabChaine[2])==sizeStations[j][0]){
						out = j;
					}
				}

				dateIn = dateFormat.parse(tabChaine[3]);
				time = dateIn.getTime();
				tab[in][id+Integer.parseInt(hourFormat.format(time))] = 
						tab[in][id+Integer.parseInt(hourFormat.format(time))] +
						1./sizeStations[in][1]/nbDay*100.;


				dateIn = dateFormat.parse(tabChaine[1]);
				time = dateIn.getTime();
				tab[out][id+24+Integer.parseInt(hourFormat.format(time))] = 
						tab[out][id+24+Integer.parseInt(hourFormat.format(time))] +
						1./sizeStations[out][1]/nbDay*100.;
			}
			i++;
		}

		for(int l=0; l<length; l++){
			for(int j=0; j<24+id; j++){	
				inMean[l+id] += tab[l+id][id+j];
				outMean[l+id] += tab[l+id][24+id+j];
			}
		}
		//comment not to normalize
		//
		for(int l=0; l<tab.length; l++){
			for(int j=0; j<24+id; j++){
				if (inMean[id+l] != 0){
					tab[id+l][id+j] = tab[id+l][id+j]/inMean[id+l] * 10;
				}
				if (outMean[id+l] != 0){
					tab[id+l][id+24+j] = tab[id+l][id+24+j]/outMean[id+l] * 10;
				}
			}
		}
		//
		fichier_source.close();
		return tab;

	}

	public static void writeMatrixCsv(double[][] matrix,String pathOut, int id) throws IOException{
		int length = matrix.length;
		FileWriter writer = new FileWriter(pathOut);
		if(id ==1){writer.write("id,");}
		writer.write("in0,in1,in2,in3,in4,in5,in6,in7,in8,in9,in10,in11,in12," +
				"in13,in14,in15,in16,in17,in18,in19,in20,in21,in22,in23," +
				"out0,out1,out2,out3,out4,out5,out6,out7,out8,out9,out10,out11,out12," +
				"out13,out14,out15,out16,out17,out18,out19,out20,out21,out22,out23\n");
		for (int j=0; j<length; j++) {
			writer.write(matrix[j][0]+"");
			for (int i=1; i<48+id; i++) {
				writer.write(",");
				writer.write(matrix[j][i]+"");
			}
			writer.write("\n");
		}
		writer.close();
	}
	public static void writeMatrixInCsv(double[][] matrix,String pathOut, int id) throws IOException{
		int length = matrix.length;
		FileWriter writer = new FileWriter(pathOut);
		if(id ==1){writer.write("id,");}
		writer.write("in0,in1,in2,in3,in4,in5,in6,in7,in8,in9,in10,in11,in12," +
				"in13,in14,in15,in16,in17,in18,in19,in20,in21,in22,in23\n");
		for (int j=0; j<length; j++) {
			writer.write(matrix[j][0]+"");
			for (int i=1; i<24+id; i++) {
				writer.write(",");
				writer.write(matrix[j][i]+"");
			}
			writer.write("\n");
		}
		writer.close();
	}

	public static void writeMatrixOutCsv(double[][] matrix,String pathOut, int id) throws IOException{
		int length = matrix.length;
		FileWriter writer = new FileWriter(pathOut);
		if(id ==1){writer.write("id,");}
		writer.write("out0,out1,out2,out3,out4,out5,out6,out7,out8,out9,out10,out11,out12," +
				"out13,out14,out15,out16,out17,out18,out19,out20,out21,out22,out23\n");
		for (int j=0; j<length; j++) {
			if(id==1){writer.write(matrix[j][0]+"");}
			writer.write(matrix[j][24+id]+"");
			for (int i=25+id; i<48+id; i++) {
				writer.write(",");
				writer.write(matrix[j][i]+"");
			}
			writer.write("\n");
		}
		writer.close();
	}

	public static String[] readWekaCsv(String path,int nbCol) throws IOException{

		String[] list = new String[indexStation.length];

		try
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
			String chaine;
			int i = 1; // To avoid reading the first lines

			while((chaine = bufferedReader.readLine())!= null)
			{
				if(i > nbCol + 6)
				{
					String[] tabChaine = chaine.split(",");

					list[i-nbCol-7] = tabChaine[nbCol+1];

				}
				i++;
			}

			bufferedReader.close();                  
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File unfindable.");
		}

		return list;	
	}

	public static void main(String[] arguments) throws Exception {

		//        String RESOURCES_PATH = "/home/ricky/VLS/Washington/";
		//        String TRIPS_FILE_NAME = "Washington_Trips_7-3-2014(copie)";

		//		double[][] tab = stationBikesFlux(31107,"/home/ricky/VLS/Washington/2014-1st-quarter_FormatOk.csv");
		//		for(int i=0;i<10;i++){
		//			System.out.println("---|--in--|--out--------");
		//			System.out.print("0" + i + " | ");
		//			System.out.println(tab[0][i]+" | "+tab[1][i]);
		//		}
		//		for(int i=10;i<24;i++){
		//			System.out.println("---|--in---|--out-------");
		//			System.out.print(i + " | ");
		//			System.out.println(tab[0][i]+" | "+tab[1][i]);
		//		}

//		@SuppressWarnings("unused")
		double[][] matrixVweek = bikesFlux("/home/ricky/VLS/Washington/Washington-Week-January.csv",
				"/home/ricky/VLS/Washington/capital-bikeshare_12-06-14_04h41_Static.json", 22.,0);
		//		double[][] matrixVweekend = bikesFlux("weekend.csv",
		//				"/home/ricky/VLS/Washington/capital-bikeshare_12-06-14_04h41_Static.json", 26.,0);

		//		for(int l=0;l<320;l++){
		//		for(int i=0;i<49;i++){
		//			//			System.out.println("---|--in--|--out--------");
		//			//			System.out.print("0" + i + " | ");
		//			System.out.print(matrixVweek[l][i]+"|");
		//		}
		//		System.out.println();
		//	}

		writeMatrixCsv(matrixVweek,"Vweek_noid.csv",0);
		//		writeMatrixCsv(matrixVweekend,"Vweekend_noid.csv",0);
		writeMatrixOutCsv(matrixVweek,"Vweek_noid_Out.csv",0);
		writeMatrixInCsv(matrixVweek,"Vweek_noid_In.csv",0);

		//String[] list = readWekaCsv("save.csv",24);

		int[] test = LireArff.lireArff("save.csv",24);

		for (int i=0;i<indexStation.length;i++){
			System.out.print(indexStation[i]);
			System.out.println(" | " + test[i]);
		}
	}
}