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

import visualizationTools.Intervals;

public class IntensitePairesColor extends ApplicationFrame{

	static String RESOURCES_PATH = "";
	static String csv_File = "quart.csv";
	static String config_File = "static.json";

	private static final long serialVersionUID = 1L;

	public IntensitePairesColor(String s, int numColors) throws IOException, ParseException, org.json.simple.parser.ParseException {
		super(s);
		JPanel jpanel = createDemoPanel(numColors);
		jpanel.setPreferredSize(new Dimension(500*2, 270*2));
		setContentPane(jpanel);
	}

	private static JFreeChart createChart(XYZDataset xyzdataset, int numColors) {
		NumberAxis numberaxis = new NumberAxis("ln (Distance (m))");
		numberaxis.setAutoRangeIncludesZero(false);
		NumberAxis numberaxis1 = new NumberAxis("ln (Intensity) / ln (max (Intensity))");
		numberaxis1.setAutoRangeIncludesZero(false);
		XYShapeRenderer xyshaperenderer = new XYShapeRenderer();

		LookupPaintScale lookuppaintscale = new LookupPaintScale(0D, 1D, new Color(255, 255, 0));
		for (int i = 1; i<numColors+1;i++){
			lookuppaintscale.add((1D/(double)numColors)*i, new Color(255-(255/numColors)*i, 255-(255/numColors)*i, (255/numColors)*i));
		}

		xyshaperenderer.setPaintScale(lookuppaintscale);
		Shape cross = ShapeUtilities.createDiagonalCross(0, 1);
		xyshaperenderer.setSeriesShape(0, cross);
		XYPlot xyplot = new XYPlot(xyzdataset, numberaxis, numberaxis1, xyshaperenderer);
		xyplot.setDomainPannable(true);
		xyplot.setRangePannable(true);
		JFreeChart jfreechart = new JFreeChart("Station pair balance", xyplot);
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

	private static int maxIntensity (int[][] intens){
		int res = 0;
		for (int i = 0; i<intens.length ; i++){
			for (int j = 0; j<i; j++){
				res = Math.max(res, intens[i][j]+intens[j][i]);
			}
		}
		return res;
	}

	public static XYZDataset createDataset() throws IOException, ParseException, org.json.simple.parser.ParseException {
		DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();

		double[][] paires = Intervals.getPaires(RESOURCES_PATH+config_File);
		int[][] intens = Intervals.intensPaires(RESOURCES_PATH+csv_File, paires);
		int maxIntens = maxIntensity(intens);
		int k = 0;
		double[] x = new double[paires.length*(paires.length-1)/2],
				y = new double[paires.length*(paires.length-1)/2], 
				z = new double[paires.length*(paires.length-1)/2];

		for(int i=0; i<paires.length;i++){
			for (int j = 0; j<i; j++){

				x[k] = Math.log(paires[i][j]);
				y[k] = Math.log(intens[i][j]+intens[j][i]+1)/Math.log(maxIntens+1);
				if(intens[i][j]*intens[j][i]!=0){
					if((double)intens[i][j]/(double)intens[j][i]<=1){
						z[k] = (double)intens[i][j]/(double)intens[j][i];
						k++;
					}
					else{
						z[k] = (double)intens[j][i]/(double)intens[i][j];
						k++;
					}
				}
				else{
					z[k] = 0;
					k++;
				}
			}
		}
		double XYZ[][] = { x, y, z };
		defaultxyzdataset.addSeries("Station pair intensity", XYZ);
		return defaultxyzdataset;
	}

	public static JPanel createDemoPanel(int numColors) throws IOException, ParseException, org.json.simple.parser.ParseException {
		JFreeChart jfreechart = createChart(createDataset(),numColors);
		ChartPanel chartpanel = new ChartPanel(jfreechart);
		chartpanel.setMouseWheelEnabled(true);
		return chartpanel;
	}

	public static void main(String args[]) throws IOException, ParseException, org.json.simple.parser.ParseException {
		IntensitePairesColor xyshaperendererdemo1 = new IntensitePairesColor("",255);
		xyshaperendererdemo1.pack();
		RefineryUtilities.centerFrameOnScreen(xyshaperendererdemo1);
		xyshaperendererdemo1.setVisible(true);

	}
}
