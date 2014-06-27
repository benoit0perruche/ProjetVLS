package visualizationTools;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ComparaisonDates {
	/**
	 * Comparaison de deux dates date1.compareTo(date2)
	 * @param date1 une date
	 * @param date2 une date
	 * @return -1 si date1 est avant date2
	 * 		   1 si date1 est apr�s date2
	 * 		   0 si date1 est date2 sont �gales
	 */
	public static int comparerDeuxDates(String date1, String date2){
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		Date date1Bis = null;;
		Date date2Bis = null;
		try {
			date1Bis = dateFormat.parse(date1);
			date2Bis = dateFormat.parse(date2);
		} catch (Exception e) {
			System.err.println("date1 et/ou date2 invalide. Format � utiliser : dd/MM/YYYY HH:mm");
			System.err.println(e.getMessage());
		}
		return dateFormat.format(date1Bis).toString().compareTo(dateFormat.format(date2Bis).toString());
	}
	
	public static int getJour (String date){
		String jour =date.charAt(3)+""+date.charAt(4);
		int i = Integer.parseInt(jour);
		return i;
	}
	
	public static int getMois (String date){
		String mois =date.charAt(0)+""+date.charAt(1);
		int i = Integer.parseInt(mois);
		return i;
	}
	
	public static int getAnnee (String date){
		String annee =date.charAt(6)+""+date.charAt(7)+date.charAt(8)+date.charAt(9);
		int i = Integer.parseInt(annee);
		return i;
	}
	
	public static int getHeure (String date){

		String heure = date.charAt(11)+""+date.charAt(12);
		
		int i = Integer.parseInt(heure);
		return i;
	}
	
	public static int getMinute (String date){
		String minute =date.charAt(14)+""+date.charAt(15);
		int i = Integer.parseInt(minute);
		return i;
	}
}
