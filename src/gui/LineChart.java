package gui;
import java.awt.Color;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import psicasso.aquaintance.DataSourceObservable;
import psicasso.aquaintance.Observable;
import psicasso.aquaintance.Observer;

import util.logging.DataSource;


/**
 * A simple demonstration application showing how to create a line chart using data from an
 * 
 */
public class LineChart implements Observer {
	
	private XYSeries totalDemand;
	private XYSeries cerContent;
	private XYSeries cerDemand;
	private XYSeries comContent;
	private XYSeries comDemand;
	private XYSeries activation;
	private XYSeries resolutionLevel;
		
    private int XValue;
    
	private XYDataset dataset;
	private JFreeChart chart;
	private ChartPanel chartPanel;
	
	/**
     * Creates a new demo.
     *
     * @param title  the frame title.
     */
    public LineChart() {
        init();
    }
    
    private void init(){
    	totalDemand = new XYSeries("Total Demand");
    	cerContent = new XYSeries("CerContent");
    	cerDemand = new XYSeries("CerDemand");
    	comContent = new XYSeries("ComContent");
    	comDemand = new XYSeries("ComDemand");
    	activation = new XYSeries("Activation");
    	resolutionLevel = new XYSeries("Resolution Level");
    	    	    	
    	XValue = 1;
    	
    	dataset = createDataset();
    	chart = createChart(dataset);
    	chartPanel = new ChartPanel(chart);
        
    }
        
    /**
     * Creates a sample dataset.
     * 
     * @return a sample dataset.
     */
    private XYDataset createDataset() {
        final XYSeriesCollection dataset = new XYSeriesCollection();
        //dataset.addSeries(totalDemand);
        dataset.addSeries(cerContent);
        //dataset.addSeries(cerDemand);
        dataset.addSeries(comContent);
        //dataset.addSeries(comDemand);
        dataset.addSeries(activation);
        dataset.addSeries(resolutionLevel);
                
        return dataset;
    }
    
    /**
     * Creates a chart.
     * 
     * @param dataset  the data for the chart.
     * 
     * @return a chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "PsiCasso Statistics",      			// chart title
            "Time",               // x axis label
            "Y",                  // y axis label
            dataset,                  	// data
            PlotOrientation.VERTICAL,
            true,                     	// include legend
            true,                     	// tooltips
            false                     	// urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
        //plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        //plot.setDomainGridlinePaint(Color.white);
        //plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        renderer.setSeriesShapesVisible(2, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;        
    }
        
	@Override
	public void update(Observable obs) {
		if(obs instanceof DataSource){
			DataSourceObservable dsobs = (DataSourceObservable) obs;
			cerContent.add((double) XValue, dsobs.getCertaintyContent());
			cerDemand.add((double) XValue, dsobs.getCertaintyDemand());
			comContent.add((double) XValue, dsobs.getCompetenceContent());
			comDemand.add((double) XValue, dsobs.getCompetenceDemand());
			activation.add((double) XValue, dsobs.getActivation());
			resolutionLevel.add((double) XValue, dsobs.getResolutionLevel());
			totalDemand.add((double) XValue, dsobs.getTotalDemand());
			XValue++;
					
			chartPanel.validate();
			chartPanel.repaint();
		}		
	}
	
	public ChartPanel getChartPanel(){
		return this.chartPanel;
	}

}