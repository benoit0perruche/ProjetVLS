package visualization;


//http://read.pudn.com/downloads92/sourcecode/graph/359672/JFreeChart/src/com/rjsoft/barcharts/xyplot/XYBarChartDemo2.java__.htm


import java.awt.Color;
import java.awt.Dimension;   
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;   

import org.jfree.chart.*;   
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;   
import org.jfree.chart.plot.XYPlot;   
import org.jfree.data.time.*;   
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;   
import visualizationTools.ComparaisonDates;
import dataRecovery.Parser;


@SuppressWarnings("serial")
public class TimeSeriesChart extends ApplicationFrame {
	static String name1 = "W00009";
	static String name2 = "W00011";
	static String name3 = "W00015";
	static String name4 = "W00093";
	
	static Color Abonne = new Color(255,0,0);	
//	static Color AbonneFast = new Color(255,40,0);
//	static Color AbonneSlow = new Color(255,0,40);
	
	static Color Occasionnel = new Color(0,255,0);
//	static Color OccasionnelFast = new Color(40,255,0);
//	static Color OccasionnelSlow = new Color(0,255,40);
	
	static Color Camion = new Color(0,0,255);

	public TimeSeriesChart (String s, List<String[]> trips) {
		super(s);
		JPanel jpanel = createPanel(trips);
		jpanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(jpanel);

	}


	/**
	 * Creates the dataset
	 * 
	 * @param trips
	 * @return a TimeSeriesCollection dataset
	 */
	@SuppressWarnings("deprecation")
	private static XYDataset createDataset (List<String[]> trips) {

		final TimeSeries serie1 = new TimeSeries("Abonné", Second.class);
//		final TimeSeries serie1a = new TimeSeries("Abonné rapide", Second.class);
//		final TimeSeries serie1b = new TimeSeries("Abonné lent", Second.class);
		
		final TimeSeries serie2 = new TimeSeries("Occasionnel", Second.class);
//		final TimeSeries serie2a = new TimeSeries("Occasionnel rapide", Second.class);
//		final TimeSeries serie2b = new TimeSeries("Occasionnel lent", Second.class);
		
		final TimeSeries serie3 = new TimeSeries("Camion", Second.class);

		double dist;

		for(String[] str : trips) {

			final String dateInit = str[1];	
			final String dateEnd = str[3];

			// Cas ou la distance n'a pas pu être calculée
			if(str[7].equals("Erreur")){
				dist = -1.0;
			}
			// Récupération de la distance
			else{
				dist = Double.parseDouble(str[7]);
			}

			// Recuperation donnees dates de depart et d'arrivee
			int anInit = ComparaisonDates.getAnnee(dateInit);
			int anEnd = ComparaisonDates.getAnnee(dateEnd);

			int moInit = ComparaisonDates.getMois(dateInit);
			int moEnd = ComparaisonDates.getMois(dateEnd);

			int joInit = ComparaisonDates.getJour(dateInit);
			int joEnd = ComparaisonDates.getJour(dateEnd);

			int heInit = ComparaisonDates.getHeure(dateInit);
			int heEnd = ComparaisonDates.getHeure(dateEnd);

			int miInit = ComparaisonDates.getMinute(dateInit);
			int miEnd = ComparaisonDates.getMinute(dateEnd);

			// secondes à 30 pour ne pas tomber sur la meme date qu'un autre trajet
			// (au maximum une valeure pour une date donnée (pas de superposition))
			int sec = 30;

			// Recuperation du type de trajet (utilisateur abonné, utilisateur occasionnel ou camion) 
			final String type = str[6];
			
			// Recuperation distance parcourue (a vol d'oiseau)
			Double yValue = dist;
			// Si station départ = arrivee, distance = 0
			// dist = 10 pour "visibilité"
			if (yValue == 0){
				yValue += 10;
			}

			// Remplie les series en fonction du type d'utilisateur
			if(type.equals("Registered")){
				serie1.addOrUpdate(new Second(sec-1, miInit, heInit, joInit, moInit, anInit), Plot.ZERO);
				serie1.addOrUpdate(new Second(sec, miInit, heInit, joInit, moInit, anInit), yValue);
				serie1.addOrUpdate(new Second(sec, miEnd, heEnd, joEnd, moEnd, anEnd), yValue);
				serie1.addOrUpdate(new Second(sec+1, miEnd, heEnd, joEnd, moEnd, anEnd), Plot.ZERO);

				// For the end of the graph (finish slightly after the last bar)
				serie1.addOrUpdate(new Second(sec+5, miEnd+5, heEnd, joEnd, moEnd, anEnd), Plot.ZERO);
			}
			else{
				if(type.equals("Casual")){
					serie2.add(new Second(sec-1, miInit, heInit, joInit, moInit, anInit), Plot.ZERO);
					serie2.add(new Second(sec, miInit, heInit, joInit, moInit, anInit), yValue);
					serie2.add(new Second(sec, miEnd, heEnd, joEnd, moEnd, anEnd), yValue);
					serie2.add(new Second(sec+1, miEnd, heEnd, joEnd, moEnd, anEnd), Plot.ZERO);

					// For the end of the graph (finish slightly after the last bar)
					serie2.addOrUpdate(new Second(sec+5, miEnd+5, heEnd, joEnd, moEnd, anEnd), Plot.ZERO);
				}
				else{
					serie3.add(new Second(sec-1, miInit, heInit, joInit, moInit, anInit), Plot.ZERO);
					serie3.add(new Second(sec, miInit, heInit, joInit, moInit, anInit), dist);
					serie3.add(new Second(sec, miEnd, heEnd, joEnd, moEnd, anEnd), yValue);
					serie3.add(new Second(sec+1, miEnd, heEnd, joEnd, moEnd, anEnd), Plot.ZERO);

					// For the end of the graph (finish slightly after the last bar)
					serie3.addOrUpdate(new Second(sec+5, miEnd+5, heEnd, joEnd, moEnd, anEnd), Plot.ZERO);
				}

			}
		}

		// get the dataset
		final TimeSeriesCollection dataset = new TimeSeriesCollection(serie1);
		dataset.addSeries(serie2);
		dataset.addSeries(serie3);

		return dataset;
	}


