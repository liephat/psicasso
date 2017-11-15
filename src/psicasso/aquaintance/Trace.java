package psicasso.aquaintance;

import java.util.Random;

/**
 * 
 *
 * @author Mike Imhof
 */
public class Trace implements Comparable<Trace> {
	
	private int[] content;
	private Trace connectedTrace;
	
	private String id;
	
	// Temporary value of last activation
	private double activation;
	
	public Trace(int[] content, Trace connectedTrace){
		init();
		this.content = content;
		this.connectedTrace = connectedTrace;
	}
	
	public Trace(int[] content) {
		init();
		this.content = content;
	}
		
	public void init(){
		id = "T0";
	}
	
	public void allocateLearningParameter(double lp){
		/*
		 *  according to Hintzman (1984) each value in the trace is replaced by 0 
		 *  with probability of lp 
		 */
		Random rnd = new Random();
		for(int i = 0; i < content.length; i++) {
			double rndDouble = rnd.nextDouble();
			if(rndDouble <= lp){
				content[i] = 0;
			}
		}
	}
	
	public static Trace merge(Trace firstTrace, Trace secondTrace){
		int[] intT1 = firstTrace.getContent();
		int[] intT2 = secondTrace.getContent();
		int[] intTrace = new int[firstTrace.length() + secondTrace.length()];
		for(int i = 0; i < firstTrace.length(); i++){
			intTrace[i] = intT1[i];
		}
		for(int i = 0; i < secondTrace.length(); i++){
			intTrace[i+intT1.length] = intT2[i];
		}
		return new Trace(intTrace);
	}
		
	public int[] getContent(){
		return content;
	}
	
	public int length(){
		return content.length;
	}
	
	public void setConnectedTrace(Trace trace){
		connectedTrace = trace;
	}
	
	public Trace getConnectedTrace(){
		return connectedTrace;
	}
	
	public double getActivation() {
		return activation;
	}

	public void setActivation(double activation) {
		this.activation = activation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String toString(){
		String strTrace;
		strTrace = new String("[ ");
		for(int i = 0; i < content.length; i++){
			if (content[i] < 0) {
				strTrace += content[i] + " ";
			} else {
				strTrace += " " + content[i] + " ";
			}
		}
		strTrace += "]";
		
		return strTrace;
	}

	@Override
	public int compareTo(Trace t) {		
		return ((Double)activation).compareTo(t.getActivation());
	}
	
}
