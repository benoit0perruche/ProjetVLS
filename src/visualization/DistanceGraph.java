package visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
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
import visualizationTools.Intervals;

public class DistanceGraph extends ApplicationFrame {
	

	private static final long serialVersionUID = 1L;
	
	static String RESOURCES_PATH = "";
	static String csv_File = "trips(copie).csv";
	static String config_File = "static.json";

	public DistanceGraph(String s, int nbIntervals) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
        super(s);
        JPanel jpanel = createDemoPanel(nbIntervals);
        jpanel.setPreferredSize(new Dimension(500*2, 270*2));
        setContentPane(jpanel);
    }
	
	public static JPanel createDemoPanel(int nbIntervals) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{

        JFreeChart jfreechart = ChartFactory.createScatterPlot("Distance Graph",
            "Distance", 
            "Trajets / paires de stations", 
            samplexydataset(nbIntervals), 
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
	
private static XYDataset samplexydataset(int nbIntervals) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
    	
		Intervals.getDistIntervalsDist(RESOURCES_PATH+csv_File, nbIntervals);
		double[] yop = Intervals.distanceStat(RESOURCES_PATH+config_File);
		double[] intervals = Intervals.deletezeroIntervals(Intervals.getDistIntervals());
		int[] a = Intervals.distanceCount(yop,intervals);
		int[] b = Intervals.tripCount(RESOURCES_PATH+csv_File,intervals);
		
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series0 = new XYSeries(nbIntervals + " intervalles");
		
		for(int i=0; i<intervals.length-1;i++){
			System.out.println(a[i] + " intervalle " + i);
			series0.add(Math.log(((intervals[i+1]-intervals[i])/2)+intervals[i]+1)
					/ Math.log(((intervals[intervals.length-1]-intervals[intervals.length-2])/2)+intervals[intervals.length-2]+1), 
					(b[i]/a[i]));
			
		}

        xySeriesCollection.addSeries(series0);
        
        return xySeriesCollection;
    }

    public static void main(String args[]) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
    	DistanceGraph scatterplotdemo4 = new DistanceGraph("Localisation clusters", 24);
        scatterplotdemo4.pack();
        RefineryUtilities.centerFrameOnScreen(scatterplotdemo4);
        scatterplotdemo4.setVisible(true);
    }
}
