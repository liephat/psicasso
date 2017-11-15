package simulation;


import java.util.Timer;

import simulation.timertasks.ExperimentTimerTask;

/**
 * The simulation timer extends the timer class. It's purpose is to repeat certain timer 
 * tasks after a specified period. The timer tasks are e.g. important agent
 * processes like leaking of the tanks, or creating new memory traces which must loop
 * within every time interval (default 3000 ms).
 *
 * @author Mike Imhof
 */

public class SimulationTimer extends Timer {
	
	// 
	private long period = 3000;
	
	// Timer tasks
	ExperimentTimerTask ett;
	
	public SimulationTimer(Experiment e){				
		ett = new ExperimentTimerTask(e);
	}
	
	public void start(){		
		this.schedule(ett, 0, period);
	}

	/**
	 * Sets the length of one timer interval.
	 * 
	 * @param period Length of one timer interval.
	 */
	
	public void setPeriod(long period) {
		this.period = period;
	}

}
