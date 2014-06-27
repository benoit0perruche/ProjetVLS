package dataRecovery;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Parser {

	//	private static Member CASUAL;
	//	private static Member REGISTRED;
	//	private static Member TRUCK;


	/** parse a csv file
	 * @param path the path of the file to parse
	 * @return the data in a List<String[]>, the element of the list are 
	 * the lines in String[], the element of the String[] are each String of the line beetween ","
	 * @throws IOException
	 */
	public static List<String[]> parse(String path) throws IOException{
		List<String[]> tripsStr = new ArrayList<String[]>();
		try
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
			String chaine;
			int i = 1; // To avoid reading the first line (titles)

			while((chaine = bufferedReader.readLine())!= null)
			{
				if(i > 1)
				{
					String[] tabChaine = chaine.split(",");
					tripsStr.add(tabChaine);
				}
				i++;
			}

			bufferedReader.close();                  
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File unfindable.");
		}
		return tripsStr; 
	}

	/**
	 * @param path the path of the file to parse (trip history in csv)
	 * @param idStation the id of the station requested
	 * @return the line where the idstation is the start of the trip
	 * @throws IOException
	 */
	public static List<String[]> parseStart(String path, String idStation) throws IOException{
		List<String[]> tripsStr = new ArrayList<String[]>();
		try
		{
			BufferedReader fichier_source = new BufferedReader(new FileReader(path));
			String chaine;
			int i = 1; // To avoid reading the first line (titles)

			while((chaine = fichier_source.readLine())!= null)
			{
				if(i > 1)
				{
					String[] tabChaine = chaine.split(",");
					if (tabChaine[2].equals(idStation)){
						tripsStr.add(tabChaine);
					}
				}
				i++;
			}

			fichier_source.close();                  
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File unfindable.");
		}
		return tripsStr; 
	}

	/**
	 * @param path the path of the file to parse (trip history in csv)
	 * @param idStation the id of the station requested
	 * @return the line where the idstation is the end of the trip
	 * @throws IOException
	 */
	public static List<String[]> parseEnd(String path, String idStation) throws IOException{
		List<String[]> tripsStr = new ArrayList<String[]>();
		try
		{
			BufferedReader fichier_source = new BufferedReader(new FileReader(path));
			String chaine;
			int i = 1; // To avoid reading the first line (titles)

			while((chaine = fichier_source.readLine())!= null)
			{
				if(i > 1)
				{
					String[] tabChaine = chaine.split(",");
					if (tabChaine[4].equals(idStation)){
						tripsStr.add(tabChaine);
					}
				}
				i++;
			}

			fichier_source.close();                  
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File unfindable.");
		}
		return tripsStr; 
	}

	/**
	 * @param path
	 * @param idVelo
	 * @return List<String[]> if this bike trips
	 * @throws IOException
	 */
	public static List<String[]> parse(String path, String idVelo) throws IOException{
		List<String[]> tripsStr = new ArrayList<String[]>();
		try
		{
			BufferedReader fichier_source = new BufferedReader(new FileReader(path));
			String chaine;
			int i = 1; // To avoid reading the first line (titles)

			while((chaine = fichier_source.readLine())!= null)
			{
				if(i > 1)
				{
					String[] tabChaine = chaine.split(",");
					if (tabChaine[5].equals(idVelo)){
						tripsStr.add(tabChaine);
					}
				}
				i++;
			}

			fichier_source.close();                 
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File unfindable.");
		}
		return tripsStr;
	}


	/**
	 * @param path he path of the file to parse (trip history in csv)
	 * @return the number of Station where trips are made
	 * @throws Exception
	 */
	static int nbStations(String path) throws Exception{
		int nbStations = 0;
		int nbStationsStart = 0;
		int nbStationsEnd = 0;
		String station = "";

		Sort.sort(path,path+"temp",3);
		try
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path+"temp"));
			String chaine;
			int i = 1; // To avoid reading the first line (titles)

			while((chaine = bufferedReader.readLine())!= null)
			{
				if(i > 1)
				{
					String[] tabChaine = chaine.split(",");
					if (!station.equals(tabChaine[3])){
						station = tabChaine[3];
						nbStationsStart++;
					}

				}
				i++;
			}

			bufferedReader.close();                  
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File unfindable.");
		}

		Sort.sort(path,path+"temp",6);

		try
		{
			BufferedReader bufferedReader = new BufferedReader(new FileReader(path+"temp"));
			String chaine;
			int i = 1; // To avoid reading the first line (titles)

			while((chaine = bufferedReader.readLine())!= null)
			{
				if(i > 1)
				{
					String[] tabChaine = chaine.split(",");
					if (!station.equals(tabChaine[6])){
						station = tabChaine[6];
						nbStationsEnd++;
					}

				}
				i++;
			}

			bufferedReader.close();                  
		}
		catch (FileNotFoundException e)
		{
			System.out.println("File unfindable.");
		}

		deleteFile(path+"temp");

		nbStations = Math.max(nbStationsStart,nbStationsEnd);

		return nbStations;
	}

	//	private static List<Trip> dataToTrips(List<String[]> data) {
	//		final List<Trip> trips = new ArrayList<Trip>();
	//
	//		for (String[] str : tripsStr) {
	//			final String duration = str[0];
	//			final String startDate = str[1];
	//			final String startStation = str[2];
	//			final String startTerminalStr = str[3];
	//			final String endDate = str[4];
	//			final String endStation = str[5];
	//			final String endTerminalStr = str[6];
	//			final String bikeNumber = str[7];
	//			final String memberStr = str[8];
	//
	//			final Integer startTerminal = Integer.parseInt(startTerminalStr);
	//			final Integer endTerminal = Integer.parseInt(endTerminalStr);
	//
	//			final Member member ;
	//			if (memberStr == "Casual"){member = CASUAL;}
	//			else if (memberStr == "Registred"){member = REGISTRED;}
	//			else {member = TRUCK;}
	//
	//			final Trip trip = new Trip(duration, startDate, startStation, startTerminal, 
	//					endDate, endStation, endTerminal, bikeNumber, member);
	//			trips.add(trip);
	//		}
	//
	//		return trips;
	//
	//	}

	/**
	 * write a csv file with the truck trips
	 * @param truckTripsStr a List<String[]> of trips performed by truck in rebalancing goal
	 * @param Path the path where the file is saved
	 * @throws IOException
	 */
	public static void writeTruckTrips(List<String[]> truckTripsStr,String Path) throws IOException{
		FileWriter writer = new FileWriter(Path);

		writer.write("Duration,Start date,Start Station,Start terminal,End date,End Station,End terminal,Bike#,Member Type\n");

		for(String[] str : truckTripsStr){
			for (int j = 0; j < 8; j++){
				writer.write(str[j]+",");
			}
			writer.write(str[8]);
			writer.write("\n");
		}
		writer.close();	
	}

	/** 
	 * delete a file
	 * @param path the file to delete
	 */
	public static void deleteFile(String path){
		File file = new File(path);
		boolean success = file.delete();
		if (!success)
			throw new IllegalArgumentException("Delete: deletion failed");
	}

	public static void main(String[] arguments) throws Exception {
		List<String[]> tripsStr = new ArrayList<String[]>();
		//		List<Trip> trips = new ArrayList<Trip>();
		List<String[]> trucksStr = new ArrayList<String[]>();
		List<String[]> truckTripsStr = new ArrayList<String[]>();
		String RESOURCES_PATH = "/home/ricky/VLS/Washington/";
		String TRIPS_FILE_NAME = "Washington_Trips_7-3-2014(copie)";

		Sort.sort(RESOURCES_PATH+TRIPS_FILE_NAME+".csv",
				RESOURCES_PATH+TRIPS_FILE_NAME+"_sorted.csv",
				7);
		tripsStr = parse(RESOURCES_PATH+TRIPS_FILE_NAME+"_sorted.csv");
		deleteFile(RESOURCES_PATH+TRIPS_FILE_NAME+"_sorted.csv");

		trucksStr = Truck.findTruck(tripsStr);
		truckTripsStr = Truck.createTruckTrips(trucksStr);
		writeTruckTrips(truckTripsStr,RESOURCES_PATH+TRIPS_FILE_NAME+"_Truck.csv");

		Sort.sort(RESOURCES_PATH+TRIPS_FILE_NAME+"_Truck.csv",
				RESOURCES_PATH+TRIPS_FILE_NAME+"_Truck_sorted_temp.csv",
				6);
		deleteFile(RESOURCES_PATH+TRIPS_FILE_NAME+"_Truck.csv");

		Sort.sort(RESOURCES_PATH+TRIPS_FILE_NAME+"_Truck_sorted_temp.csv",
				RESOURCES_PATH+TRIPS_FILE_NAME+"_Truck_sorted.csv",
				3);
		deleteFile(RESOURCES_PATH+TRIPS_FILE_NAME+"_Truck_sorted_temp.csv");
		//trips = dataToTrips(tripsStr);

		/*TEST*/			
		//		for(String[] str : truckTripsStr){
		//			for (int j = 0; j < 8; j++){
		//				System.out.print(str[j]+",");
		//			}
		//			System.out.print(str[8]);
		//			System.out.println();
		//		}
		//		System.out.println(tripsStr.size());
		//		System.out.println(trucksStr.size());
		//		System.out.println(truckTripsStr.size());

	}		
}

