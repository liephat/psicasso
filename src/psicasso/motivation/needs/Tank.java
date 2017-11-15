package psicasso.motivation.needs;

import java.util.ArrayList;

import psicasso.aquaintance.Observer;
import psicasso.aquaintance.TankObservable;

/**
 * 
 *
 * @author Mike Imhof
 */
public abstract class Tank implements TankObservable {
	
	// Attributes of the tank
	private static double SIZE = 1;				// The tank's size is always 1
	private static double UNIT = 0.01d;			// A unit for filling or draining once is defined here
	private double nominalValue;
	private double content;
	private double leakFactor;
	private double demand;
	private double signalStrengthFactor;
	
	// Variables for observer pattern
	private ArrayList<Observer> observer;
	private double lastChange;
	
	/**
	 * This constructor creates a tank with default attributes.
	 */
	public Tank(){
		init();
		// Set attributes to default values
		this.setNominalValue(0.8);
		this.setContent(0.6);
		this.setLeakFactor(0.1d);
	}
	
	public Tank(double nominalValue, double actualContent, double leakFactor){
		init();
		this.setNominalValue(nominalValue);
		this.setContent(actualContent);		
		this.setLeakFactor(leakFactor);
	}
	
	public void init(){
		observer = new ArrayList<Observer>();
		lastChange = 0;
	}
	
	public void addObserver(Observer o){
		observer.add(o);
	}
	
	public void notifyObservers(){
		for (int i = 0; i < observer.size(); i++){
			Observer o = observer.get(i);
			o.update(this);
		}
		lastChange = 0;
	}
	
	public void deleteObserver(Observer o){
		int i = observer.indexOf(o);
		if (i >= 0){
			observer.remove(i);
		}
	}
		
	protected void valuesChanged(){
		notifyObservers();
	}
	
	/**
	 * Fills a given amount into the tank.
	 * 
	 * @param amount Absolute Amount. Must be a value between 0 and 1.
	 */
	public void fill(double amount){
		
		setContent(content + signalStrengthFactor*amount);
		this.lastChange = amount;
	}
	
	/**
	 * Fills a given amount of units into the tank.
	 * 
	 * @param units Number of units which have a defined size.
	 */
	public void fill(int units){
		setContent(content + signalStrengthFactor*units*UNIT);
		this.lastChange = units*UNIT;
	}
	
	/**
	 * Lets the tank drain a given amount.
	 * 
	 * @param amount Absolute Amount. Must be a value between 0 and 1.
	 */
	public void drain(double amount){
		setContent(content - amount);
		this.lastChange = amount*-1;
	}
	
	/**
	 * Lets the tank drain a given amount of units.
	 *
	 * @param units Number of units which have a defined size.
	 */
	public void drain(int units){		
		setContent(content - units*UNIT);
		this.lastChange = units*UNIT*-1;
	}
	
	/**
	 * Lets the tank leak one unit multiplied by the leak factor.
	 */
	public void leak(){
		setContent(content - UNIT * leakFactor);
	}
	
	public double getCurrentContent() {
		return content;
	}

	protected void setContent(double actualContent) {
		if (actualContent > SIZE){
			this.content = SIZE;
		} else if (actualContent < 0){
			this.content = 0;
		} else {
			this.content = actualContent;
		}
		calculateDemand();
		calculateSignalStrengthFactor();
		valuesChanged();
	}

	public double getNominalValue() {
		return nominalValue;
	}
	
	protected void setNominalValue(double nominalValue) {
		double minNominalValue = UNIT;
		if (nominalValue > SIZE){
			this.nominalValue = SIZE;
		} else if (nominalValue < minNominalValue){
			this.nominalValue = minNominalValue;
		} else {
			this.nominalValue = nominalValue;
		}	
	}

	public double getLeakFactor() {
		return leakFactor;
	}
	
	protected void setLeakFactor(double leakFactor) {
		if(leakFactor < 0){
			this.leakFactor = 0;
		} else if (leakFactor > 1){
			this.leakFactor = 1;
		} else {
			this.leakFactor = leakFactor;
		}	
	}
	
	public void calculateDemand(){
		demand = nominalValue - content;
	}
	
	public void calculateSignalStrengthFactor(){
		double schwingungsbreite = 1.0;
		double neurotizismus = 1.0;
		double grundstimmung = 0.0;
		double yShift = 1.0;
		signalStrengthFactor = (schwingungsbreite * Math.tanh(neurotizismus * -this.demand + grundstimmung) + yShift);
		setLeakFactor(signalStrengthFactor / 5);
	}
	
	public double getDemand() {
		return demand;
	}
	
	public double getLastChange() {
		return lastChange;
	}

	public void printActualParameters(){
		System.out.println(this.toString());
		System.out.print("Actual Content: " + this.content);
		System.out.print(", Nominal Value: " + this.nominalValue);
		System.out.print(", Demand: " + this.demand);
		System.out.println(", Leak Factor: " + this.leakFactor);
	}
	
}
