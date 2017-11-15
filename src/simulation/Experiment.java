package simulation;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import org.encog.ml.data.basic.BasicMLDataSet;

import psicasso.agent.Agent;
import psicasso.aquaintance.Trace;
import util.imageprocessing.ReadImageCSV;
import util.logging.DataSource;
import util.neuralnet.DataContent;
import util.neuralnet.SimpleSOM;

/**
 * 
 *
 * @author Mike Imhof
 */
public class Experiment {
	
	// Neural net settings
	private final static String HOMEDIRECTORY = new File("").getAbsolutePath();
	private final static String DATAFILENAME = "allImagesCoarseness.csv";
	private final static String DATAFILEPATH = "\\datasets\\";
	
	private BasicMLDataSet trainingSet;
	private SimpleSOM som;
	
	private Trace[] traces;
	private static final double THREESTEPTHRESHOLD = 1d;	
	
	// Data settings
	private ReadImageCSV rcsv;
	
	// Agent settings
	private Agent agent;
	
	// Simulation settings
	private DataSource dataSource;
    private SimulationTimer simTimer;
    
    // Other
    private int time;
		
	public Experiment(DataSource dataSource){
		this.dataSource = dataSource;
		init();
		System.out.println("Experiment created.");
	}
	
	public void init(){
		agent = new Agent(dataSource);
		loadImageData();
		setUpNeuralNet();
	}
	
	public void loadImageData(){
		System.out.println("Loading image data from " + DATAFILEPATH + DATAFILENAME);
		rcsv = new ReadImageCSV(DATAFILENAME, DATAFILEPATH);
		
		int imageCount = rcsv.getDatasetCount();
		
		rcsv = new ReadImageCSV(DATAFILENAME, DATAFILEPATH);
		
		System.out.println("Building training set...");
		trainingSet = buildTrainingSet(new DataContent[] { DataContent.RHIGH, DataContent.RMID, DataContent.GHIGH, DataContent.GMID, 
				DataContent.BHIGH, DataContent.BMID, DataContent.LUMCONTRAST, DataContent.AVGCONTRAST, 
				DataContent.DIRECTIONALITY, DataContent.COARSENESS }, imageCount);
		
	}
	
	public BasicMLDataSet buildTrainingSet(DataContent[] dc, int count) {
		return rcsv.getData(dc, count);
	}
		
	public void setUpNeuralNet(){
		som = new SimpleSOM();
		som.setOptimalTraining(true);
		som.setTraceThresholdFactor(THREESTEPTHRESHOLD);
		som.load(trainingSet);
	}
	
	public void trainNeuralNet() {
		ArrayList<int[]> intTraces;
		setUpNeuralNet();
		
		som.learn();		
		som.prepareDataForFirstStatistics();		
		som.calculateStatistics();
		
		intTraces = som.makeFirstTraces();
		traces = new Trace[intTraces.size()];
		for(int i = 0; i < intTraces.size(); i++){
			Trace t = new Trace(intTraces.get(i));
			t.setId("VT" + i);
			traces[i] = t;
		}
	}
	
	public void run() {		
		trainNeuralNet();
		//agent.feedMemory(buildRandomSetOfTraces(10000));
						
    	this.simTimer = new SimulationTimer(this);
    	simTimer.setPeriod(10);
    	
    	System.out.println("Run Experiment...");
    	
    	simTimer.start();
	}
	
	public Trace[] buildRandomSetOfTraces(int size){
		Trace[] randomSet = new Trace[size];
		for(int i = 0; i < size; i++){
			Trace randomVisualTrace = chooseRandomTraceFromFirstSet();
			Trace trace = Trace.merge(randomVisualTrace, new Trace(new int[]{0,0}));
			trace.setId(randomVisualTrace.getId());
			randomSet[i] = trace;		
		}		
		return randomSet;
	}
	
	public Trace chooseRandomTraceFromFirstSet(){
		Random rnd = new Random();
		return traces[rnd.nextInt(1024)];
	}
	
	public Trace chooseRandomTraceFromSecondSet(){
		Random rnd = new Random();
		return traces[rnd.nextInt(687) + 1024];
	}
	
	public void refresh() {
        if(time >= 5000){
        	agent.refresh(chooseRandomTraceFromSecondSet());
        } else {
        	agent.refresh(chooseRandomTraceFromFirstSet());
        }
		
        dataSource.refresh();
        time++;
	}
	
	public void stop() {
		simTimer.cancel();
	}

	public Agent getAgent() {
		return agent;
	}
	
	public DataSource getDataSource(){
		return dataSource;
	}
	
}
