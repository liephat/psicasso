package psicasso.perception;

import java.util.ArrayList;
import java.util.Random;

import psicasso.aquaintance.Observer;
import psicasso.aquaintance.SenseObservable;
import psicasso.aquaintance.Trace;

/**
 * 
 *
 * @author Mike Imhof
 */
public class Vision implements SenseObservable {
		
	// Other settings
	private ArrayList<Trace> traces;
	private Trace actualTrace;	
			
	private ArrayList<Observer> observer;
	
	public Vision(){
		observer = new ArrayList<Observer>();		
	}
	
	public void lookAt(Trace trace){
		actualTrace = trace;
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

	public Trace getCurrentTrace() {
		return actualTrace;
	}
	
	public void setActualTrace(Trace trace) {
		actualTrace = trace;
		System.out.println(actualTrace.getId() + "\t" + actualTrace.toString());
		notifyObservers();
	}

}
