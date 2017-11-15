package psicasso.aquaintance;

/**
 * 
 *
 * @author Mike Imhof
 */
public interface HyPerceptObservable extends Observable {
	
	public double calculateCertaintySignal();
	public double getEchoIntensity();
	public String getActualProbeID();

}
