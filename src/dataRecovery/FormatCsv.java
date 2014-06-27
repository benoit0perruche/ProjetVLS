package dataRecovery;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
        	
        	oneData[0] = trip[0];        	
        	oneData[1] = dateOk(trip[1]);
        	oneData[2] = trip[3];
        	oneData[3] = dateOk(trip[4]);
        	oneData[4] = trip[6];
        	oneData[5] = trip[7];
        	oneData[6] = trip[8];
        		
//        	//without duration
//        	oneData = new String[6];
//        	oneData[0] = dateOk(trip[1]);        	
//        	oneData[1] = trip[3];
//        	oneData[2] = dateOk(trip[4]);
//        	oneData[3] = trip[6];
//        	oneData[4] = trip[7];
//        	oneData[5] = trip[8];


        	// Ajout du tableau concernant un trajet
        	data.add(oneData);
        }
        
        // Creation du fichier resultat avec le bon format
        FileWriter writer = new FileWriter(pathOut);
        // Ecriture de l'entete du fichier
    	writer.write("Duration,Start date,Start terminal,End date,End terminal,Bike#,Member Type\n");

//    	//without duration
//    	writer.write("Start date,Start terminal,End date,End terminal,Bike#,Member Type\n");
    	
    	// Ecriture de tous les trajets
    	for(int j = 1; j<data.size(); j++){
    		for(int i = 0; i<6; i++){
    			writer.write(data.get(j)[i]+",");
    		}
    		writer.write(data.get(j)[6]);
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
    
//	public static void main(String[] arguments) throws Exception {
//		formaterFichierCsv("P:/Washington_Trajets/Washington_Trajets_7-3-2014_Trajets-Bike+Truck.csv", 
//			"P:/Washington_Trajets/Washington_Trajets_7-3-2014_Trajets-Bike+Truck_FormatOk.csv");
//	}
}
