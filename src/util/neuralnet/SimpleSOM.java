package util.neuralnet;

/*
 * Encog(tm) Examples v3.0 - Java Version
 * http://www.heatonresearch.com/encog/
 * http://code.google.com/p/encog-java/

 * Copyright 2008-2011 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */

import util.imageprocessing.FileLoader;
import util.imageprocessing.Histogram;
import util.imageprocessing.ReadImageCSV;

import java.io.File;
import java.util.ArrayList;

import org.encog.Encog;
import org.encog.mathutil.matrices.Matrix;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.neural.som.SOM;
import org.encog.neural.som.training.basic.BasicTrainSOM;
import org.encog.neural.som.training.basic.neighborhood.NeighborhoodSingle;

/**
 * Implement a simple SOM using Encog. It learns to recognize two patterns.
 * 
 * @author jeff, chris
 * 
 */
public class SimpleSOM {

	private final static String HOMEDIRECTORY = new File("").getAbsolutePath();
	private static String datafilefolder;
	private static String datafilename;

	private static int imagecount = 1711;
	private static int outputneurons = 25;
	private final static int TRAININGITERATIONS = 20;

	private boolean optimalTraining = false;

	private double threshold;

	BasicMLDataSet training;
	BasicTrainSOM train;
	SOM network;

	// Statistical values
	ArrayList<ArrayList<Double>> values;
	ArrayList<double[]> parameter;
	
	// Traces
	private ArrayList<int[]> result;

	public SimpleSOM() {

	}

	public SimpleSOM(int imagecount, int outputneurons) {
		this.imagecount = imagecount;
		this.outputneurons = outputneurons;
	}

	private void printIntArray(int[] ia) {
		String s = "[";
		for (int i = 0; i < ia.length; i++) {
			s = s + ia[i] + " ";
		}
		s = s + "]";
		System.out.println(s);
	}

	public BasicMLDataSet buildTrainingSet(DataContent[] dc, int count) {
		ReadImageCSV rcsv = new ReadImageCSV(datafilename, datafilefolder);

		return rcsv.getData(dc, count);
	}

	public int[] singleTrace(String imagename) {
		FileLoader imageLoader = new FileLoader(HOMEDIRECTORY + datafilefolder + imagename);
		File image[] = imageLoader.getFiles();
		// To do
		return null;
	}

	public BasicMLDataSet buildTrainingSet(int count) {

		FileLoader imageLoader = new FileLoader(HOMEDIRECTORY + datafilefolder + datafilename);
		File[] images = imageLoader.getFiles();
		Histogram histogram;

		training = new BasicMLDataSet();

		System.out.println("Found " + images.length + " images");

		for (int i = 0; i < count; i++) {

			double[] tmp;

			histogram = new Histogram(images[i]);

			System.out.println("Datei: " + images[i].getName() + ", Verarbeitungszeit: " + histogram.getProcessingTime() + " Sekunden");

			// histogram.createHistogramGraphic(new File(HOMEDIRECTORY +
			// "\\histograms\\" + i + ".png"));

			tmp = histogram.calculateRelativeRgbValues();
			System.out.println("k:" + tmp[5]);
			training.add(new BasicMLData(tmp));

		}

		return training;

	}

	public void setFile(String datafilename) {
		this.datafilename = datafilename;

	}

	public void setPath(String filepath) {
		this.datafilefolder = filepath;

	}

	public void learn() {
		if (!optimalTraining) {
			int iteration = 0;
			for (iteration = 0; iteration <= TRAININGITERATIONS; iteration++) {
				train.iteration();
			}
		} else {
			boolean finished = false;
			double oldSum = 0d;
			int iterations = 0;
			while (!finished) {
				train.iteration();
				iterations++;
				BasicMLData bmld = (BasicMLData) network.compute(training.getData().get(0).getInput());

				double sum = 0;
				for (int i = 0; i < bmld.size(); i++) {
					sum += bmld.getData(i);
				}
				if ((oldSum - sum <= 0.00001) && (sum - oldSum <= 0.00001)) {
					finished = true;
					System.out.println("Iterations: " + iterations);
				} else {
					oldSum = sum;
				}
			}
		}
		System.out.println("Neural net has learned.");
	}

	public void load(BasicMLDataSet training) {
		// training = this.buildTrainingSet(new DataContent[] {
		// DataContent.LUMCONTRAST, DataContent.AVGCONTRAST,
		// DataContent.DIRECTIONALITY }, imagecount);

		// training = this.buildTrainingSet(new DataContent[] {
		// DataContent.DIRECTIONALITY, DataContent.RLOW, DataContent.RMID,
		// DataContent.RHIGH,
		// DataContent.GLOW, DataContent.GMID, DataContent.GHIGH,
		// DataContent.BLOW, DataContent.BMID, DataContent.BHIGH,
		// DataContent.COARSENESS,
		// DataContent.AVGCONTRAST }, imagecount);

		this.training = training;
		
		System.out.println("Record Count:" + training.getRecordCount());
		System.out.println("Input Size: " + training.getInputSize());

		network = new SOM(training.getInputSize(), outputneurons);

		Matrix mx = new Matrix(training.getInputSize(), outputneurons);
		mx.set(0.0);
		network.setWeights(mx);

		train = new BasicTrainSOM(network, 0.1, training, new NeighborhoodSingle());
		train.setForceWinner(false);

	}

