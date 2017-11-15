package psicasso.perception;

import java.util.ArrayList;

import psicasso.aquaintance.EmoObservable;
import psicasso.aquaintance.HyPerceptObservable;
import psicasso.aquaintance.Observable;
import psicasso.aquaintance.Observer;
import psicasso.aquaintance.SenseObservable;
import psicasso.aquaintance.Trace;
import psicasso.memory.MemoryController;

/**
 * 
 *
 * @author Mike Imhof
 */

public class HyPercept implements Observer, HyPerceptObservable {
		
	private MemoryController memoryCtrl;
	
	private ArrayList<Observer> observer;
	
	private double ei;
	private String actualProbeID;
	
	public HyPercept(Vision vision, NeedPercept needPercept){
		init();
		vision.addObserver(this);
		needPercept.addObserver(this);
	}
	
	private void init(){
		observer = new ArrayList<Observer>();
	}
	
	public Trace identification(Trace probe){
		int[] tmp = new int[]{0, 0};
		double ei = memoryCtrl.getEchoIntensity(Trace.merge(probe, new Trace(tmp)));
		System.out.println("Echo intensity: " + ei);
				
		this.ei = ei;
		actualProbeID = probe.getId();
		notifyObservers();
		
		return null;
	}
					
	// TODO Update-Methode soll den Auflösungsgrad weiterleiten
	public void update(Observable obs) {
		if(obs instanceof NeedPercept){
			SenseObservable sobs = (SenseObservable) obs;
			sobs.getCurrentTrace();
		} else if(obs instanceof Vision){
			SenseObservable sobs = (SenseObservable) obs;
			identification(sobs.getCurrentTrace());
		}
	}
	
	public void update(EmoObservable obs){
		
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

	public void setMemoryCtrl(MemoryController memoryCtrl) {
		this.memoryCtrl = memoryCtrl;
	}

	/**
	 * Returns certainty amount of the last identification.
	 * 
	 * @return the certainty
	 */
	public double calculateCertaintySignal() {
		if(ei == 0.0){
			return 0.0;
		} else {
			return ei / (memoryCtrl.getMemorySize() * 2.1);
		}
		
	}
	
	public String getActualProbeID() {
		return actualProbeID;
	}
	
	public double getEchoIntensity() {
		return ei;
	}
	
}
