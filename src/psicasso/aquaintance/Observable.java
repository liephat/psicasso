package psicasso.aquaintance;

/**
 * A class can implement the Observable interface to represent an observable object.
 * 
 * An observable object can have one or more observers. An observer may be any object 
 * that implements interface <code>Observer</code>. After an observable instance changes, 
 * an application calling the <code>Observable</code>'s <code>notifyObservers</code> method 
 * causes all of its observers to be notified of the change by a call to their update method.
 * 
 * @author Mike Imhof
 */
public interface Observable {

	public void addObserver(Observer o);
	public void deleteObserver(Observer o);
	public void notifyObservers();
		
}
