package dataRecovery;

public class City {

	/**
	 * @param cityInt the int that define the city
	 * @return the string used to capture the station feed and to create the folder
	 * where the captured feeds are.
	 */
	public static String getCitySystemString (int cityInt){
		String citySystem = "";

		switch (cityInt) {
		case -1:  citySystem = "";
		break;
		case 0:  citySystem = "citi-bike-nyc";
		break;
		case 1:  citySystem = "divvy";
		break;
		case 2:  citySystem = "bay-area-bike-share";
		break;
		case 3:  citySystem = "bike-chattanooga";
		break;
		case 4:  citySystem = "hubway";
		break;
		case 5:  citySystem = "capital-bikeshare";
		break;
		case 6:  citySystem = "melbourne-bike-share";
		break;
		case 7:  citySystem = "bixi-toronto";
		break;
		case 8:  citySystem = "bixi-montreal";
		break;
		case 9:  citySystem = "nice-ride";
		break;
		case 10:  citySystem = "capital-bixi";
		break;
		case 11:  citySystem = "barclays-cycle-hire";
		break;
		case 12:  citySystem = "velib";
		break;
		case 13:  citySystem = "bicloo";
		break;
		case 14:  citySystem = "li-bia-velo";
		break;
		case 15:  citySystem = "tusbic";
		break;
		case 16:  citySystem = "villo";
		break;
		case 17:  citySystem = "sevici";
		break;
		case 18:  citySystem = "valenbisi";
		break;
		case 19:  citySystem = "velam";
		break;
		case 20:  citySystem = "velocite-besancon";
		break;
		case 21:  citySystem = "velo2";
		break;
		case 22:  citySystem = "cristolib";
		break;
		case 23:  citySystem = "velov";
		break;
		case 24:  citySystem = "le-velo";
		break;
		case 25:  citySystem = "velostanlib";
		break;
		case 26:  citySystem = "cyclic";
		break;
		case 27:  citySystem = "velo";
		break;
		case 28:  citySystem = "toyama-cyclocity";
		break;
		case 29:  citySystem = "veloh";
		break;
		case 30:  citySystem = "velik";
		break;
		case 31:  citySystem = "goeteborg";
		break;
		case 32:  citySystem = "bicikelj";
		break;
		case 33:  citySystem = "dublinbikes"; 
		break;
		}

		return citySystem;

	}

	/**
	 * @param cityInt the int that define the city
	 * @return the city format that correspond to the format of the json:
	 * 0 for PBSC1
	 * 1 for PBSC2
	 * 2 for JCD
	 */
	public static int getCityFormat(int cityInt){
		int cityFormat = -1;

		switch (cityInt) {	
		case -1:  cityFormat = -1;
		break;
		case 0:  cityFormat = 0;
		break;
		case 1:  cityFormat = 0;
		break;
		case 2:  cityFormat = 0;
		break;
		case 3:  cityFormat = 0;
		break;
		case 4:  cityFormat = 1;
		break;
		case 5:  cityFormat = 1;
		break;
		case 6:  cityFormat = 1;
		break;
		case 7:  cityFormat = 1;
		break;
		case 8:  cityFormat = 1;
		break;
		case 9:  cityFormat = 1;
		break;
		case 10:  cityFormat = 1;
		break;
		case 11:  cityFormat = 1;
		break;
		case 12:  cityFormat = 2;
		break;
		case 13:  cityFormat = 2;
		break;
		case 14:  cityFormat = 2;
		break;
		case 15:  cityFormat = 2;
		break;
		case 16:  cityFormat = 2;
		break;
		case 17:  cityFormat = 2;
		break;
		case 18:  cityFormat = 2;
		break;
		case 19:  cityFormat = 2;
		break;
		case 20:  cityFormat = 2;
		break;
		case 21:  cityFormat = 2;
		break;
		case 22:  cityFormat = 2;
		break;
		case 23:  cityFormat = 2;
		break;
		case 24:  cityFormat = 2;
		break;
		case 25:  cityFormat = 2;
		break;
		case 26:  cityFormat = 2;
		break;
		case 27:  cityFormat = 2;
		break;
		case 28:  cityFormat = 2;
		break;
		case 29:  cityFormat = 2;
		break;
		case 30:  cityFormat = 2;
		break;
		case 31:  cityFormat = 2;
		break;
		case 32:  cityFormat = 2;
		break;
		case 33:  cityFormat = 2; 
		break;
		}
		return cityFormat;
	}

	/**
	 * @param cityInt the int that define the city
	 * @return the lag between UTC and the city, in hour.
	 */
	public static long getCityTimeZone (int cityInt){
		int cityTimeZone = 0;

		switch (cityInt) {	
		case -1:  cityTimeZone = 0;
		break;
		case 0:  cityTimeZone = -5;
		break;
		case 1:  cityTimeZone = -5;
		break;
		case 2:  cityTimeZone = -8;
		break;
		case 3:  cityTimeZone = -5;
		break;
		case 4:  cityTimeZone = -5;
		break;
		case 5:  cityTimeZone = -5;
		break;
		case 6:  cityTimeZone = 10;
		break;
		case 7:  cityTimeZone = -5;
		break;
		case 8:  cityTimeZone = -5;
		break;
		case 9:  cityTimeZone = -6;
		break;
		case 10:  cityTimeZone = -5;
		break;
		case 11:  cityTimeZone = 0;
		break;
		case 12:  cityTimeZone = 1;
		break;
		case 13:  cityTimeZone = 1;
		break;
		case 14:  cityTimeZone = 1;
		break;
		case 15:  cityTimeZone = 1;
		break;
		case 16:  cityTimeZone = 1;
		break;
		case 17:  cityTimeZone = 1;
		break;
		case 18:  cityTimeZone = 1;
		break;
		case 19:  cityTimeZone = 1;
		break;
		case 20:  cityTimeZone = 1;
		break;
		case 21:  cityTimeZone = 1;
		break;
		case 22:  cityTimeZone = 1;
		break;
		case 23:  cityTimeZone = 1;
		break;
		case 24:  cityTimeZone = 1;
		break;
		case 25:  cityTimeZone = 1;
		break;
		case 26:  cityTimeZone = 1;
		break;
		case 27:  cityTimeZone = 1;
		break;
		case 28:  cityTimeZone = 9; // no DST in Japan !
		break;
		case 29:  cityTimeZone = 1;
		break;
		case 30:  cityTimeZone = 4;
		break;
		case 31:  cityTimeZone = 1;
		break;
		case 32:  cityTimeZone = 1;
		break;
		case 33:  cityTimeZone = 0; 
		break;
		}

		return cityTimeZone;
	}
}
