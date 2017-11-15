package psicasso.emotion;

import java.util.ArrayList;

import psicasso.aquaintance.EmoObservable;
import psicasso.aquaintance.Observable;
import psicasso.aquaintance.Observer;
import psicasso.motivation.needs.CertaintyTank;
import psicasso.motivation.needs.CompetenceTank;

/**
 * 
 *
 * @author Mike Imhof
 */
public class EmotionalStates implements EmoObservable, Observer {
	
	// Observable variables
	private ArrayList<Observer> observer;
	
	// Emotional state variables
	private double resolutionLevel;
	private double activation;
	
	// Demand variables
	private double comDemand;
	private double cerDemand;
	private double totalDemandOld;
	private double totalDemandNew;
	
	// Activation variables
	private final static double WEIGHT = 1.5;
	private final static double MINACTIVATION = 0;
	private final static double MAXACTIVATION = 1;
		
	public EmotionalStates(){
		init();
	}
	
	private void init(){
		observer = new ArrayList<Observer>();
		activation = 0.5;
		calculateResolutionLevel();
	}
		
	public void addObserver(Observer o) {
		observer.add(o);
	}

	public void deleteObserver(Observer o) {
		int i = observer.indexOf(o);
		if (i >= 0){
			observer.remove(i);
		}
	}

	public void notifyObservers() {
		for (int i = 0; i < observer.size(); i++){
			Observer o = observer.get(i);
			o.update(this);
		}
	}
	
	public void update(Observable obs) {
		if(obs instanceof CompetenceTank){
			comDemand = ((CompetenceTank) obs).getDemand();
		} else if (obs instanceof CertaintyTank){
			cerDemand = ((CertaintyTank) obs).getDemand();
		}
	}
	
	private void calculateActivation(){
		totalDemandOld = totalDemandNew;
		
		totalDemandNew = (comDemand + cerDemand) / 2;
		
		double totalDemandDiff = totalDemandNew - totalDemandOld;
		if(totalDemandDiff >= 0){
			activation = activation + (WEIGHT * totalDemandDiff);
			if(activation >= MAXACTIVATION){
				activation = MAXACTIVATION;
			}
		} else if(totalDemandDiff < 0) {
			activation = activation - (WEIGHT * totalDemandDiff * -1);
			if(activation <= MINACTIVATION){
				activation = MINACTIVATION;
			}
		}
	}
	
	private void calculateResolutionLevel(){
		resolutionLevel = 1 - Math.sqrt(activation);
	}
	
	public void updateStates(){
		calculateActivation();
		calculateResolutionLevel();
		notifyObservers();
	}
	
	public double getResolutionLevel() {
		return resolutionLevel;
	}

	public double getActivation() {
		return activation;
	}

}
