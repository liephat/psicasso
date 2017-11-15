package psicasso.motivation.needs;

/**
 * 
 *
 * @author Mike Imhof
 */
public class SustainmentTank extends Tank {

	public SustainmentTank(){
		super();
	}
	
	/**
	 * @param nominalValue
	 * @param actualContent
	 * @param leakFactor
	 */
	public SustainmentTank(double nominalValue, double actualContent, double leakFactor) {
		super(nominalValue, actualContent, leakFactor);
	}

}
