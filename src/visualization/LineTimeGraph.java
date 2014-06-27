package visualization;

import java.awt.*;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.*;
import org.jfree.ui.*;

import visualizationTools.Old_Flux;


import dataRecovery.Parser;

public class LineTimeGraph extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static String RESOURCES_PATH = "/home/ricky/VLS/Washington/";
	static String TRIPS_FILE_NAME = "Washington_Trips_7-3-2014(copie).csv";
	
	static long idStation0 = 31623;
	static List<String[]> tripsStart0 = new ArrayList<String[]>();
	static List<String[]> tripsEnd0 = new ArrayList<String[]>();
//	tripsStart0 = Parser.parseStart(RESOURCES_PATH+TRIPS_FILE_NAME, String.valueOf(idStation0));
//	tripsEnd0 = Parser.parseEnd(RESOURCES_PATH+TRIPS_FILE_NAME, String.valueOf(idStation0)); 

	static long idStation1 = 31628;
	static List<String[]> tripsStart1 = new ArrayList<String[]>();
	static List<String[]> tripsEnd1 = new ArrayList<String[]>();
	
	static long idStation2 = 31629;
	static List<String[]> tripsStart2 = new ArrayList<String[]>();
	static List<String[]> tripsEnd2 = new ArrayList<String[]>();

	DateFormat yearFormat = new SimpleDateFormat("yyyy");
	DateFormat montFormat = new SimpleDateFormat("MM");
	DateFormat dayFormat = new SimpleDateFormat("dd");
	static DateFormat hourFormat = new SimpleDateFormat("HH");


	static class DemoPanel extends JPanel
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private XYDataset data;

		@SuppressWarnings("deprecation")
		private TimeSeriesCollection createSampleData() throws Exception 
		{
			final TimeSeriesCollection dataset = new TimeSeriesCollection();
			TimeSeries serie0 = new TimeSeries("Flux Entrant et Sortant", Hour.class);
			TimeSeries serie1 = new TimeSeries("Flux Entrant et Sortant", Hour.class);
			TimeSeries serie2 = new TimeSeries("Flux Entrant et Sortant", Hour.class);

			
			tripsStart0 = Parser.parseStart(RESOURCES_PATH+TRIPS_FILE_NAME, String.valueOf(idStation0) );
			tripsEnd0 = Parser.parseEnd(RESOURCES_PATH+TRIPS_FILE_NAME, String.valueOf(idStation0));
			tripsStart1 = Parser.parseStart(RESOURCES_PATH+TRIPS_FILE_NAME, String.valueOf(idStation1) );
			tripsEnd1 = Parser.parseEnd(RESOURCES_PATH+TRIPS_FILE_NAME, String.valueOf(idStation1));
			tripsStart2 = Parser.parseStart(RESOURCES_PATH+TRIPS_FILE_NAME, String.valueOf(idStation2) );
			tripsEnd2 = Parser.parseEnd(RESOURCES_PATH+TRIPS_FILE_NAME, String.valueOf(idStation2));

			serie0 = getFlux(tripsStart0, tripsEnd0, idStation0);
			serie1 = getFlux(tripsStart1, tripsEnd1, idStation1);
			serie2 = getFlux(tripsStart2, tripsEnd2, idStation2);
			dataset.addSeries(serie0);
			dataset.addSeries(serie1);
			dataset.addSeries(serie2);
			return dataset;

		}
		
		/**
		 * @param tripsStart
		 * @param tripsEnd
		 * @param idStation
		 * @return
		 * @throws Exception
		 */
		@SuppressWarnings("deprecation")
		private TimeSeries getFlux (List<String[]> tripsStart, List<String[]> tripsEnd, long idStation) throws Exception{
			long[] velosIn = velosIn(tripsEnd, idStation);
			long[] velosOut = velosOut(tripsStart, idStation);			
			
			final TimeSeries serie = new TimeSeries("Station " + idStation, Hour.class);
			RegularTimePeriod start = new Hour(0, 7, 3, 2014);
			long flux = 0;
			for (int i = 0; i < 24; i++){
				flux = velosIn[i]-velosOut[i];
				serie.add(start, flux);
				start = start.next();
			}
			return serie;
		}

		private long[] velosOut (List<String[]> tripsStart, long idStation) throws Exception {
			long[][] tabOut;
			int l = 0; int hourInt = 0; int hour = 0;
			long[] velosOut = new long[24];
			tabOut = Old_Flux.getBikesOut(idStation, RESOURCES_PATH+TRIPS_FILE_NAME);
			RegularTimePeriod start = new Hour(0, 7, 3, 2014);
			for (int i = 0; i < 24; i++){
//				System.out.println(i);
				hour = Integer.parseInt(hourFormat.format(start.getStart()));
				if (l<tabOut.length){
					hourInt = (int) tabOut[l][1];					
					if (hourInt == hour){
							velosOut[hour] = tabOut[l][0];
							l++;
					} else { velosOut[hour] = 0; }
				} else { velosOut[hour] = 0; }
				start = start.next();
			}
			return velosOut;
		}
		
		private long[] velosIn (List<String[]> tripsEnd, long idStation) throws Exception {
			long[][] tabIn;
			int l = 0; int hourInt = 0; int hour = 0;
			long[] velosIn = new long[24];
			tabIn = Old_Flux.getBikesIn(idStation, RESOURCES_PATH+TRIPS_FILE_NAME);
			RegularTimePeriod start = new Hour(0, 7, 3, 2014);
			for (int i = 0; i < 24; i++){
				if (l<tabIn.length){
					hourInt = (int) tabIn[l][1];
					hour = Integer.parseInt(hourFormat.format(start.getStart()));
					if (hourInt == hour){
							velosIn[hour] = tabIn[l][0];
							l++;						
					} else { velosIn[hour] = 0; }
				} else { velosIn[hour] = 0; }
				start = start.next();
			}
			return velosIn;
		}

		private JTabbedPane createContent() throws Exception
		{
			JTabbedPane jtabbedpane = new JTabbedPane();
			jtabbedpane.add("LineTimeGraph", createChartPanel1());
//			jtabbedpane.add("Station " + idStation1, createChartPanel1(tripsStart, tripsEnd));
			return jtabbedpane;
		}

		private ChartPanel createChartPanel1() throws Exception
		{
			
			DateAxis dateaxis = new DateAxis("Time");
			NumberAxis numberaxis1 = new NumberAxis("Flux");
			numberaxis1.setAutoRangeIncludesZero(true);
			XYSplineRenderer xysplinerenderer = new XYSplineRenderer();
			
			XYPlot xyplot = new XYPlot(data, dateaxis, numberaxis1, xysplinerenderer);
			xyplot.setBackgroundPaint(Color.white);
			xyplot.setDomainGridlinePaint(Color.white);
			xyplot.setRangeGridlinePaint(Color.black);
			xyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));
			JFreeChart jfreechart = new JFreeChart("Flux entrant et sortant", 
					JFreeChart.DEFAULT_TITLE_FONT, 
					xyplot, 
					true);
			jfreechart.setBackgroundPaint(Color.white);
			ChartPanel chartpanel = new ChartPanel(jfreechart, false);
			return chartpanel;
		}


		public DemoPanel() throws Exception
		{
			super(new BorderLayout());
			data = createSampleData();
			add(createContent());
		}
	}


	public LineTimeGraph(String s) throws Exception
	{
		super(s);
		JPanel jpanel = createDemoPanel();
		getContentPane().add(jpanel);
	}

	public static JPanel createDemoPanel() throws Exception
	{
		return new DemoPanel();
	}

	public static void main(String args[])
	{

		try {

			tripsStart0 = Parser.parseStart(RESOURCES_PATH+TRIPS_FILE_NAME, String.valueOf(idStation0) );
			tripsEnd0 = Parser.parseEnd(RESOURCES_PATH+TRIPS_FILE_NAME, String.valueOf(idStation0));

			LineTimeGraph xysplinerenderer = new LineTimeGraph("TimeLine Graph");
			xysplinerenderer.pack();
			RefineryUtilities.centerFrameOnScreen(xysplinerenderer);
			xysplinerenderer.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}

}
