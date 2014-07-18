package dataRecovery;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.json.simple.parser.ParseException;


public class RealTime {

	/** try to establish a connection, retry if it does not work
	 * @param url a string used as url
	 * @return an URLConnection
	 * @throws MalformedURLException 
	 */
	public static URLConnection establishConnection(String url) throws MalformedURLException {
		URL u = new URL(url);
		URLConnection urlConnection = null;
		try {
			urlConnection = u.openConnection();
		} catch (IOException e) {
			// connection failed, try again.
			try { Thread.sleep(1000); } catch (InterruptedException e1) {};

			establishConnection(url);
		}
		return urlConnection;
	}


	/**
	 * get a file from an URL
	 * @param url the url
	 * @param pathOut name givent to the file (path)
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void getFile(String url, String pathOut) throws IOException, InterruptedException {

		URLConnection urlConnection = null;
		urlConnection = establishConnection(url);
		int size = urlConnection.getContentLength();
		if (size <= 0){
			Thread.sleep(60000);
			getFile( url, pathOut);
		}
		InputStream inputStream = urlConnection.getInputStream();
		InputStream bufferedInputStream = new BufferedInputStream(inputStream);	
		byte[] data = new byte[size];
		int readOctects = 0;
		int move = 0; float alreadyRead = 0;

		while(move < size){
			readOctects = bufferedInputStream.read(data, move, (data.length - move));
			alreadyRead = alreadyRead + readOctects;
			if(readOctects == -1) break; //end of file
			move += readOctects;
		}

		bufferedInputStream.close();
		FileOutputStream fichierSortie = new FileOutputStream(pathOut);
		fichierSortie.write(data);
		fichierSortie.flush(); fichierSortie.close();
	}

	//	/**
	//	 * get a file from an URL
	//	 * @param url the url
	//	 * @param pathOut name givent to the file (path)
	//	 * @throws IOException
	//	 * @throws InterruptedException
	//	 */
	//	public static void getFile(String url, String pathOut) throws IOException, InterruptedException {
	//
	//		URL u = new URL(url); // velib http://api.citybik.es/v2/networks/velib
	//		URLConnection urlConenction = null;
	//		try {
	//			urlConenction = u.openConnection();
	//		} catch (IOException e) {
	//			// Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		int size = urlConenction.getContentLength();
	//		InputStream inputStream = urlConenction.getInputStream();
	//		InputStream bufferedInputStream = new BufferedInputStream(inputStream);	
	//		byte[] data = new byte[size];
	//		int readOctects = 0;
	//		int move = 0; float alreadyRead = 0;
	//
	//		while(move < size){
	//			readOctects = bufferedInputStream.read(data, move, (data.length - move));
	//			alreadyRead = alreadyRead + readOctects;
	//			if(readOctects == -1) break; //end of file
	//			move += readOctects;
	//		}
	//
	//		bufferedInputStream.close();
	//		FileOutputStream fichierSortie = new FileOutputStream(pathOut);
	//		fichierSortie.write(data);
	//		fichierSortie.flush(); fichierSortie.close();
	//	}

