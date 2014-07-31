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

import dataAnalysis.LireArff;
import dataAnalysis.MatriceTrajets;


@SuppressWarnings("serial")
public class ClusterLocalisation extends ApplicationFrame {
	
	private static final String FILE_PATH = "/home/benoit/Documents/Polytech4A/Stage/Donnees/Washington";
	static String static_File = "/capital-bikeshare_Config.json";
	private static final String ARFF_FILE_PATH = "/home/benoit/Documents/Polytech4A/Stage/Weka/Washington";
	private static final String FILE_NAME_IN = "/bikesInWeek.arff";
	private static final String FILE_NAME_OUT = "/bikesOutWeek.arff";
	
	private static double latitude = 38.8935965;
	private static double longitude = -77.014576;
	
	public ClusterLocalisation(String s) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
        super(s);
        JPanel jpanel = createDemoPanel();
        jpanel.setPreferredSize(new Dimension(500*2, 270*2));
        setContentPane(jpanel);
    }

    public static JPanel createDemoPanel() throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{

        JFreeChart jfreechart = ChartFactory.createScatterPlot("Localisation des clusters In",
            "Longitude", 
            "Latitude", 
            samplexydataset(), 
            PlotOrientation.VERTICAL, 
            true, 
            true, 
            false);
        Shape cross = ShapeUtilities.createDiagonalCross(3, 1);

        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setBaseShape(cross);
        renderer.setBasePaint(Color.red);
        //changing the Renderer to XYDotRenderer
        //xyPlot.setRenderer(new XYDotRenderer());
        XYDotRenderer xydotrenderer = new XYDotRenderer();
        xydotrenderer.setDotHeight(6);
        xydotrenderer.setDotWidth(6);
        xyPlot.setRenderer(xydotrenderer);
        xydotrenderer.setSeriesShape(0, cross);

        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        xyPlot.setBackgroundPaint(Color.white);

        return new ChartPanel(jfreechart);
    }
    
    /**
     * @param clusters
     * @param inOut = 0 for clusters In, 1 for Out
     * @return the number of different clusters
     */
    @SuppressWarnings("unused")
	private static int numClusters (int[][] clusters, int inOut){
    	int res = 0;
    	for (int i = 0; i<clusters.length; i++){
    		res = Math.max(res, clusters[i][inOut]);
    	}
    	res ++;
    	return res;
    }

    private static XYDataset samplexydataset() throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
    	
    	double[][] indLocStations = MatriceTrajets.indexLoc(FILE_PATH+static_File);
    	int[][] clustersStat = dataAnalysis.StationType.clusterStats(LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_IN,24),
				LireArff.lireArff(ARFF_FILE_PATH+FILE_NAME_OUT,24));
//    	int nbClusters = numClusters(clustersStat, 0);

        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        XYSeries series0 = new XYSeries("CLuster0");
        XYSeries series1 = new XYSeries("CLuster1");
        XYSeries series2 = new XYSeries("CLuster2");
        XYSeries series3 = new XYSeries("CLuster3");
        XYSeries series4 = new XYSeries("CLuster4");
        
        for (int i = 0; i<clustersStat.length; i++){
        	switch (clustersStat[i][0]) {
        	case 0 : series0.add(indLocStations[i][1]-longitude, indLocStations[i][2]-latitude);
        	case 1 : series1.add(indLocStations[i][1]-longitude, indLocStations[i][2]-latitude);
        	case 2 : series2.add(indLocStations[i][1]-longitude, indLocStations[i][2]-latitude);
        	case 3 : series3.add(indLocStations[i][1]-longitude, indLocStations[i][2]-latitude);
        	case 4 : series4.add(indLocStations[i][1]-longitude, indLocStations[i][2]-latitude);
        	}
        }


        xySeriesCollection.addSeries(series0);
        xySeriesCollection.addSeries(series1);
        xySeriesCollection.addSeries(series2);
        xySeriesCollection.addSeries(series3);
        xySeriesCollection.addSeries(series4);
        
        return xySeriesCollection;
    }

    public static void main(String args[]) throws FileNotFoundException, IOException, ParseException, org.json.simple.parser.ParseException{
    	ClusterLocalisation scatterplotdemo4 = new ClusterLocalisation("Localisation clusters");
        scatterplotdemo4.pack();
        RefineryUtilities.centerFrameOnScreen(scatterplotdemo4);
        scatterplotdemo4.setVisible(true);
    }
}
