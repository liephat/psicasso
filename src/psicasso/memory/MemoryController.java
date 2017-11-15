package psicasso.memory;

import psicasso.aquaintance.EmoObservable;
import psicasso.aquaintance.Observer;
import psicasso.aquaintance.SenseObservable;
import psicasso.aquaintance.Observable;
import psicasso.aquaintance.Trace;
import psicasso.perception.NeedPercept;
import psicasso.perception.Vision;

/**
 * 
 *
 * @author Mike Imhof
 */
public class MemoryController implements Observer {
	
	private SecondaryMemory sm;
	
	private Trace tmpVisualTrace;
	private Trace tmpNeedTrace;
	
	private double learningParameter;
	private double rl;
	
	private static double LEARNINGPARAMETERFACTOR = 0.25;
	
	public MemoryController(SecondaryMemory sm){
		this.sm = sm;
		init();
	}
	
	private void init(){
		learningParameter = 0.2;
	}
	
	public void update(Observable obs) {
		if(obs instanceof Vision){
			SenseObservable sobs = (SenseObservable) obs;
			tmpVisualTrace = sobs.getCurrentTrace();
			checkAndMerge();
		} else if (obs instanceof NeedPercept){
			SenseObservable sobs = (SenseObservable) obs;
			tmpNeedTrace = sobs.getCurrentTrace();
			checkAndMerge();
		} else if (obs instanceof EmoObservable){
			EmoObservable eobs = (EmoObservable) obs;
			rl = eobs.getResolutionLevel();
			calculateLearningParameter();
		}
	}
	
	public Trace getTraceWithHighestActivation(Trace probe){
		return sm.getTraceWithHighestActivation(probe);
	}
	
	public double getEchoIntensity(Trace probe){
		return sm.getEchoIntensity(probe);
	}
	
	public int getMemorySize(){
		return sm.getMemorySize();
	}
	
	public void printMemoryContent(){
		sm.printMemoryContent();
	}
	
	private void checkAndMerge(){			
		if(tmpVisualTrace != null && tmpNeedTrace != null){
			Trace trace = Trace.merge(tmpVisualTrace, tmpNeedTrace);
			trace.setId(tmpVisualTrace.getId() + "_" + tmpNeedTrace.getId());
			addTraceToSM(trace);
			tmpNeedTrace = null;
			tmpVisualTrace = null;
		}
	}
	
	public void addTraceToSM(Trace trace){
		// allocating with learning parameter
		trace.allocateLearningParameter(learningParameter);
		sm.addTrace(trace);
	}
	
	private void calculateLearningParameter(){
		learningParameter = (1 - rl) * LEARNINGPARAMETERFACTOR;
	}

	/**
	 * @param learningparameterfactor
	 */
	public static void setLearningParameterFactor(double lpf) {
		LEARNINGPARAMETERFACTOR = lpf;
	}
		
}