	/** to call the right method of parsing depending of the cityInt
	 * @param cityInt the cityInt
	 * @param path the path to the file to parse
	 * @return the content of the file in a double[][]
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static double[][] parserStat(int cityInt, String path) throws FileNotFoundException, IOException, ParseException{
		double[][] tab = null;
		int cityFormat = City.getCityFormat(cityInt);
		if (cityFormat==-1){tab = FormatDefaut.parserStat(path);}
		else if (cityFormat==0){tab = FormatPBSC1.parserStat(path);}
		else if (cityFormat==1){tab = FormatPBSC2.parserStat(path);}
		else {tab = FormatJCD.parserStat(path);}
		return tab;
	}

	/** to call the right method of parsing depending of the cityInt
	 * @param cityInt the cityInt
	 * @param path the path to the file to parse
	 * @return the content of the file in a double[][]
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static double[][] parserDyn(int cityInt, String path) throws FileNotFoundException, IOException, ParseException{
		double[][] tab = null;
		int cityFormat = City.getCityFormat(cityInt);
		if (cityFormat==-1){tab = FormatDefaut.parserDyn(path);}
		else if (cityFormat==0){tab = FormatPBSC1.parserDyn(path);}
		else if (cityFormat==1){tab = FormatPBSC2.parserDyn(path);}
		else {tab = FormatJCD.parserDyn(path);}
		return tab;
	}

	public static void main(String[] args) throws IOException, InterruptedException, ParseException {

		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
		DateFormat hourFormat = new SimpleDateFormat("HH'h'mm");

		long now = 0;
		long testDay = 0; 

		long timeZone = 0;

		String date = "";		
		String testDate = "";	
		String hourBeg = "";	//timestamp
		String hourEnd = "";	//timestamp


		String urlBeg = "http://api.citybik.es/v2/networks/";
		String citySystem = "";

		double[][] stat = null;
		double[][] dyn = null;

		long t1,t2,t3 = 0;

		String citiesString = 
				" 0 - New York - Citi Bike NYC\n" +
						" 1 - Chicago - Divvy\n" +
						" 2 - San Francisco and others - Bay Area Bike Share\n" +
						" 3 - Chattanooga - Bike Chattanooga\n" +
						" 4 - Boston - Hubway\n" +
						" 5 - Washington - Capital Bikeshare\n" +
						" 6 - Melbourne - Melbourne Bikeshare\n" +
						" 7 - Toronto - Bixi Toronto\n" +
						" 8 - Montreal - Bixi Montreal\n" +
						" 9 - Saint-Paul, Minneapolis - Nice Ride\n" +
						"10 - Ottawa, Gatineau - Capital Bixi\n" +
						"11 - Londres - Barclays Cycle Hire\n" +
						"12 - Paris - Velib\n" +
						"13 - Nantes - Bicloo\n" +
						"14 - Namur - Li bia velo\n" +
						"15 - Santander - Tusbic\n" +
						"16 - Bruxelles - villo\n" +
						"17 - Seville - SEVICI\n" +
						"18 - Valence - Valenbisi\n" +
						"19 - Amiens - Velam\n" +
						"20 - Besancon - VéloCité\n" +
						"21 - Cergy-Pontoise - Velo2\n" +
						"22 - Creteil - Cristolib\n" +
						"23 - Lyon - Vélo'V\n" +
						"24 - Marseille - Le vélo\n" +
						"25 - Nancy - vélOstan'lib\n" +
						"26 - Rouen - cy'clic\n" +
						"27 - Toulouse - Vélô\n" +
						"28 - Toyama - cyclocity\n" +
						"29 - Luxembourg - Veloh\n" +
						"30 - Kazan - Veli'k\n" +
						"31 - Goteborg - Göteborg\n" +
						"32 - Ljubljana - Bicikelj\n" +
						"33 - Dublin - dublinbikes\n";

		if (args.length!=1){
			System.out.println("Usage : put number of a city in argument.");
			System.out.println(citiesString);
		}

		int cityInt = 5;
		if (args.length!=0){
			cityInt = Integer.parseInt(args[0]);
		}

		timeZone = City.getCityTimeZone(cityInt);
		citySystem = City.getCitySystemString(cityInt);

		//create a folder
		File folder = new File (citySystem);
		folder.mkdirs();

		while(true){

			t1 = System.currentTimeMillis();	//start the timer (hour)
			t2 = System.currentTimeMillis();	//start the timer (minute)

			now = System.currentTimeMillis();	//timestamp 
			now = now + timeZone*3600000;		//get the local time
			date = dateFormat.format(now);		//timestamp
			hourBeg = hourFormat.format(now);	//timestamp

			//get the file
			getFile(urlBeg+citySystem,citySystem+"/"+citySystem+"_station_feed.json");
			//convert
			//parse and write the static part
			stat = parserStat(cityInt,citySystem+"/"+citySystem+"_station_feed.json");
			Format.writeStat(stat,citySystem+"/"+citySystem+"_"+date+"_"+hourBeg+"_Static.json");
			//parse and write the dynamic part
			dyn = parserDyn(cityInt,citySystem+"/"+citySystem+"_station_feed.json");//TODO
			Format.writeDyn(dyn,citySystem+"/"+citySystem+"_dyna1.json");

			//wait one minute between two get
			t3 = System.currentTimeMillis();
			while(t3 - t2 < 60000){	
				Thread.sleep(3000);
				t3 = System.currentTimeMillis();
			}


			while(t2 - t1 < 3540000){ //while(t2 - t1 < 3420000){ 

				t2 = System.currentTimeMillis();	//start the timer (minute)

				//get the second file
				getFile(urlBeg+citySystem,citySystem+"/"+citySystem+"_station_feed.json");
				//convert
				//parse and write the dynamic part
				try{
					dyn = parserDyn(cityInt,citySystem+"/"+citySystem+"_station_feed.json");//TODO
					Format.writeDyn(dyn,citySystem+"/"+citySystem+"_dyna2.json");

					FormatDefaut.merge(citySystem+"/"+citySystem+"_dyna1.json",
							citySystem+"/"+citySystem+"_dyna2.json",
							citySystem+"/"+citySystem+"_dyna3.json");
					FormatDefaut.deleteDoubles(citySystem+"/"+citySystem+"_dyna3.json");
				}
				catch(IOException e){
					System.out.println(e);
				}

				//wait one minute between two get
				t3 = System.currentTimeMillis();
				while(t3 - t2 < 60000){
					Thread.sleep(3000);
					t3 = System.currentTimeMillis();
				}	

				t2 = System.currentTimeMillis();	//start the timer (minute)

				//get file
				getFile(urlBeg+citySystem,citySystem+"/"+citySystem+"_station_feed.json");
				//convert
				//parse and write the dynamic part
				try{
					dyn = parserDyn(cityInt,citySystem+"/"+citySystem+"_station_feed.json");//TODO
					Format.writeDyn(dyn,citySystem+"/"+citySystem+"_dyna2.json");

					FormatDefaut.merge(citySystem+"/"+citySystem+"_dyna3.json",
							citySystem+"/"+citySystem+"_dyna2.json",
							citySystem+"/"+citySystem+"_dyna1.json");
					FormatDefaut.deleteDoubles(citySystem+"/"+citySystem+"_dyna1.json");
				}
				catch(IOException e){
					System.out.println(e);
				}
				//break if the day ends
				testDay = System.currentTimeMillis();
				testDay = testDay + timeZone*3600000;	//get the local time
				testDate = dateFormat.format(testDay);
				if (!(testDate.equals(date))) break;

				//wait one minute between two get
				t3 = System.currentTimeMillis();
				while(t3 - t2 < 60000){
					Thread.sleep(3000);
					t3 = System.currentTimeMillis();
				}	

			}

			now = System.currentTimeMillis();	//timestamp
			now = now + timeZone*3600000;		//get the local time
			hourEnd = hourFormat.format(now);	//timestamp

			t2 = System.currentTimeMillis();	//start the timer (minute)

			//get the last file of the hour
			getFile(urlBeg+citySystem,citySystem+"/"+citySystem+"_station_feed.json");
			//convert
			//parse and write the dynamic part
			dyn = parserDyn(cityInt,citySystem+"/"+citySystem+"_station_feed.json");
			Format.writeDyn(dyn,citySystem+"/"+citySystem+"_dyna2.json");

			FormatDefaut.merge(citySystem+"/"+citySystem+"_dyna1.json",
					citySystem+"/"+citySystem+"_dyna2.json",
					citySystem+"/"+citySystem+"_"+date+"_"+hourBeg+"_"+hourEnd+"_dyna.json");
			FormatDefaut.deleteDoubles(citySystem+"/"+citySystem+"_"+date+"_"+hourBeg+"_"+hourEnd+"_dyna.json");		


		}
	}
}
