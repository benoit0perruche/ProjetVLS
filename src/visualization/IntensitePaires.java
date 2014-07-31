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

public class IntensitePaires extends ApplicationFrame {

	private static final long serialVersionUID = 1L;
	
	static String RESOURCES_PATH = "";
	static String csv_File = "quart.csv";
	static String config_File = "static.json";

	public IntensitePaires(String s) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
        super(s);
        JPanel jpanel = createDemoPanel();
        jpanel.setPreferredSize(new Dimension(500*2, 270*2));
        setContentPane(jpanel);
    }
	
	public static JPanel createDemoPanel() throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{

        JFreeChart jfreechart = ChartFactory.createScatterPlot("Log de l'intensite par paire",
            "Distance (en m)", 
            "Intensite (flux)", 
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
        xydotrenderer.setDotHeight(2);
        xydotrenderer.setDotWidth(2);
        xyPlot.setRenderer(xydotrenderer);
        xydotrenderer.setSeriesShape(0, cross);

        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        xyPlot.setBackgroundPaint(Color.white);

        return new ChartPanel(jfreechart);
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
	
private static XYDataset samplexydataset() throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
    	
		double[][] paires = Intervals.getPaires(RESOURCES_PATH+config_File);
		int[][] intens = Intervals.intensPaires(RESOURCES_PATH+csv_File, paires);
		int maxIntens = maxIntensity(intens);
		
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series0 = new XYSeries("Intensite pour chaque paire de station");
		
		for(int i=0; i<paires.length;i++){
			for (int j = 0; j<i; j++){
//				if (paires[i][j]<=12000){
			series0.add(Math.log(paires[i][j]), Math.log(intens[i][j]+intens[j][i]+1)/Math.log(maxIntens+1));
//				}
			}
		}

        xySeriesCollection.addSeries(series0);
        
        return xySeriesCollection;
    }

    public static void main(String args[]) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
    	IntensitePaires scatterplotdemo4 = new IntensitePaires("Intensite par paire");
        scatterplotdemo4.pack();
        RefineryUtilities.centerFrameOnScreen(scatterplotdemo4);
        scatterplotdemo4.setVisible(true);
    }

}
