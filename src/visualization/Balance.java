package visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.LookupPaintScale;
import org.jfree.chart.renderer.xy.XYShapeRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.ShapeUtilities;

import dataAnalysis.MatriceTrajets;
import visualizationTools.BalanceScore;


public class Balance extends ApplicationFrame{

	static String RESOURCES_PATH = "";
	static String csv_File = "aaa.csv";
	static String config_File = "static.json";
	//private static double latitude = 38.8935965;
	//private static double longitude = -77.014576;
	
	private static final long serialVersionUID = 1L;

	public Balance(String s, int numColors) throws IOException, ParseException, org.json.simple.parser.ParseException {
		super(s);
		JPanel jpanel = createDemoPanel(numColors);
		jpanel.setPreferredSize(new Dimension(500*2, 270*2));
		setContentPane(jpanel);
	}

	private static JFreeChart createChart(XYZDataset xyzdataset, int numColors) {
		NumberAxis numberaxis = new NumberAxis("Longitude");
		numberaxis.setAutoRangeIncludesZero(false);
		NumberAxis numberaxis1 = new NumberAxis("Latitude");
		numberaxis1.setAutoRangeIncludesZero(false);
		XYShapeRenderer xyshaperenderer = new XYShapeRenderer();

		LookupPaintScale lookuppaintscale = new LookupPaintScale(-1D, 1D, new Color(255, 255, 0));
		for (int i = 1; i<numColors+1;i++){
			lookuppaintscale.add((-1D/(double)numColors)*i, new Color(255, 255-(255/numColors)*i, 0));
		}
		for (int i = 1; i<numColors+1;i++){
			lookuppaintscale.add((1D/(double)numColors)*i, new Color(255-(255/numColors)*i, 255, 0));
		}

		xyshaperenderer.setPaintScale(lookuppaintscale);
		Shape cross = ShapeUtilities.createDiagonalCross(0, 2);
		xyshaperenderer.setSeriesShape(0, cross);
		XYPlot xyplot = new XYPlot(xyzdataset, numberaxis, numberaxis1, xyshaperenderer);
		xyplot.setDomainPannable(true);
		xyplot.setRangePannable(true);
		JFreeChart jfreechart = new JFreeChart("Sation Balance", xyplot);
		jfreechart.removeLegend();
		NumberAxis numberaxis2 = new NumberAxis("Balance score");
		numberaxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		PaintScaleLegend paintscalelegend = new PaintScaleLegend(lookuppaintscale, numberaxis2);
		paintscalelegend.setPosition(RectangleEdge.RIGHT);
		paintscalelegend.setMargin(4D, 4D, 40D, 4D);
		paintscalelegend.setAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
		jfreechart.addSubtitle(paintscalelegend);
		ChartUtilities.applyCurrentTheme(jfreechart);
		return jfreechart;
	}


	public static XYZDataset createDataset() throws IOException, ParseException, org.json.simple.parser.ParseException {
		DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();
		double[][] indLocStations = MatriceTrajets.indexLoc(config_File);
		int[] stationIndex = MatriceTrajets.index(config_File);
		double[][] matrice = MatriceTrajets.createMat(stationIndex, csv_File);
		int size  = matrice.length;
//		double[][] score = BalanceScore.ScoreRAW(stationIndex, matrice);
		double[][] score = BalanceScore.ScoreLOG(stationIndex, matrice);
//		double[][] score = BalanceScore.Score(stationIndex, matrice);

		double[] x = new double[size],
				y = new double[size], 
				z = new double[size];
		double max = 0;
		double min = 0;
		
		for(int i=0; i<size;i++){
			x[i] = indLocStations[i][1];//-longitude;
			y[i] = indLocStations[i][2];//-latitude;	
			
			max = Math.max(max, score[i][2]);
			min = Math.min(min, score[i][2]);
		}
		for(int i=0; i<size;i++){
			if (score[i][2]<0){
			z[i] = score[i][2]*2./(max-min);
			}
			else if(score[i][2]>0){
			z[i] = score[i][2]*2./(max-min);
			}
			else{
				z[i] = 0;
				}
		}


		double XYZ[][] = { x, y, z };
		defaultxyzdataset.addSeries("Station balance", XYZ);
		return defaultxyzdataset;
	}

	public static JPanel createDemoPanel(int numColors) throws IOException, ParseException, org.json.simple.parser.ParseException {
		JFreeChart jfreechart = createChart(createDataset(),numColors);
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		chartpanel.setPreferredSize(new Dimension(456, 456));
		return chartpanel;
	}

	public static void main(String args[]) throws IOException, ParseException, org.json.simple.parser.ParseException {
		Balance xyshaperendererdemo1 = new Balance("",255);
		xyshaperendererdemo1.pack();
		RefineryUtilities.centerFrameOnScreen(xyshaperendererdemo1);
		xyshaperendererdemo1.setVisible(true);

	}
}
