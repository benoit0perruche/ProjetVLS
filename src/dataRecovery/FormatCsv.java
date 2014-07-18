package dataRecovery;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.json.simple.parser.ParseException;

public class FormatCsv {

	/**
	 * Obtenir le bon format pour la date de depart, la date d'arrivee et la duree
	 * @param pathIn le fichier csv d'entree
	 * @param pathOut le fichier csv de sortie au bon format 
	 * @throws IOException
	 */
	public static void formaterFichierCsv(String pathIn, String pathOut) throws IOException{
		List<String[]> raw = Parser.parse(pathIn);
		List<String[]> data = new ArrayList<String[]>();

		// Tableau de string contenant les informations d'un trajet
		String[] oneData = null;

		// On parcourt tous les trajets du fichier
		for (String[] trip : raw) {

			oneData = new String[7];

			oneData[0] = durationOk(trip[0]);       	
//			oneData[1] = dateOk(trip[1]);
			oneData[1] = trip[1];
			oneData[2] = trip[3];
//			oneData[3] = dateOk(trip[4]);
			oneData[3] = trip[4];
			oneData[4] = trip[6];
			oneData[5] = trip[7];
			oneData[6] = trip[8];

			// Ajout du tableau concernant un trajet
			data.add(oneData);
		}

		// Creation du fichier resultat avec le bon format
		FileWriter writer = new FileWriter(pathOut);
		// Ecriture de l'entete du fichier
		writer.write("Duration,Start date,Start terminal,End date,End terminal,Bike#,Member Type\n");

		// Ecriture de tous les trajets
		for (String[] trip : data) {
			for(int i = 0; i<6; i++){
				writer.write(trip[i]+",");
			}
			writer.write(trip[6]);
			writer.write("\n");
		}
		writer.close();

	}

	public static String dateOk(String dateTrajet){
		String chaine1 = null;
		String chaine2 = null;
		String s2 = null;
		String[] dateTableau = new String[5];
		// Remplissage de la colonne "Start date"
		// On regarde si la ligne de trajet courante est la ligne d'entete du fichier
		if((dateTrajet.equals("Start date")) || (dateTrajet.equals("End date"))){
			return dateTrajet;
		}
		else {
			// On regarde si le nombre de caracteres de la string "start date" ou "end date" est egal e 16. 
			// Si ce n'est pas le cas, c'est qu'il manque des "zeros" pour les heures, jours ou mois
			if(dateTrajet.length()<16){
				// Couper la date en deux parties : date et horaire
				StringTokenizer st2 = new StringTokenizer(dateTrajet," ");
				chaine1 = st2.nextToken(); // contenant la partie date
				chaine2 = st2.nextToken(); // contenant la partie horaire

				// Retrouver le mois, le jour et l'annee
				StringTokenizer st3 = new StringTokenizer(chaine1,"/");

				//On regarde si la partie "date" contient bien 10 caracteres.
				// Si ce n'est pas le cas, il faut ajouter un "zero" au jour et/ou au mois
				if(chaine1.length()<10){
					s2 = st3.nextToken();
					//On regarde si le mois est compose de deux chiffres. Si ce n'est pas le cas, on ajoute un "zero"
					if(s2.length()==1){
						dateTableau[0] = "0"+s2;
					}
					else{
						dateTableau[0] = s2;
					}

					s2 = st3.nextToken();

					// On regarde si le jour est compose de deux chiffres. Si ce n'est pas le cas, on ajoute un "zero".
					if(s2.length()==1){
						dateTableau[1] = "0"+s2;
					}
					else {
						dateTableau[1] = s2;
					}
					s2 = st3.nextToken();
					dateTableau[2] = s2;
				}

				// Cas oe le jour et le mois sont bien composes de deux chiffres et l'annee de quatre chiffres
				else {
					dateTableau[2] = st2.nextToken();
					dateTableau[1] = st2.nextToken();
					dateTableau[0] = st2.nextToken();
				}

				// Retrouver l'heure et les minutes de la date
				StringTokenizer st4 = new StringTokenizer(chaine2,":");

				// On regarde si les heures et les minutes sont composes de deux chiffres. 
				// Si ce n'est pas le cas, nous ajoutons un "zero" e l'heure.
				if(chaine2.length()<5){
					dateTableau[3] = "0"+st4.nextToken();
					dateTableau[4] = st4.nextToken();
				}
				// Cas oe les heures et les minutes sont bien composes de deux chiffres
				else {
					dateTableau[3] = st4.nextToken();
					dateTableau[4] = st4.nextToken();
				}

				// On rassemble toutes les parties afin de mettre "start date" au bon format
				return dateTableau[0]+"/"+dateTableau[1]+"/"+dateTableau[2]+" "+dateTableau[3]+":"+dateTableau[4];
			}
			else {
				return dateTrajet;    		
			}
		}
	}

