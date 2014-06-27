package visualization;

import java.awt.*;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.*;
import org.jfree.ui.*;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.DeviationRenderer;

import visualizationTools.MeanStdDev;
import dataRecovery.Parser;

import dataAnalysis.LireArff;


public class ClustersGraph extends ApplicationFrame {

	private static final String ARFF_FILE_PATH = "/home/benoit/Documents/Polytech4A/Stage/Weka";
	private static final String FILE_NAME = "/EM6IN.arff";
	static String Week_File = "/Vweek_noid_In.csv";

	private static final long serialVersionUID = 1L;
	/**
	 * @param s the window title
	 * @throws Exception
	 */
	public ClustersGraph(String s) throws Exception
    {
            super(s);
            JPanel jpanel = createDemoPanel();
            jpanel.setPreferredSize(new Dimension(500, 270));
            setContentPane(jpanel);
    }
	
	/**
	 * @return the JPanel
	 * @throws Exception
	 */
	public static JPanel createDemoPanel() throws Exception
    {
            JFreeChart jfreechart = createChart(createDataset());
            return new ChartPanel(jfreechart);
    }



	/**
	 * @return XYDataset
	 * @throws Exception
	 */
	private static XYDataset createDataset() throws Exception 
	{
		int[] clusterArray = LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME,24);

		double[][] bikesInArray = MeanStdDev.StringtoDouble(Parser.parse(ARFF_FILE_PATH+Week_File));

		double[][] mean0 = MeanStdDev.getMeanStd(clusterArray, bikesInArray, 0, 24);
		double[][] mean1 = MeanStdDev.getMeanStd(clusterArray, bikesInArray, 1, 24);
		double[][] mean2 = MeanStdDev.getMeanStd(clusterArray, bikesInArray, 2, 24);
//		double[][] mean3 = MeanStdDev.getMeanStd(clusterArray, bikesInArray, 3, 24);
//		double[][] mean4 = MeanStdDev.getMeanStd(clusterArray, bikesInArray, 4, 24);
//		double[][] mean5 = MeanStdDev.getMeanStd(clusterArray, bikesInArray, 5, 24);

		final YIntervalSeriesCollection dataset = new YIntervalSeriesCollection();
		// mean"i"[24][0] is the number of stations in the cluster "i"
		YIntervalSeries cluster0 = new YIntervalSeries("Cluster0  " + (int)mean0[24][0] + " ");
		YIntervalSeries cluster1 = new YIntervalSeries("Cluster1  " + (int)mean1[24][0] + " ");
		YIntervalSeries cluster2 = new YIntervalSeries("Cluster2  " + (int)mean2[24][0] + " ");
//		YIntervalSeries cluster3 = new YIntervalSeries("Cluster3  " + (int)mean3[24][0] + " ");
//		YIntervalSeries cluster4 = new YIntervalSeries("Cluster4  " + (int)mean4[24][0] + " ");
//		YIntervalSeries cluster5 = new YIntervalSeries("Cluster5  " + (int)mean5[24][0] + " ");
		
		double a0, a1, a2;

		for (int i = 0; i < 24; i++){
			a0 = mean0[i][0]-mean0[i][1];
			if (a0 < 0) { a0 = 0; }
			a1 = mean1[i][0]-mean1[i][1];
			if (a1 < 0) { a1 = 0; }
			a2 = mean2[i][0]-mean2[i][1];
			if (a2 < 0) { a2 = 0; }
//			a3 = mean3[i][0]-mean3[i][1];
//			if (a3 < 0) { a3 = 0; }
//			a4 = mean4[i][0]-mean4[i][1];
//			if (a4 < 0) { a4 = 0; }

			cluster0.add(i, mean0[i][0], a0, mean0[i][0]+mean0[i][1]);
			cluster1.add(i, mean1[i][0], a1, mean1[i][0]+mean1[i][1]);
			cluster2.add(i, mean2[i][0], a2, mean2[i][0]+mean2[i][1]);
//			cluster3.add(i, mean3[i][0], a3, mean3[i][0]+mean3[i][1]);
//			cluster4.add(i, mean4[i][0], a4, mean4[i][0]+mean4[i][1]);
//			cluster5.add(i, mean5[i][0], mean5[i][0]-mean5[i][1], mean5[i][0]+mean5[i][1]);
		}

		dataset.addSeries(cluster0);
		dataset.addSeries(cluster1);
		dataset.addSeries(cluster2);
//		dataset.addSeries(cluster3);
//		dataset.addSeries(cluster4);
//		dataset.addSeries(cluster5);

		return dataset;

	}
	
	private static JFreeChart createChart(XYDataset data) throws Exception
	{		
		JFreeChart jfreechart = ChartFactory.createXYLineChart("DeviationRenderer", 
				"Time", "Mean Bikes In", data, PlotOrientation.VERTICAL, true, true, false);
        jfreechart.setBackgroundPaint(Color.white);
        XYPlot xyplot = (XYPlot)jfreechart.getPlot();
        xyplot.setBackgroundPaint(Color.white);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        xyplot.setDomainGridlinePaint(Color.white);
        xyplot.setRangeGridlinePaint(Color.black);
        DeviationRenderer deviationrenderer = new DeviationRenderer(true, false);
        deviationrenderer.setSeriesStroke(0, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesStroke(1, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesStroke(2, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesStroke(3, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesStroke(4, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesStroke(5, new BasicStroke(3F, 1, 1));
        deviationrenderer.setSeriesFillPaint(0, new Color(255, 200, 200));
        deviationrenderer.setSeriesFillPaint(1, new Color(200, 200, 255));
        deviationrenderer.setSeriesFillPaint(2, new Color(200, 255, 200));
        deviationrenderer.setSeriesFillPaint(3, Color.yellow);
        deviationrenderer.setSeriesFillPaint(4, Color.magenta);
        deviationrenderer.setSeriesFillPaint(5, Color.cyan);
        xyplot.setRenderer(deviationrenderer);
        NumberAxis numberaxis = (NumberAxis)xyplot.getRangeAxis();
        numberaxis.setAutoRangeIncludesZero(false);
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return jfreechart;

	}

	public static void main(String args[])
	{

		try {

			ClustersGraph xysplinerenderer = new ClustersGraph("Clusters Graph");
			xysplinerenderer.pack();
			RefineryUtilities.centerFrameOnScreen(xysplinerenderer);
			xysplinerenderer.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
}
