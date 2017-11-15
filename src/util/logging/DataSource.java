package util.logging;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

import psicasso.aquaintance.HyPerceptObservable;
import psicasso.aquaintance.Observable;
import psicasso.aquaintance.DataSourceObservable;
import psicasso.aquaintance.EmoObservable;
import psicasso.aquaintance.Observer;
import psicasso.aquaintance.PleasureDistressObservable;
import psicasso.motivation.needs.CertaintyTank;
import psicasso.motivation.needs.CompetenceTank;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This class contains data about the agent components. It helps to monitor
 * the inner states of the agent (e.g. content of the tanks). By extending the 
 * Observable class, e.g. a GUI or a log file is able to use these information.
 * 
 * @author Mike
 */
public class DataSource implements DataSourceObservable, Observer {
    
	DecimalFormat decimalFormat;
	
    private String logOutput = new String("Hier erscheint später der Log-Output"
            + " von Psicasso..");
    
    private WriteDataCSV dwriter;
    
    //-----Logging variables for a log output (e.g. text file or GUI)-----
    // Tanks
    private ArrayList<Double> comContent, cerContent, susContent, comDemand, cerDemand, pleasureDistress, certaintySignal;
    
    // Emotions
    private ArrayList<Double> activation, resolutionLevel;
    
    // Simulation Time
    private int time;
    
    // Memory
    private ArrayList<Double> echoIntensity;
    private ArrayList<String> traceID;
    
    //-----Temporal variables-----
    private String traceIDTmp;
    private double susContentTmp, cerContentTmp, comContentTmp, cerDemandTmp, comDemandTmp, 
    	pdTmp, actTmp, rlTmp, eiTmp, cerSignalTmp;
    
    
    // Observer variables
    private ArrayList<Observer> observers;
    
    public DataSource(){
        super();
        init();
    }
    
    private void init(){    	
    	DecimalFormatSymbols dfs = new DecimalFormatSymbols();
    	dfs.setDecimalSeparator('.');
    	decimalFormat = new DecimalFormat( "0.000000000", dfs);
    	
    	observers = new ArrayList<Observer>();
    	
    	// Initialize all logging variables
    	traceID = new ArrayList<String>();
    	comContent = new ArrayList<Double>();
    	cerContent = new ArrayList<Double>();
    	susContent = new ArrayList<Double>();
    	comDemand = new ArrayList<Double>();
    	cerDemand = new ArrayList<Double>();
    	activation = new ArrayList<Double>();
    	resolutionLevel = new ArrayList<Double>();
    	pleasureDistress = new ArrayList<Double>();
    	echoIntensity = new ArrayList<Double>();
    	certaintySignal = new ArrayList<Double>();
        
        time = 0;
    	
    	// Initialize all temporal variables
    	susContentTmp = 0.0;
    	cerContentTmp = 0.0;
    	comContentTmp = 0.0;
    	cerDemandTmp = 0.0;
    	comDemandTmp = 0.0;
    	pdTmp = 0;
    	rlTmp = 0;
    	actTmp = 0;
    	cerSignalTmp = 0.0;
    }
            
    public String getLogOutput(){
    	return logOutput;
    }
    
    public double getCompetenceContent(){
        return comContentTmp;
    }
    
    public double getCompetenceDemand(){
    	return comDemandTmp;
    }
    
    public double getCertaintyContent(){
        return cerContentTmp;
    }
    
    public double getCertaintyDemand(){
    	return cerDemandTmp;
    }
    
    public double getSustainment(){
        return susContentTmp;
    }
    
    public int getSimulationTime(){
        return time;
    }
    
