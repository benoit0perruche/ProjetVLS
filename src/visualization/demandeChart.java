package visualization;
import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import dataRecovery.City;

import visualizationTools.Old_Flux;
import dataRecovery.Parser;


public class demandeChart extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static int idStation = 31200;

	DateFormat yearFormat = new SimpleDateFormat("yyyy");
	DateFormat montFormat = new SimpleDateFormat("MM");
	DateFormat dayFormat = new SimpleDateFormat("dd");
	DateFormat hourFormat = new SimpleDateFormat("HH");
	DateFormat minFormat = new SimpleDateFormat("mm");

	long timestamp = 0; 
	
	String path = "/home/benoit/Documents/Polytech4A/Stage" +
			"/Donnees/Washington/Trajets/Washington_Trips_7-3-2014.csv";

	/**
	 * @param title
	 * @param obj
	 * @param tripsStart
	 * @param tripsEnd
	 * @throws Exception
	 */
	public demandeChart(String title, Object obj, List<String[]> tripsStart, List<String[]> tripsEnd) throws Exception {
		super(title);
		final JFreeChart chart = createChart(obj, tripsStart, tripsEnd);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
	}
	// ****************************************************************************
	// * JFREECHART DEVELOPER GUIDE                                               *
	// * The JFreeChart Developer Guide, written by David Gilbert, is available   *
	// * to purchase from Object Refinery Limited:                                *
	// *                                                                          *
	// * http://www.object-refinery.com/jfreechart/guide.html                     *
	// *                                                                          *
	// * Sales are used to provide funding for the JFreeChart project - please    * 
	// * support us so that we can continue developing free software.             *
	// ****************************************************************************


	/**
	 * Creates a sample dataset
	 * 
	 * @returnthe TimeSeries of the bikes IN during the day
	 * @throws Exception
	 */
	private TimeSeries createInDataset() throws Exception {

		int hourInt = 0; int minInt = 0;
		int l = 0;
		long velosIn = 0;
		long[][] tab;


		@SuppressWarnings("deprecation")
		final TimeSeries s1 = new TimeSeries("Velos IN", Minute.class);

		RegularTimePeriod start = new Minute(0, 0, 6, 6, 2014);
		
		
		tab = Old_Flux.getBikesIn(idStation, path);
		
		
		for (int i = 0; i < 1480; i++){
			if (l<tab.length){
				hourInt = Integer.parseInt(hourFormat.format(tab[l][1]));
				
				minInt =  Integer.parseInt(minFormat.format(tab[l][1]));
				
				if (hourInt == Integer.parseInt(hourFormat.format(start.getStart()))
						&& minInt == Integer.parseInt(minFormat.format(start.getStart()))){
					//					System.out.println("tab[" + l + "][0] : " + tab[l][0]);
					velosIn = tab[l][0];
					//					System.out.println("Percent : " + percent);
					l++;
				} else { velosIn = 0; }
			} else { velosIn = 0; }
			s1.add(start, velosIn);
			start = start.next();
		}
		return s1;
	}

	/**
	 * Creates a sample dataset
	 * 
	 * @return the TimeSeries of the bikes OUT during the day
	 * @throws Exception
	 */

	private TimeSeries createOutDataset() throws Exception {
		int hourInt = 0; int minInt = 0;
		int l = 0;
		long velosOut = 0;
		long[][] tab;

		@SuppressWarnings("deprecation")
		final TimeSeries s2 = new TimeSeries("Velos OUT", Minute.class);

		RegularTimePeriod start = new Minute(0, 0, 6, 6, 2014);
		
		
		tab = Old_Flux.getBikesOut(idStation, path);
		
		
		for (int i = 0; i < 1480; i++){
			if (l<tab.length){
				hourInt = Integer.parseInt(hourFormat.format(tab[l][1]));
				
				minInt =  Integer.parseInt(minFormat.format(tab[l][1]));
				
				if (hourInt == Integer.parseInt(hourFormat.format(start.getStart()))
						&& minInt == Integer.parseInt(minFormat.format(start.getStart()))){
					//					System.out.println("tab[" + l + "][0] : " + tab[l][0]);
					velosOut = tab[l][0];
					//					System.out.println("Percent : " + percent);
					l++;
				} else { velosOut = 0; }
			} else { velosOut = 0; }
			s2.add(start, velosOut);
			start = start.next();
		}
		return s2;
	}

	/**
	 * @param stat
	 * @param idStation
	 * @return the station slot number
	 */
	private long nbSlots (JSONArray stat, int idStation) {
		long slots = -1;
		for (int i=0; i<stat.size(); i++){
			JSONObject station = (JSONObject) stat.get(i);
			long id = (Long) station.get("id");
			if (id == idStation){
				slots = (Long) station.get("slots");	

				return slots;
			}
		}
		return slots;
	}

	/**
	 * @param stat
	 * @param idStation
	 * @return an array with the number of free_bikes and the last update
	 * 	for our station
	 */
	private long[][] createFreeArray (JSONArray stat, int idStation) {
		long[][] tab;
		int l = 0, j = 0;
		for (int i=0; i<stat.size(); i++){
			JSONObject station = (JSONObject) stat.get(i);
			long id = (Long) station.get("id");
			if (id == idStation){
				l++;
			}
		}
		tab = new long[l][2];
		long decalage = City.getCityTimeZone(5) * 3600000;
		for (int i=0; i<stat.size(); i++){
			JSONObject station = (JSONObject) stat.get(i);
			long id = (Long) station.get("id");
			if (id == idStation){
				tab[j][0] = (Long) station.get("free_bikes");
				
				tab[j][1] = (Long) station.get("last_update") + decalage;
				j++;
			}
		}		
		return tab;
	}


	/**
	 * Creates a sample dataset.
	 * @param obj
	 * @return the dataset
	 */
	private TimeSeriesCollection createRempliDataset(Object obj) {
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray stat = (JSONArray) jsonObject.get("stations");

		int hourInt = 0; int minInt = 0;
		double percent = 50; int l = 1;
		double nbSlots = 0; double v = 0;
		long[][] tab;

		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		@SuppressWarnings("deprecation")
		final TimeSeries s1 = new TimeSeries("Remplissage", Minute.class);

		RegularTimePeriod start = new Minute(0, 0, 6, 6, 2014);

		nbSlots = nbSlots(stat, idStation);
		if (nbSlots == -1) {
			System.out.println("Erreur nombre slots");
		}
//		System.out.println("Vrai nbSlots : " + nbSlots);
		tab = createFreeArray(stat, idStation);
//		System.out.println("tab[0][0] : " + tab[0][0]);
		int length = tab.length;
		v = ((Long) tab[0][0]).doubleValue();

		percent = (Double) (v / nbSlots);
//		System.out.println("Percent : " + percent);

		for (int i = 1; i < 1480; i++){
			s1.add(start, percent);
			start = start.next();

			if (l<length){
				hourInt = Integer.parseInt(hourFormat.format(tab[l][1]));
//				System.out.println("heure tab["+l+"][1] : " + hourInt);
				minInt =  Integer.parseInt(minFormat.format(tab[l][1]));
//				System.out.println("minute tab["+l+"][1] : " + minInt);
				if (hourInt == Integer.parseInt(hourFormat.format(start.getStart()))
						&& minInt == Integer.parseInt(minFormat.format(start.getStart()))){
					//					System.out.println("tab[" + l + "][0] : " + tab[l][0]);
					v = ((Long) tab[l][0]).doubleValue();
					percent = (Double) (v /  nbSlots);
					//					System.out.println("Percent : " + percent);
					l++;
				}
			}
		} 


		dataset.addSeries(s1);
		return dataset;
	}

	/**
	 * @param obj
	 * @param tripsStart
	 * @param tripsEnd
	 * @return the chart
	 * @throws Exception
	 */
	private JFreeChart createChart(Object obj, List<String[]> tripsStart, List<String[]> tripsEnd) throws Exception {
		final TimeSeries dataset1 = createInDataset();
		final TimeSeries dataset2 = createOutDataset();
		final TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(dataset1);
		dataset.addSeries(dataset2);
		//		TimeSeriesCollection my_data_series= new TimeSeriesCollection();
		//		my_data_series.add(dataset1);

		final JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Station num " + idStation, 
				"Time", 
				"Bikes",
				dataset,
				true,
				true,
				false
				);

		final XYPlot plot = chart.getXYPlot();
		plot.getDomainAxis().setLowerMargin(0.0);
		plot.getDomainAxis().setUpperMargin(0.0);

		// configure the range axis to display directions...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
