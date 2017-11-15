package psicasso.motivation.needs;

import psicasso.aquaintance.Observable;
import psicasso.aquaintance.Observer;
import psicasso.aquaintance.PleasureDistressObservable;


/**
 * 
 *
 * @author Mike Imhof
 */
public class CompetenceTank extends Tank implements Observer {

	/**
	 * @param nominalValue
	 * @param actualContent
	 * @param leakFactor
	 */
	
	public CompetenceTank(double nominalValue, double actualContent, double leakFactor) {
		super(nominalValue, actualContent, leakFactor);
	}
	
	public CompetenceTank(){
		super();
	}
	
	public void update(Observable obs){
		if(obs instanceof PleasureDistressObservable){
			double pdSignal = ((PleasureDistressObservable) obs).getPleasureDistressSignal();
			if(pdSignal < 0) {
				this.drain(pdSignal);
			} else {
				this.fill(pdSignal);
			}
			
		}
	}

}