	public static String durationOk(String duration){
		String chaine1 = null;
		String chaine2 = null;
		String chaine3 = null;
		String[] tableauDuree = new String[3];

		if(duration.equals("Duration")){
			return duration;
		}
		else {
			// On regarde si le nombre de caracteres de la string "duration" est egal e 11. 
			// Si ce n'est pas le cas, c'est qu'il manque des "zeros" pour les heures, minutes ou secondes
			if(duration.length()<12){
				// Couper la duree en trois parties : heures, minutes et secondes
				StringTokenizer st2 = new StringTokenizer(duration," ");
				chaine1 = st2.nextToken(); // contenant la partie heure
				chaine2 = st2.nextToken(); // contenant la partie minute
				chaine3 = st2.nextToken(); // contenant la partie seconde

				// On regarde si la partie heure contient bien 3 caracteres (deux chiffres et le caractere "h").
				// Si ce n'est pas le cas, nous ajoutons un "zero" devant
				if(chaine1.length() < 3){
					tableauDuree[0]="0"+chaine1;
				}
				else {
					tableauDuree[0] = chaine1;
				}

				// On regarde si la partie minute contient bien 3 caracteres (deux chiffres et le caractere "m").
				// Si ce n'est pas le cas, nous ajoutons un "zero" devant
				if(chaine2.length() < 3){
					tableauDuree[1]="0"+chaine2;
				}
				else {
					tableauDuree[1] = chaine2;
				}

				// On regarde si la partie seconde contient bien 3 caracteres (deux chiffres et le caractere "s").
				// Si ce n'est pas le cas, nous ajoutons un "zero" devant
				if(chaine3.length() < 3){
					tableauDuree[2]="0"+chaine3;
				}
				else {
					tableauDuree[2] = chaine3;
				}

				// On reconstruit la chaine de caractere correspondant e la duree au bon format
				return tableauDuree[0] + " " + tableauDuree[1] + " " + tableauDuree[2];

			}
			// La chaine de caractere concernant la duree est au bon format
			else{
				return duration;
			}
		}
	}

	/**
	 * Add the distance to a light trip history file
	 * @param pathTrips path to trip history file
	 * @param pathStatic path to static file (config file)
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void addDistance(String pathTrips, String pathStatic) throws FileNotFoundException, IOException, ParseException{
		double[][] stat = FormatDefaut.parserStat(pathStatic);
		double[][] distance = new double[stat.length][stat.length];
		int[] indexStation = new int[stat.length];
		for (int i=0;i<stat.length;i++){
			indexStation[i] = (int) stat[i][0];
			for (int j=0;j<i;j++){
				distance[i][j] = distanceVolOiseau(stat[i][1],stat[i][2],stat[j][1],stat[j][2]);
				distance[j][i] = distance[i][j];
			}
		}
		//create a temp copy of the file
		File file = new File(pathTrips);
		File temp = new File(pathTrips+"temp");
		file.renameTo(temp);	

		FileWriter writer = new FileWriter(pathTrips);
		writer.write("Duration,Start date,Start terminal,End date,End terminal,Bike#,Member Type,Distance\n");

		int index1 = 0;
		int index2 = 0;
		boolean found = false;
		double dist = 0.;

		BufferedReader bufferedReader = new BufferedReader(new FileReader(pathTrips+"temp"));
		String chaine;
		int i = 1; // To avoid reading the first line (titles)

		while((chaine = bufferedReader.readLine())!= null)
		{
			if(i > 1)
			{
				String[] tabChaine = chaine.split(",");
				for(int j = 0 ; j<stat.length; j++){
					if (Integer.parseInt(tabChaine[2]) == indexStation[j]) {
						index1 = j;
						if (found){
							found = false;
							break;
						}
						found = true;
					}
					if (Integer.parseInt(tabChaine[4]) == indexStation[j]) {
						index2 = j;
						if (found){
							found = false;
							break;
						}
						found = true;
					}

				}
				dist = distance[index1][index2];
				index1 = 0;
				index2 = 0;
				for(int k=0;k<tabChaine.length;k++){
					writer.write(tabChaine[k] + ",");
					}
				writer.write((int) dist+"");
				writer.write("\n");
			}
			i++;
		}

		bufferedReader.close();                  
		writer.close();

		temp.delete();
	}

	/**
	 * Calculate the distnce between two points using there latitude-longitude
	 * @param latA first point latitude (degree)
	 * @param lngA first point  longitude (degree)
	 * @param latB second point latitude (degree)
	 * @param lngB second point  longitude (degree)
	 * @return double distance (m)
	 */
	public static double distanceVolOiseau(double latA, double lngA, double latB, double lngB){
		double distance = 0;
		double distanceTemp1;
		double distanceTemp2;

		// The earth is a 6378137m radius sphere
		double earth_radius = 6378137;

		// Latitudes & longitudes in radian
		double rlatA;
		double rlngA;
		double rlatB;
		double rlngB;

		double dlng;
		double dlat;

		rlatA = Math.toRadians(latA);
		rlngA = Math.toRadians(lngA);
		rlatB = Math.toRadians(latB);
		rlngB = Math.toRadians(lngB);

		// Radian diff calculus
		dlng = (rlngB - rlngA) / 2;
		dlat = (rlatB - rlatA) / 2;

		// Distance calculus
		distanceTemp1 = (Math.sin(dlat) * Math.sin(dlat)) + Math.cos(rlatA) * Math.cos(rlatB) * (Math.sin(dlng) * Math.sin(dlng));
		distanceTemp2 = 2 * Math.atan2(Math.sqrt(distanceTemp1), Math.sqrt(1-distanceTemp1));
		distance = (earth_radius * distanceTemp2);

		return distance;
	}

	public static void main(String[] arguments) throws Exception {
		addDistance("/home/ricky/VLS/Washington/2014-1st-quarter(copie).csv","/home/ricky/VLS/Washington/capital-bikeshare_12-06-14_04h41_Static.json");
	}

}
