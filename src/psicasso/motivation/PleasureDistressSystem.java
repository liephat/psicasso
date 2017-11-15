package psicasso.motivation;

import java.util.ArrayList;

import psicasso.aquaintance.Observable;
import psicasso.aquaintance.Observer;
import psicasso.aquaintance.PleasureDistressObservable;
import psicasso.aquaintance.TankObservable;
import psicasso.motivation.needs.Tank;

/**
 * 
 *
 * @author Mike
 */
public class PleasureDistressSystem implements PleasureDistressObservable, Observer {

	// Pleasure Distress variables
	private double pdSignal;
	
	// Need variables
	private Tank[] connectedTanks;
	
	// Variables for observer pattern
	ArrayList<Observer> observers;
	
	public PleasureDistressSystem(Tank[] connectedTanks){
		this.connectedTanks = connectedTanks;
		
		init();
	}
	
	private void init(){
		observers = new ArrayList<Observer>();
		for(int i = 0; i < connectedTanks.length; i++){
			connectedTanks[i].addObserver(this);
		}
		pdSignal = 0;
	}
	
	@Override
	public void update(Observable obs) {
		TankObservable tobs = (TankObservable) obs;
		pdSignal = tobs.getLastChange();		// calculation of a pleasure distress signal
		if(tobs.getDemand() > 0){				// Check if demand was reduced
			notifyObservers();
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
	public double getPleasureDistressSignal() {
		return pdSignal;
	}
	
}
