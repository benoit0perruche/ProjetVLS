package visualization;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import visualizationTools.OutilsDemande;

import dataAnalysis.MatriceTrajets;

import Jama.Matrix;

public class HistoValPropreNMF extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	static String RESOURCES_PATH = "/home/benoit/Documents/Polytech4A/Stage/Donnees/Washington";
	static String csv_File = "/Trajets/2014-Washington-January.csv";
	static String static_File = "/config_Washington.json";

	/**
	 * Creates a new demo instance.
	 *
	 * @param title  the frame title.
	 * @throws org.json.simple.parser.ParseException 
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public HistoValPropreNMF(String title, String staticFile, String csv) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException {
		super(title);
		JPanel chartPanel = createDemoPanel(staticFile, csv);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);
	}

	/**
	 * Returns a sample dataset.
	 *
	 * @return The dataset.
	 * @throws org.json.simple.parser.ParseException 
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private static IntervalXYDataset createDataset(String staticFile, String csv) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException {
		XYSeries xyseries = new XYSeries("Valeurs propres");
		Matrix[] matrices = MatriceTrajets.getMatrix(staticFile, csv);
		double[][] matrice = matrices[1].getArray();
		double max = OutilsDemande.getMaxMatrice(matrice);
		double alpha = 1;
		
		for (int i = 0; i < matrice.length; i++){
			alpha = OutilsDemande.getAlpha(max, matrice[i][i]);
			xyseries.add(i, alpha);
		}
		XYSeriesCollection xyseriescollection = new XYSeriesCollection();
		xyseriescollection.addSeries(xyseries);
		return new XYBarDataset(xyseriescollection, 0.90000000000000002D);
	}

	/**
	 * Creates a sample chart.
	 *
	 * @param intervalXYDataset  the dataset.
	 *
	 * @return The chart.
	 */
	private static JFreeChart createChart(IntervalXYDataset intervalXYDataset) {

		// create the chart...
		JFreeChart jfreechart = ChartFactory.createXYBarChart(
				"Histogramme Valeurs Propres",       // chart title
				null,                     // domain axis label
				false, "VP",                  // range axis label
				intervalXYDataset,                  // data
				PlotOrientation.VERTICAL,
				true,                     // include legend
				true,                     // tooltips?
				false                     // URLs?
				);

		jfreechart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();
        xyplot.setBackgroundPaint(Color.white);
        xyplot.setDomainGridlinePaint(Color.black);
        xyplot.setRangeGridlinePaint(Color.black);
        NumberAxis numberaxis = (NumberAxis)xyplot.getDomainAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        XYBarRenderer xybarrenderer = (XYBarRenderer)xyplot.getRenderer();
        xybarrenderer.setSeriesPaint(0, Color.blue);
//        xybarrenderer.setDrawBarOutline(false);
        return jfreechart;
	}

	/**
	 * Creates a panel for the demo (used by SuperDemo.java).
	 *
	 * @return A panel.
	 * @throws org.json.simple.parser.ParseException 
	 * @throws ParseException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static JPanel createDemoPanel(String staticFile, String csv) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException {
		JFreeChart chart = createChart(createDataset(staticFile, csv));
		return new ChartPanel(chart);
	}

	/**
	 * Starting point for the demonstration application.
	 *
	 * @param args  ignored.
	 */
	public static void main(String[] args) {
		HistoValPropreNMF demo = null;
		try {
			demo = new HistoValPropreNMF(
					"Valeurs Propres", RESOURCES_PATH+static_File, RESOURCES_PATH+csv_File);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}
		demo.pack();
		RefineryUtilities.centerFrameOnScreen(demo);
		demo.setVisible(true);
	}
}
