package psicasso.agent;

import psicasso.aquaintance.Trace;
import psicasso.emotion.EmotionalStates;
import psicasso.memory.MemoryController;
import psicasso.memory.SecondaryMemory;
import psicasso.motivation.PleasureDistressSystem;
import psicasso.motivation.needs.CertaintyTank;
import psicasso.motivation.needs.CompetenceTank;
import psicasso.motivation.needs.Tank;
import psicasso.perception.HyPercept;
import psicasso.perception.NeedPercept;
import psicasso.perception.Vision;
import util.logging.DataSource;

/**
 * 
 *
 * @author Mike
 */

public class Agent {
	
	// Setup variables for motivation: Competence, Certainty and PleasureDistressSystem
	private CompetenceTank comt;
	private CertaintyTank cert;
	private PleasureDistressSystem pds;
	
	// Setup variables for memory
	private SecondaryMemory sm;
	private MemoryController mc;
	
	// Setup variable for emotions
	private EmotionalStates e;
	
	// Setup variables for perception: HyPercept, Vision, and NeedPercept
	private HyPercept hp;
	private Vision v;
	private NeedPercept np;
		
	// Data source variable
	private DataSource ds;
		
	/**
	 * Sets up a PsiCasso agent.
	 * 
	 * @param dataSource Data Source object which is needed to save data of the agent
	 * for an output (e.g. GUI, data file).
	 */
	public Agent(DataSource ds){
		this.ds = ds;
		setUpAllComponents();
	}
	
	/**
	 * Sets up a PsiCasso agent.
	 */
	public Agent(){
		this.ds = new DataSource();
		setUpAllComponents();
	}
	
	public void lookAt(Trace trace){
		v.setActualTrace(trace);
	}
		
	public void feedMemory(Trace[] traces){
		for(int i = 0; i < traces.length; i++){
			mc.addTraceToSM(traces[i]);
		}
	}
	
	private void setUpAllComponents(){
		System.out.println("Setup Agent...");
		
		try {
			setUpMotivationalSystem();
			setUpEmotions();
			setUpMemory();
			setUpPerception();
			connectAllComponents();
		} catch (Exception e){
			e.printStackTrace();
		}
        cert.addObserver(ds);
        comt.addObserver(ds);
        e.addObserver(ds);
        pds.addObserver(ds);
        hp.addObserver(ds);
			
		System.out.println("Agent ready to perceive.");
	}
	
	public void refresh(Trace visualTrace){
		
		lookAt(visualTrace);
		np.createTrace();
		cert.leak();
		comt.leak();
		e.updateStates();
		
	}
	
	public void setUpMotivationalSystem(){		
		cert = new CertaintyTank(0.6, 0.8, 0.2d);
		comt = new CompetenceTank();
		pds = new PleasureDistressSystem(new Tank[] {cert});
		pds.addObserver(comt);
	}
		
	public void setUpEmotions(){		
		e = new EmotionalStates();	
	}
	
	public void setUpMemory(){
		sm = new SecondaryMemory();
		mc = new MemoryController(sm);	
	}
	
	public void setUpPerception(){		
		v = new Vision();
		np = new NeedPercept();
		hp = new HyPercept(v, np);		
	}
	
	public void connectAllComponents(){
		// Emotions observe Competence and Certainty tank
		comt.addObserver(e);	
		cert.addObserver(e);
		
		// HyPercept needs access to memory
		hp.setMemoryCtrl(mc);
			
		// HyPercept and Memory Controller observe emotions to get information about the resolution level
		e.addObserver(hp);
		e.addObserver(mc);
			
		// Certainty tank observes HyPercept to get certainty signals
		hp.addObserver(cert);
				
		// Working memory observes Vision and NeedPercept to get information about the perception
		v.addObserver(mc);
		np.addObserver(mc);
				
		// NeedPercept observes all tanks to get information about the needs
		cert.addObserver(np);
		comt.addObserver(np);	
	}
	public MemoryController getMemoryController() {
		return mc;
	}

	public CompetenceTank getCompetenceTank() {
		return comt;
	}

	public CertaintyTank getCertaintyTank() {
		return cert;
	}

	public Vision getVision() {
		return v;
	}

	public NeedPercept getNeedPercept() {
		return np;
	}

	public DataSource getDataSource() {
		return ds;
	}

	public void setDataSource(DataSource ds) {
		this.ds = ds;
	}
}
