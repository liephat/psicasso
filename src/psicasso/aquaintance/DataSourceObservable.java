package psicasso.aquaintance;

/**
 * 
 *
 * @author Mike
 */
public interface DataSourceObservable extends Observable {
	public double getCertaintyContent();
	public double getCertaintyDemand();
	public double getCompetenceContent();
	public double getCompetenceDemand();
	public double getActivation();
	public double getResolutionLevel();
	public double getTotalDemand();
}