    public void refresh(){
    	time++;
    	    	
    	this.traceID.add(traceIDTmp);
    	this.susContent.add(susContentTmp);
		this.cerContent.add(cerContentTmp);
		this.cerDemand.add(cerDemandTmp);
		this.comContent.add(comContentTmp);
		this.comDemand.add(comDemandTmp);
		this.pleasureDistress.add(pdTmp);
		this.echoIntensity.add(eiTmp);
		this.certaintySignal.add(cerSignalTmp);
		
		this.activation.add(actTmp);
		this.resolutionLevel.add(rlTmp);
		
		notifyObservers();
		
		pdTmp = 0;
    }
    
    public void writeAllRecordsToCsv(){
    	dwriter = new WriteDataCSV("PsiCassoTestdata.csv", "\\data\\");
    	String[] csvHeader = new String[11];
    	csvHeader[0] = "time";
    	csvHeader[1] = "traceID";
    	csvHeader[2] = "certaintyContent";
    	csvHeader[3] = "certaintyDemand";
    	csvHeader[4] = "competenceContent";
    	csvHeader[5] = "competenceDemand";
    	csvHeader[6] = "pleasureDistressSignal";
    	csvHeader[7] = "activation";
    	csvHeader[8] = "resolutionLevel";
    	csvHeader[9] = "echoIntensity";
    	csvHeader[10] = "certaintySignal";
    	dwriter.writeHeader(csvHeader);
    	
    	for(int i = 0; i < time; i++){
    		String[] s = new String[11];
        	s[0] = Integer.toString(i+1);
        	s[1] = traceID.get(i);
        	s[2] = decimalFormat.format(cerContent.get(i));
        	s[3] = decimalFormat.format(cerDemand.get(i));
        	s[4] = decimalFormat.format(comContent.get(i));
        	s[5] = decimalFormat.format(comDemand.get(i));
        	s[6] = decimalFormat.format(pleasureDistress.get(i));
        	s[7] = decimalFormat.format(activation.get(i));
        	s[8] = decimalFormat.format(resolutionLevel.get(i));
        	s[9] = decimalFormat.format(echoIntensity.get(i));
        	s[10] = decimalFormat.format(certaintySignal.get(i));
        	dwriter.writeRecord(s);
    	}
    	dwriter.close();    	
    }
    
	@Override
	public void update(Observable obs) {
		if(obs instanceof CertaintyTank) {
			CertaintyTank cert = (CertaintyTank) obs;
			cerContentTmp = cert.getCurrentContent();
			cerDemandTmp = cert.getDemand();
		} else if(obs instanceof CompetenceTank) {
			CompetenceTank comt = (CompetenceTank) obs;
			comContentTmp = comt.getCurrentContent();
			comDemandTmp = comt.getDemand();
		} else if(obs instanceof PleasureDistressObservable) {
			PleasureDistressObservable pdobs = (PleasureDistressObservable) obs;
			pdTmp += pdobs.getPleasureDistressSignal();
		} else if(obs instanceof EmoObservable){
			EmoObservable eobs = (EmoObservable) obs;
			actTmp = eobs.getActivation();
			rlTmp = eobs.getResolutionLevel();
		} else if(obs instanceof HyPerceptObservable){
			HyPerceptObservable hobs = (HyPerceptObservable) obs;
			eiTmp = hobs.getEchoIntensity();
			traceIDTmp = hobs.getActualProbeID();
			cerSignalTmp = hobs.calculateCertaintySignal();
		}
	}
	

	@Override
	public void addObserver(Observer o) {
		observers.add(o);		
	}

	@Override
	public void deleteObserver(Observer o) {
		int i = observers.indexOf(o);
		if (i >= 0){
			observers.remove(i);
		}
	}

	@Override
	public void notifyObservers() {
		for (int i = 0; i < observers.size(); i++){
			Observer o = observers.get(i);
			o.update(this);
		}
	}

	@Override
	public double getActivation() {
		return actTmp;
	}

	@Override
	public double getResolutionLevel() {
		return rlTmp;
	}

	@Override
	public double getTotalDemand() {
		return (comDemandTmp + cerDemandTmp)/2;
	}
}
