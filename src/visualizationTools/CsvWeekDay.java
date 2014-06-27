package visualizationTools;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dataRecovery.Parser;

public class CsvWeekDay {

	static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	static Date dateIn;


	/**
	 * Crée un nouveau fichier des trajets pour les jours de la semaine
	 * 
	 * @param pathIn
	 * @param pathOut
	 */
	public static void getWeek (String pathIn, String pathOut) {

		try {
			List<String[]> data = Parser.parse(pathIn);
			FileWriter writer = new FileWriter(pathOut);
			int size = data.get(0).length;
			if (size == 9){
				writer.write("Duration,Start date,Start terminal," +
						"End date,End terminal,Bike#,Member Type,Distance,Vitesse\n");
			}
			else if (size == 7){
				writer.write("Duration,Start date,Start terminal," +
						"End date,End terminal,Bike#,Member Type\n");
			}
			else {
				System.out.println("wrong number of columns into the csv file"); 
			}

			for(String[] str : data){

				dateIn = dateFormat.parse(str[1]);

				Calendar cal = Calendar.getInstance();
				cal.setTime(dateIn);
				int wday = cal.get(Calendar.DAY_OF_WEEK);
				if ((wday != Calendar.SATURDAY) && (wday != Calendar.SUNDAY)) {
					for (int j = 0; j < size-1; j++){
						writer.write(str[j]+",");
					}
					writer.write(str[size-1]);
					writer.write("\n");			
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Crée un nouveau fichier des trajets pour les jours du week end
	 * 
	 * @param pathIn
	 * @param pathOut
	 */
	public static void getWeekEnd (String pathIn, String pathOut) {

		try {
			List<String[]> data = Parser.parse(pathIn);
			FileWriter writer = new FileWriter(pathOut);
			int size = data.get(0).length;
			if (size == 9){
				writer.write("Duration,Start date,Start terminal," +
						"End date,End terminal,Bike#,Member Type,Distance,Vitesse\n");
			}
			else if (size == 7){
				writer.write("Duration,Start date,Start terminal," +
						"End date,End terminal,Bike#,Member Type\n");
			}
			else {
				System.out.println("wrong number of columns into the csv file"); 
			}

			for(String[] str : data){

				dateIn = dateFormat.parse(str[1]);

				Calendar cal = Calendar.getInstance();
				cal.setTime(dateIn);
				int wday = cal.get(Calendar.DAY_OF_WEEK);
				if ((wday == Calendar.SATURDAY) || (wday == Calendar.SUNDAY)) {
					for (int j = 0; j < size-1; j++){
						writer.write(str[j]+",");
					}
					writer.write(str[size-1]);
					writer.write("\n");
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] arguments) throws IOException {
		String pathIn = "/home/ricky/VLS/Washington/" +
				"2014-1st-quarter_FormatOk.csv";

		getWeek(pathIn, "week.csv");
		getWeekEnd(pathIn, "weekend.csv");
	}

}