//		rangeAxis.setAutoRangeIncludesZero(false);
		
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray stat = (JSONArray) jsonObject.get("stations");
		long nbSlots = nbSlots(stat, idStation);
		if (nbSlots == -1) {
			System.out.println("Erreur nombre slots");
		}
		rangeAxis.setRange(0, nbSlots);
//		final TickUnits units = new TickUnits();
//		units.add(new NumberTickUnit(180.0, new CompassFormat()));
//		units.add(new NumberTickUnit(90.0, new CompassFormat()));
//		units.add(new NumberTickUnit(45.0, new CompassFormat()));
//		units.add(new NumberTickUnit(22.5, new CompassFormat()));
//		rangeAxis.setStandardTickUnits(units);

		// add the remplissage with a secondary dataset/renderer/axis
		plot.setRangeAxis(rangeAxis);
		final XYItemRenderer renderer2 = new XYAreaRenderer();
		final NumberAxis axis2 = new NumberAxis("Remplissage Station");

		axis2.setNumberFormatOverride(new DecimalFormat("0.0%"));
		StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.30);
		renderer.setRenderAsPercentages(true);

		axis2.setRange(0.0, 1.0);
		renderer2.setSeriesPaint(0, new Color(160, 160, 160, 128));
		plot.setDataset(1, createRempliDataset(obj));
		plot.setRenderer(1, renderer2);
		plot.setRangeAxis(1, axis2);
		plot.mapDatasetToRangeAxis(1, 1);
		plot.setBackgroundPaint(Color.white);
		plot.setDomainGridlinePaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.lightGray);

		return chart;
	}

	/**
	 * Starting point for the application.
	 *
	 * @param args  ignored.
	 * @throws Exception 
	 */
	public static void main(final String[] args) throws Exception {

		JSONParser parser = new JSONParser();
		List<String[]> tripsStart = new ArrayList<String[]>();
		List<String[]> tripsEnd = new ArrayList<String[]>();
		String RESOURCES_PATH = "/home/benoit/Documents/Polytech4A/Stage/Donnees/Washington/Trajets";
		String TRIPS_FILE_NAME = "/2014-Washington-January";

		try {
			Object obj = parser.parse(new FileReader
					("/home/benoit/Documents/Polytech4A/Stage" +
							"/Donnees/Washington/realTime" +
							"/capital-bikeshare_04-06-14_19h26_20h28_dyna.json"));
			tripsStart = Parser.parseStart(RESOURCES_PATH+TRIPS_FILE_NAME+".csv", String.valueOf(idStation) );
			tripsEnd = Parser.parseEnd(RESOURCES_PATH+TRIPS_FILE_NAME+".csv", String.valueOf(idStation) );

			final demandeChart demo = new demandeChart("Graphe Demande 1", obj, tripsStart, tripsEnd);
			demo.pack();
			RefineryUtilities.centerFrameOnScreen(demo);
			demo.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
