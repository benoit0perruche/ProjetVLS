package dataRecovery;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class Truck {

	/**
	 * find the trips where a truck trip is probably hidden.
	 * Typically they are trips where the start destination of a bike is
	 * different than its end destination of its previous trip.
	 * @param tripsStr all the trips obtained after parsing a trip history file
	 * and sorting it by bikeId
	 * @return list a trips where a truck trip is probably hidden 
	 */
	public static List<String[]> findTruck(List<String[]> tripsStr) {
		List<String[]> trucksStr = new ArrayList<String[]>();

		for (int i = 1; i < (tripsStr.size() - 1); i++) {

			if ((tripsStr.get(i)[7]).equals(tripsStr.get(i + 1)[7])
					&& !(tripsStr.get(i)[6].equals(tripsStr.get(i + 1)[3]))) {

				trucksStr.add(tripsStr.get(i));
				trucksStr.add(tripsStr.get(i + 1));

			}

		}
		return trucksStr;
	}

	/**
	 * @param date
	 * @return
	 */
	public static int[] FormatDate(String date) {
		int nombre;
		String chaine1, chaine2;
		int[] dateTableau = null;
		dateTableau = new int[5];

		// Couper la date en deux parties : date et horaire
		StringTokenizer st1 = new StringTokenizer(date, " ");
		chaine1 = st1.nextToken();
		chaine2 = st1.nextToken();

		// Retrouver le jour, le mois et l'année de la date
		StringTokenizer st2 = new StringTokenizer(chaine1, "/");
		nombre = Integer.parseInt(st2.nextToken());
		dateTableau[2] = nombre;
		nombre = Integer.parseInt(st2.nextToken());
		dateTableau[1] = nombre;
		nombre = Integer.parseInt(st2.nextToken());
		dateTableau[0] = nombre;

		// Retrouver l'heure et les minutes de la date
		StringTokenizer st3 = new StringTokenizer(chaine2, ":");
		nombre = Integer.parseInt(st3.nextToken());
		dateTableau[3] = nombre;
		nombre = Integer.parseInt(st3.nextToken());
		dateTableau[4] = nombre;

		return dateTableau;
	}

	/**
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static String CalcDuration(String start, String end)
			throws ParseException {

		int[] date1;
		int[] date2;
		date1 = FormatDate(start);
		date2 = FormatDate(end);

		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.set(date1[0], date1[1], date1[2], date1[3], date1[4]);
		calendar2.set(date2[0], date2[1], date2[2], date2[3], date2[4]);
		long milliseconds1 = calendar1.getTimeInMillis();
		long milliseconds2 = calendar2.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		long diffSeconds = diff / 1000;
		// System.out.println("Time in seconds: " + diffSeconds
		// + " seconds.");

		long diffMinutes = diff / (60 * 1000);
		diffSeconds = diffSeconds - diffMinutes * 60;
		long diffHours = diff / (60 * 60 * 1000);
		diffMinutes = diffMinutes - diffHours * 60;
		return Long.toString(diffHours) + "h " + Long.toString(diffMinutes)
				+ "m " + Long.toOctalString(diffSeconds) + "s ";

	}

	/**
	 * @param trucksStr the list of trips where a truck trip
	 * is probably hidden 
	 * @return List<String[]> of trips made by trucks
	 * @throws ParseException
	 */
	public static List<String[]> createTruckTrips(List<String[]> trucksStr)
			throws ParseException {
		List<String[]> truckTripsStr = new ArrayList<String[]>();
		String[] line = new String[9];
		String[] lineStr = new String[9];

		for (int i = 0; i < (trucksStr.size() - 1); i = i + 2) {

			line[0] = CalcDuration(trucksStr.get(i)[4], trucksStr.get(i + 1)[1]);
			line[1] = trucksStr.get(i)[4];
			line[2] = trucksStr.get(i)[5];
			line[3] = trucksStr.get(i)[6];
			line[4] = trucksStr.get(i + 1)[1];
			line[5] = trucksStr.get(i + 1)[2];
			line[6] = trucksStr.get(i + 1)[3];
			line[7] = trucksStr.get(i)[7];
			line[8] = "Truck";

			lineStr = new String[] { line[0], line[1], line[2], line[3],
					line[4], line[5], line[6], line[7], line[8] };

			truckTripsStr.add(lineStr);

		}

		return truckTripsStr;
	}

}
