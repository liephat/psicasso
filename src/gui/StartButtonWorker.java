/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.SwingWorker;

import psicasso.agent.Agent;
import simulation.Experiment;
import simulation.SimulationTimer;
import util.logging.DataSource;
/**
 *
 * @author Mike Imhof
 */
public class StartButtonWorker extends SwingWorker<Void, Void> {
    
    private Experiment experiment;
    
    public StartButtonWorker(DataSource dataSource){
        super();
        experiment = new Experiment(dataSource);
    }
 
    
    protected Void doInBackground() throws Exception {
    	experiment.run();
    	
        return null;
    }
    
    public Experiment getExperiment(){
    	return experiment;
    }
    
}
