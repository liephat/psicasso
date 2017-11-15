package psicasso.perception;

import java.util.ArrayList;

import psicasso.aquaintance.Observable;
import psicasso.aquaintance.Observer;
import psicasso.aquaintance.SenseObservable;
import psicasso.aquaintance.TankObservable;
import psicasso.aquaintance.Trace;
import psicasso.motivation.needs.CertaintyTank;
import psicasso.motivation.needs.CompetenceTank;

/**
 * 
 *
 * @author Mike Imhof
 */
public class NeedPercept implements SenseObservable, Observer{
	
	private Trace actualTrace;
	
	private ArrayList<Double> cerPressureHistory;
	private ArrayList<Double> comPressureHistory;
	
	// Variables for the observer pattern
	private ArrayList<Observer> observer;
	
	public NeedPercept(){
		init();
	}
	
	private void init(){
		observer = new ArrayList<Observer>();
		cerPressureHistory = new ArrayList<Double>();
		cerPressureHistory.add(0.0);
		cerPressureHistory.add(0.0);
		comPressureHistory = new ArrayList<Double>();
		comPressureHistory.add(0.0);
		comPressureHistory.add(0.0);
	}
	/**
	 * Creates a NeedTrace which encodes the pressure reduction or 
	 * growth of sustainment (1), certainty (2) and competence (3)
	 * 
	 * @return NeedTrace
	 */
	public void createTrace(){
		int[] content = new int[2];
				
		if(cerPressureHistory.get((cerPressureHistory.size()-1)) < 
				cerPressureHistory.get(cerPressureHistory.size()-2)){
			content[0] = 1;
		} else if (cerPressureHistory.get((cerPressureHistory.size()-1)) > 
				cerPressureHistory.get(cerPressureHistory.size()-2)){
			content[0] = -1;
		} else {
			content[0] = 0;
		}
		
		if(comPressureHistory.get((comPressureHistory.size()-1)) < 
				comPressureHistory.get(comPressureHistory.size()-2)){
			content[1] = 1;
		} else if (comPressureHistory.get((comPressureHistory.size()-1)) > 
				comPressureHistory.get(comPressureHistory.size()-2)){
			content[1] = -1;
		} else {
			content[1] = 0;
		}
		actualTrace = new Trace(content);
		System.out.println(actualTrace.getId() + "\t" + actualTrace.toString());
		notifyObservers();
	}

	public void addObserver(Observer o) {
		observer.add((Observer)o);		
	}

	public void deleteObserver(Observer o) {
		int i = observer.indexOf((Observer)o);
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
		TankObservable tobs = (TankObservable) obs;
		if(obs instanceof CompetenceTank){
			comPressureHistory.add(tobs.getDemand());			
		} else if (obs instanceof CertaintyTank){
			cerPressureHistory.add(tobs.getDemand());			
		}
	}

	public Trace getCurrentTrace() {
		return actualTrace;
	}
	
}
