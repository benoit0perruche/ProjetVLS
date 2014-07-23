package visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import dataAnalysis.MatriceTrajets;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYDotRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

public class Intensity extends ApplicationFrame {


	private static final long serialVersionUID = 1L;

	static String RESOURCES_PATH = "";
	static String csv_File = "trips(copie).csv";
	static String config_File = "static.json";

	public Intensity(String s) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
		super(s);
		JPanel jpanel = createDemoPanel();
		jpanel.setPreferredSize(new Dimension(500*2, 270*2));
		setContentPane(jpanel);
	}

	public static JPanel createDemoPanel() throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{

		JFreeChart jfreechart = ChartFactory.createScatterPlot("Sation Intensities",
				"Stations", 
				"Intensity", 
				samplexydataset(), 
				PlotOrientation.VERTICAL, 
				true, 
				true, 
				false);
		Shape cross = ShapeUtilities.createDiagonalCross(3, 1);

		XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
		XYItemRenderer renderer = xyPlot.getRenderer();
		renderer.setBaseShape(cross);
		renderer.setBasePaint(Color.blue);
		//changing the Renderer to XYDotRenderer
		//xyPlot.setRenderer(new XYDotRenderer());
		XYDotRenderer xydotrenderer = new XYDotRenderer();
		xydotrenderer.setDotHeight(5);
		xydotrenderer.setDotWidth(5);
		xyPlot.setRenderer(xydotrenderer);
		xydotrenderer.setSeriesShape(0, cross);

		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		xyPlot.setBackgroundPaint(Color.white);

		return new ChartPanel(jfreechart);
	}

	private static XYDataset samplexydataset() throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
		int[] indiceStations = MatriceTrajets.index(config_File);
		double[][] matrice = MatriceTrajets.createMat(indiceStations, csv_File);
		double[][] sym = MatriceTrajets.createSym(matrice);
		double[] intensity = new double[sym.length];

		for(int i=0; i<sym.length; i++){
			for(int j=0; j<sym.length; j++){
				intensity[i]+=sym[i][j];
			}
		}

		Arrays.sort(intensity);


		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		XYSeries series0 = new XYSeries("Stations");

		for(int i=0; i<intensity.length; i++){
			if (intensity[i]>0){
				series0.add(i, 
						Math.log(intensity[i]));
			}
		}
		xySeriesCollection.addSeries(series0);

		return xySeriesCollection;
	}

	public static void main(String args[]) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
		Intensity scatterplotdemo4 = new Intensity("Sation Intensities");
		scatterplotdemo4.pack();
		RefineryUtilities.centerFrameOnScreen(scatterplotdemo4);
		scatterplotdemo4.setVisible(true);
	}
}
