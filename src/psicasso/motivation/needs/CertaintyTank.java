package psicasso.motivation.needs;

import psicasso.aquaintance.Observable;
import psicasso.aquaintance.Observer;
import psicasso.perception.HyPercept;

/**
 * 
 *
 * @author Mike Imhof
 */
public class CertaintyTank extends Tank implements Observer {
		
	public CertaintyTank(){
		super();
	}
	
	/**
	 * @param nominalValue
	 * @param actualContent
	 * @param leakFactor
	 */
	public CertaintyTank(double nominalValue, double actualContent, double leakFactor) {
		super(nominalValue, actualContent, leakFactor);
	}

	public void update(Observable obs) {
		if(obs instanceof HyPercept){
			HyPercept hobs = (HyPercept) obs;
			
			this.fill(hobs.calculateCertaintySignal());
		}		
	}

}
