package simulation.timertasks;

import java.util.TimerTask;

import simulation.Experiment;

/**
 * 
 *
 * @author Mike
 */
public class ExperimentTimerTask extends TimerTask {

	private Experiment experiment;
	
	public ExperimentTimerTask (Experiment experiment){
		this.experiment = experiment;
	}
	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		experiment.refresh();
	}

}
