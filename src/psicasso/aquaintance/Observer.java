package psicasso.aquaintance;

/**
 * A class can implement the Observer interface when it wants to be informed 
 * of changes in observable objects.
 *
 * @author Mike Imhof
 */
public interface Observer {
	
	public void update(Observable obs);
	
}