	public void setOptimalTraining(boolean b) {
		this.optimalTraining = b;

	}

	public void prepareDataForFirstStatistics() {
		values = new ArrayList<ArrayList<Double>>();
				
		System.out.println("\n\n*************************************************************");
		
		// Display all records
		for (int input = 0; input < (int) training.getRecordCount(); input++) {
			BasicMLData bmld = (BasicMLData) network.compute(training.getData().get(input).getInput());
			System.out.print("Input " + input + ": ");
			for (int i = 0; i < bmld.size(); i++) {
				System.out.print(" " + bmld.getData(i));
			}
			System.out.println();
		}				
		
		// Prepare data
		for (int a = 0; a < outputneurons; a++) {
			ArrayList<Double> spalte = new ArrayList<Double>();
			for (int input = 0; input < imagecount; input++) {
				BasicMLData bmld = (BasicMLData) network.compute(training.getData().get(input).getInput());
				spalte.add(bmld.getData(a));

			}
			values.add(spalte);
		}
				
	}
	
	public void addDataForStatistics(BasicMLData newData){

		// Berechne neue Mittelwerte:
		for(int i = 0; i < values.size(); i++){
			BasicMLData bmld = (BasicMLData) network.compute(newData);
			values.get(i).add(bmld.getData(i));		
		}
	}
	
	public void calculateStatistics(){
		parameter = new ArrayList<double[]>();
		
		for (int h = 0; h < values.size(); h++) {
			ArrayList<Double> spalte = values.get(h);
			double mittelwert = 0;
			double quadratsumme = 0;
			for (int k = 0; k < spalte.size(); k++) {
				mittelwert += spalte.get(k);
				quadratsumme += Math.pow(spalte.get(k), 2);
			}
			mittelwert = mittelwert / spalte.size();

			double[] standardfehler = new double[spalte.size()];
			for (int k = 0; k < spalte.size(); k++) {
				standardfehler[k] = mittelwert - spalte.get(k);

			}
			double fehlersummeQ = 0;
			for (int k = 0; k < spalte.size(); k++) {
				fehlersummeQ += standardfehler[k] * standardfehler[k];
			}

			double sd = Math.sqrt((1d / (spalte.size() - 1)) * fehlersummeQ);

			System.out.println("Neuron " + h + " Mittelwert: " + mittelwert + " -- Standardabweichung: " + sd);
			double[] params = new double[2];
			params[0] = mittelwert;
			params[1] = sd;
			parameter.add(params);
		}
		System.out.println("*************************************************************");
		
		Encog.getInstance().shutdown();
	}

	public ArrayList<int[]> makeFirstTraces() {
		ArrayList<int[]> traces = new ArrayList<int[]>();
		
		for (int i = 0; i < values.get(0).size(); i++) {
			double[] d = new double[parameter.size()];
			for (int l = 0; l < parameter.size(); l++) {	
				d[l] = values.get(l).get(i);
			}

			int[] trace = new int[d.length];	
			for (int j = 0; j < d.length; j++) {
				trace[j] = 0;
				double m = parameter.get(j)[0];
				double sd = parameter.get(j)[1];
				double test = m - d[j];

				if (test > 0) {
					if ((sd / threshold) >= Math.abs(test)) {
						trace[j] = 1;
					}
				} else {
					if ((sd / threshold) >= Math.abs(test)) {
						trace[j] = -1;
					}
				}
			}
			traces.add(trace);
		}
		return traces;

	}
	
	public int[] makeNewTrace() {
		int[] trace = new int[values.size()];
			
		double[] d = new double[parameter.size()];
		for (int l = 0; l < parameter.size(); l++) {
			d[l] = values.get(l).get(values.get(l).size()-1);
		}
				
		for (int j = 0; j < d.length; j++) {
			trace[j] = 0;
			double m = parameter.get(j)[0];
			double sd = parameter.get(j)[1];
			double test = m - d[j];

			if (test > 0) {
				if ((sd / threshold) >= Math.abs(test)) {
					trace[j] = 1;
				}
			} else {
				if ((sd / threshold) >= Math.abs(test)) {
					trace[j] = -1;
				}
			}

		}
				
		return trace;
	}

	public ArrayList<int[]> getTraces() {
		return result;
	}

	public void setTraceThresholdFactor(double d) {
		threshold = d;

	}
}