	/**
	 * create panel
	 * 
	 * @param trips
	 * @return the ChartPanel
	 */
	public static JPanel createPanel(List<String[]> trips) {
		return new ChartPanel(createChart(createDataset(trips)));
	}


	/**
	 * Creates a chart.
	 * 
	 * @param xyDataset  the dataset.
	 * 
	 * @return A chart.
	 */
	private static JFreeChart createChart(final XYDataset xyDataset) {

		final JFreeChart chart = ChartFactory.createXYAreaChart(
				"Vélo : " + name1,
				"Temps", 
				"Distance à vol d'oiseau (en m)",
				xyDataset,
				PlotOrientation.VERTICAL, 
				true,  // legend
				true,  // tool tips
				false  // URLs
				);

		chart.setBackgroundPaint(Color.white);

//		final XYPlot plot = chart.getXYPlot();
		XYPlot plot = (XYPlot) chart.getPlot();
		//		XYDifferenceRenderer xydifferencerenderer = new XYDifferenceRenderer(Color.green, Color.red, true);
		//		xydifferencerenderer.setRoundXCoordinates(true);
		//		plot.setRenderer(xydifferencerenderer);
		//plot.setOutlinePaint(Color.black);
		plot.setBackgroundPaint(Color.white);
		plot.setForegroundAlpha(0.65f);
//		plot.setDomainGridlinePaint(Color.white);
//		plot.setRangeGridlinePaint(Color.white);
		
		DateAxis dateaxis = new DateAxis("Temps");

//		final ValueAxis domainAxis = plot.getDomainAxis();
		dateaxis.setTickMarkPaint(Color.black);
		dateaxis.setLowerMargin(0.0);
		dateaxis.setUpperMargin(0.0);
		plot.setDomainAxis(dateaxis);

//		XYItemRenderer xyitemrenderer = plot.getRenderer();  
		plot.getRenderer().setSeriesPaint(0, Abonne);
		plot.getRenderer().setSeriesPaint(1, Occasionnel);
		plot.getRenderer().setSeriesPaint(2, Camion);
		
//		xyitemrenderer.setBaseToolTipGenerator(
//				new StandardXYToolTipGenerator("{0}: ({1}, {2})", 
//						new SimpleDateFormat("d-MMM-yyyy"), 
//						new DecimalFormat("#,##0.00")));


		return chart;

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
	 * Starting point for the demonstration application.
	 *
	 * @param args  ignored.
	 */
	public static void main(final String[] args) {

		List<String[]> trips1 = new ArrayList<String[]>();
		List<String[]> trips2 = new ArrayList<String[]>();
		List<String[]> trips3 = new ArrayList<String[]>();
		@SuppressWarnings("unused")
		List<String[]> trips4 = new ArrayList<String[]>();
		
		String RESOURCES_PATH = "/home/benoit/Documents/Polytech4A/Stage/Donnees/Washington/Trajets";
		String TRIPS_FILE_NAME = "/2014-Washington-January";

		try {

			trips1 = Parser.parse(RESOURCES_PATH+TRIPS_FILE_NAME+".csv", name1 );
			trips2 = Parser.parse(RESOURCES_PATH+TRIPS_FILE_NAME+".csv", name2 );
			trips3 = Parser.parse(RESOURCES_PATH+TRIPS_FILE_NAME+".csv", name3 );
			
			final TimeSeriesChart demo1 = new TimeSeriesChart("TimeSeriesChart Demo", trips1);
			final TimeSeriesChart demo2 = new TimeSeriesChart("TimeSeriesChart Demo", trips2);
			final TimeSeriesChart demo3 = new TimeSeriesChart("TimeSeriesChart Demo", trips3);
			
			demo1.pack();
//			RefineryUtilities.centerFrameOnScreen(demo1);
			demo1.setVisible(true);
			demo2.pack();
//			RefineryUtilities.centerFrameOnScreen(demo2);
			demo2.setVisible(true);
			demo3.pack();
			demo3.setVisible(true);
			
			
		} catch (Exception e) {
		
			e.printStackTrace();
		}
	}

}