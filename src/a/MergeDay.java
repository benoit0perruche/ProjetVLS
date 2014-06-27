package a;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import dataRecovery.FormatDefaut;

//test EGit
public class MergeDay {
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException{
		String path = "/home/ricky/VLS/Washington/reserveur/";
//		String fileStart = "capital-bikeshare_0";

		
		FormatDefaut.merge(path+"capital-bikeshare_04-06-14_11h13_12h15_dyna.json",
				path+"capital-bikeshare_04-06-14_12h15_13h16_dyna.json",
				path+"temp1");
		FormatDefaut.merge(path+"capital-bikeshare_04-06-14_13h16_14h18_dyna.json",
				path+"capital-bikeshare_04-06-14_14h18_15h20_dyna.json",
				path+"temp2");
		FormatDefaut.merge(path+"capital-bikeshare_04-06-14_15h20_16h21_dyna.json",
				path+"capital-bikeshare_04-06-14_16h21_17h23_dyna.json",
				path+"temp3");
		FormatDefaut.merge(path+"capital-bikeshare_04-06-14_17h23_18h24_dyna.json",
				path+"capital-bikeshare_04-06-14_18h24_19h26_dyna.json",
				path+"temp4");
		FormatDefaut.merge(path+"capital-bikeshare_04-06-14_19h26_20h28_dyna.json",
				path+"capital-bikeshare_04-06-14_20h28_21h29_dyna.json",
				path+"temp5");
		FormatDefaut.merge(path+"capital-bikeshare_04-06-14_21h29_22h31_dyna.json",
				path+"capital-bikeshare_04-06-14_22h31_23h32_dyna.json",
				path+"temp6");
		FormatDefaut.merge(path+"capital-bikeshare_04-06-14_23h32_00h01_dyna.json",
				path+"temp0",
				path+"temp7");
		FormatDefaut.merge(path+"temp1",
				path+"temp2",
				path+"temp8");
		FormatDefaut.merge(path+"temp3",
				path+"temp4",
				path+"temp9");
		FormatDefaut.merge(path+"temp5",
				path+"temp6",
				path+"temp10");
		FormatDefaut.merge(path+"temp7",
				path+"temp8",
				path+"temp11");
		FormatDefaut.merge(path+"temp9",
				path+"temp10",
				path+"temp12");
		FormatDefaut.merge(path+"temp11",
				path+"temp12",
				path+"fichier_journée");
		
		FormatDefaut.deleteDoubles(path+"fichier_journée");


	}
}
