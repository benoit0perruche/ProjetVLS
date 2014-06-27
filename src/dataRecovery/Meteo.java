package dataRecovery;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Meteo {

	/**
	 * @param folderPath the path to the folder containing the meteo files
	 * @param timestamp the date when the meteo is needed (local time, not UTC)
	 * @return the line corresponding to the closest time from the timestamp
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String[] getMeteo(String folderPath, long timestamp) throws IOException, ParseException{

		String[] line = null;
		List<String[]> meteoDay;
		
		Date date;
		long startDate;
		long delta = 1000 * 60 *60 * 24;
		String dateFile = "";

		
		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		DateFormat montFormat = new SimpleDateFormat("MM");
		DateFormat dayFormat = new SimpleDateFormat("dd");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		

		dateFile = yearFormat.format(timestamp)+"-"+
				Integer.parseInt(montFormat.format(timestamp))+"-"+
				Integer.parseInt(dayFormat.format(timestamp));


		String path = folderPath + dateFile + ".csv";
		File file = new File (path);
		if(!file.exists()){
			System.out.println("Day unfindable, specify an other folder.");
		} 
		else{
			
			//parse the file
			meteoDay = Parser.parse(path);
			//explore the parsed file
			for(String[] str : meteoDay){

				int length = str[0].length();
				if (length	> 10 ){		//to avoid <br> lines	
					
					//convert String "yyyy-MM-dd HH:mm:ss" to timestamp
					date = dateFormat.parse(str[0]);
					startDate = date.getTime();
					
					//compare the delta between timestamp and the line timestamp
					if (Math.abs(timestamp - startDate) > delta){	//can't enter on first iteration (delta = 24h)
						break;//exit the loop
					}
					else{
						delta = Math.abs(timestamp - startDate); //store the new delta value
						line = str; //store the line
					}
				}
			}
		}	

		return line;
	}
	
	//debug
	public static void main(String[] arguments) throws Exception {
		String pathFolder = "/home/ricky/VLS/Washington/meteo/";
		String[] meteo;
		long timestamp = 1394193600000L;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("your time stamp correspond to : ");
		System.out.println(dateFormat.format(timestamp));
		meteo = getMeteo(pathFolder, timestamp);
		System.out.println("the closest line : ");
		for(String str : meteo){
			System.out.print(str+"|");
		}
	}
}
