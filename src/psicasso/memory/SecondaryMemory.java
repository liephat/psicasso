package psicasso.memory;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;

import psicasso.aquaintance.Trace;

/**
 * 
 * 
 * @author Mike Imhof
 */
public class SecondaryMemory {

	private LinkedList<Trace> protocol;

	public SecondaryMemory() {
		protocol = new LinkedList<Trace>();
	}

	public void addTrace(Trace trace) {
		protocol.add(trace);
	}
	
	public int getMemorySize(){
		return protocol.size();
	}

	private double getSimilarity(Trace probe, Trace trace) {
		double similarity = 0;
		if (probe.length() != trace.length()) {
			// TODO Exception werfen
			System.out.println("Fehler: Die Probe-Länge passt nicht zur Trace-Länge");
		} else {
			int tmp = 0;
			int nRelevant = 0;
			for (int i = 0; i < probe.length(); i++) {
				if (probe.getContent()[i] != 0 || trace.getContent()[i] != 0) {
					nRelevant++;
				}
				tmp = tmp + probe.getContent()[i] * trace.getContent()[i];
			}
			if (nRelevant == 0) {
				similarity = 0;
			} else {
				similarity = (double) tmp / (double) nRelevant;
			}
			
		}
		return similarity;
	}

	private double getActivation(Trace probe, Trace trace) {
		double activation = 0;
		activation = Math.pow(getSimilarity(probe, trace), 3);
		trace.setActivation(activation);
		return activation;
	}

	public double getEchoIntensity(Trace probe) {
		double echoIntensity = 0;
		if (protocol.size() != 0) {
			for (int i = 0; i < protocol.size(); i++) {
				echoIntensity = echoIntensity + getActivation(probe, protocol.get(i));
			}
		}
		return echoIntensity;
	}

	private double[] getEchoContent(Trace probe) {
		double[] echoContent = new double[probe.length()];
		if (protocol.size() != 0) {
			for (int i = 0; i < protocol.size(); i++) {
				Trace tmpTrace = protocol.get(i);
				double activation = getActivation(probe, tmpTrace);
				for (int j = 0; j < probe.length(); j++) {
					echoContent[j] = echoContent[j] + (activation * tmpTrace.getContent()[j]);
				}
			}
		}
		return echoContent;
	}

	public Trace getTraceWithHighestActivation(Trace probe) {
		if (protocol.size() != 0){
			Trace traceWithHighestAct = protocol.get(0);
			double tmpAct1 = getActivation(probe, traceWithHighestAct);
			double tmpAct2;
			for (int i = 1; i < protocol.size(); i++) {
				tmpAct2 = getActivation(probe, protocol.get(i));
				if (tmpAct1 < tmpAct2) {
					traceWithHighestAct = protocol.get(i);
					tmpAct1 = getActivation(probe, traceWithHighestAct);
				}
			}
			return traceWithHighestAct;
		} else {
			return null;
		}
	}

	public Trace getTrace(String id) {
		Trace t = null;
		for (int i = 0; i < protocol.size(); i++) {
			if (protocol.get(i).getId().equals(id)) {
				t = protocol.get(i);
			}
		}
		if (t == null) {
			System.out.println("Fehler: Trace-ID existiert nicht");
		}
		return t;
	}

	public ArrayList<String> getMostActivatedTraces(Trace probe, int amount) {
		ArrayList<String> traces = new ArrayList<String>();
		LinkedList<Trace> sortedTraceList;
		// Calculate activation for each trace
		for (int i = 0; i < protocol.size(); i++)
			getActivation(probe, protocol.get(i));

		sortedTraceList = protocol;
		// Sort list by activation
		Collections.sort(sortedTraceList);
		Collections.reverse(sortedTraceList);

		for (int i = 0; i < amount; i++) {
			traces.add((protocol.get(i).getActivation() + 1d) + "_" + protocol.get(i).getId());
		}
		return traces;
	}

	public ArrayList<String> getMostActivatedTraces(Trace probe, double threshold) {
		ArrayList<String> traces = new ArrayList<String>();
		// Calculate activation for each trace
		for (int i = 0; i < protocol.size(); i++)
			getActivation(probe, protocol.get(i));
		for (int i = 0; i < protocol.size(); i++) {
			if (protocol.get(i).getActivation() >= threshold) {

				traces.add(protocol.get(i).getActivation() + "_" + protocol.get(i).getId());
			}
		}
		return traces;
	}

	public ArrayList<String> getLeastActivatedTraces(Trace probe, int amount) {
		ArrayList<String> traces = new ArrayList<String>();
		LinkedList<Trace> sortedTraceList;
		// Calculate activation for each trace
		for (int i = 0; i < protocol.size(); i++)
			getActivation(probe, protocol.get(i));

		sortedTraceList = protocol;
		// Sort list by activation
		Collections.sort(sortedTraceList);

		for (int i = 0; i < amount; i++) {
			traces.add((protocol.get(i).getActivation() + 1d) + "_" + protocol.get(i).getId());
		}
		return traces;
	}

	public ArrayList<String> getLeastActivatedTraces(Trace probe, double threshold) {
		ArrayList<String> traces = new ArrayList<String>();
		// Calculate activation for each trace
		for (int i = 0; i < protocol.size(); i++)
			getActivation(probe, protocol.get(i));
		for (int i = 0; i < protocol.size(); i++) {
			if (protocol.get(i).getActivation() <= threshold) {

				traces.add(protocol.get(i).getActivation() + "_" + protocol.get(i).getId());
			}
		}
		return traces;
	}

	public void printMemoryContent(Trace probe) {
		// Calculate activation for each trace
		for (int i = 0; i < protocol.size(); i++)
			getActivation(probe, protocol.get(i));

		LinkedList<Trace> sortedTraceList = protocol;
		// Sort list by activation
		Collections.sort(sortedTraceList);
		Collections.reverse(sortedTraceList);

		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
		numberFormat.setMinimumFractionDigits(2);
		numberFormat.setMaximumFractionDigits(2);

		System.out.println("\nMemory Content (sorted by activation):");
		System.out.println("\n" + probe.getId() + ":\t" + probe.toString() + "\tProbe\n");
		for (int i = 0; i < sortedTraceList.size(); i++) {
			System.out.print(sortedTraceList.get(i).getId() + ":\t");
			System.out.print(sortedTraceList.get(i).toString());
			System.out.print("\tAct: " + numberFormat.format(sortedTraceList.get(i).getActivation()));
			System.out.println();
		}
		System.out.println("\nEchoIntensity:\t" + numberFormat.format(getEchoIntensity(probe)));
		System.out.print("EchoContent:");
		double[] echoContent = getEchoContent(probe);

		for (int i = 0; i < echoContent.length; i++) {
			System.out.print("\t" + numberFormat.format(echoContent[i]));
		}
	}

	public void printMemoryContent() {
		System.out.println("\nMemory Content:\n");

		for (int i = 0; i < protocol.size(); i++) {
			System.out.print(protocol.get(i).getId() + ":\t");
			System.out.println(protocol.get(i).toString());
		}
	}

}
