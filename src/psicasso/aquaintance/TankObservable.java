package psicasso.aquaintance;

/**
 * @author Mike Imhof
 *
 */
public interface TankObservable extends Observable {
		
	public double getCurrentContent();
	public double getDemand();
	public double getLastChange();
	
}
