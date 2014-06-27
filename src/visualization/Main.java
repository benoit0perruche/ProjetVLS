package visualization;
import java.awt.Color;
import java.awt.Dimension;

public class Main {
	public static void main(String[] args) {
//		String path = "/home/ricky/VLS/Washington/";
//		int[][] matrice = Demande.matriceDemandeSommeLignesColonnes(
//				path + "2014-1st-quarter_FormatOk.csv",	//file for the first quarter 2014
////				path + "Washington_Trips_7-3-2014(copie)_FormatOk.csv",	//file for 7th March
//				path + "capital-bikeshare_12-06-14_04h41_Static.json");
//		int max = OutilsDemande.getMaxMatrice(matrice);
		
		
		JCanvas jc = new JCanvas();
		Legend legend = new Legend();
		jc.setBackground(Color.WHITE);
		jc.setPreferredSize(new Dimension(969,889));
		legend.setPreferredSize(new Dimension(80,889)); //(width,height)
		GuiHelper.showOnFrame(jc, legend, "Popularity - Washington - Capital Bike Share");
	}
}
