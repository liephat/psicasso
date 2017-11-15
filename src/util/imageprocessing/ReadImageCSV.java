package util.imageprocessing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;

import com.csvreader.CsvReader;

import util.neuralnet.DataContent;

public class ReadImageCSV {
	private final static String HOMEDIRECTORY = new File("").getAbsolutePath();

	private CsvReader freader; // CSV-Reader

	private String datafilename;
	private String filepath;

	public ReadImageCSV(String datafilename, String filepath) {
		this.datafilename = datafilename;
		this.filepath = filepath;
		try {
			freader = new CsvReader(HOMEDIRECTORY + filepath + datafilename, '\t');
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public String[] getImageFilenames(int count) {
		String[] ifn = new String[count];
		try {
			freader.readHeaders();
			for (int i = 0; i < count; i++) {
				if (freader.readRecord()) {
					ifn[i] = freader.get(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ifn;
	}
	
	public int getDatasetCount(){
		int dsCount = 0;
		try {
			freader.readHeaders();
			while(freader.readRecord()){
				dsCount++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dsCount;		
	}

	public BasicMLDataSet getData(DataContent[] dc, int count) {
		BasicMLDataSet ds = new BasicMLDataSet();
		double d[] = new double[dc.length];
		BasicMLData bmld;
		try {
			freader.readHeaders();
			for (int i = 0; i < count; i++) {
				if (freader.readRecord()) {
					
					//System.out.println("CurrentRecord:" + freader.getCurrentRecord());
					for (int k = 0; k < dc.length; k++) {
						double tmp = 0.0;
						
						switch (dc[k]) {
						case TCONTRASTR:
							tmp = Double.valueOf(freader.get(771)).doubleValue();
							break;
						case TCONTRASTG:
							tmp = Double.valueOf(freader.get(772)).doubleValue();
							break;
						case TCONTRASTB:
							tmp = Double.valueOf(freader.get(773)).doubleValue();
							break;
						case AVGCONTRAST:
							tmp = Double.valueOf(freader.get(774)).doubleValue();
							break;
						case LUMCONTRAST:
							tmp = Double.valueOf(freader.get(775)).doubleValue();
							break;
						case DIRECTIONALITY:
							tmp = Double.valueOf(freader.get(776)).doubleValue();
							break;
						case COARSENESS:
							tmp = Double.valueOf(freader.get(777)).doubleValue();
							break;
						case RNESS:
							double dSumR = 0;
							for (int p = 0; p < 256; p++) {
								dSumR = dSumR + (p + 1) * Double.valueOf(freader.get(3 + p)).doubleValue();
							}

							tmp = dSumR / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue() * 256);
							// System.out.println("RNESS:" + tmp);
							break;
						case RLOW:
							double dSumRLow = 0;
							for (int p = 0; p < 85; p++) {
								dSumRLow = dSumRLow + Double.valueOf(freader.get(3 + p)).doubleValue();
							}

							tmp = dSumRLow / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue());
							// System.out.println("RLOW:" + tmp);
							break;
						case RMID:
							double dSumRMid = 0;
							for (int p = 0; p < 86; p++) {
								dSumRMid = dSumRMid + Double.valueOf(freader.get(88 + p)).doubleValue();
							}

							tmp = dSumRMid / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue());
							// System.out.println("RMID:" + tmp);
							break;
						case RHIGH:
							double dSumRHigh = 0;
							for (int p = 0; p < 85; p++) {
								dSumRHigh = dSumRHigh + Double.valueOf(freader.get(174 + p)).doubleValue();
							}

							tmp = dSumRHigh / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue());
							// System.out.println("RHIGH:" + tmp);
							break;
						case GNESS:
							double dSumG = 0;
							for (int p = 0; p < 256; p++) {
								dSumG = dSumG + (p + 1) * Double.valueOf(freader.get(259 + p)).doubleValue();
							}
							tmp = dSumG / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue() * 256);
							break;
						case GLOW:
							double dSumGLow = 0;
							for (int p = 0; p < 85; p++) {
								dSumGLow = dSumGLow + Double.valueOf(freader.get(259 + p)).doubleValue();
							}

							tmp = dSumGLow / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue());
							break;
						case GMID:
							double dSumGMid = 0;
							for (int p = 0; p < 86; p++) {
								dSumGMid = dSumGMid + Double.valueOf(freader.get(344 + p)).doubleValue();
							}

							tmp = dSumGMid / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue());
							break;
						case GHIGH:
							double dSumGHigh = 0;
							for (int p = 0; p < 85; p++) {
								dSumGHigh = dSumGHigh + Double.valueOf(freader.get(430 + p)).doubleValue();
							}

							tmp = dSumGHigh / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue());
							break;
						case BNESS:
							double dSumB = 0;
							for (int p = 0; p < 256; p++) {
								dSumB = dSumB + (p + 1) * Double.valueOf(freader.get(515 + p)).doubleValue();
							}
							tmp = dSumB / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue() * 256);
							break;
						case BLOW:
							double dSumBLow = 0;
							for (int p = 0; p < 85; p++) {
								dSumBLow = dSumBLow + Double.valueOf(freader.get(515 + p)).doubleValue();
							}

							tmp = dSumBLow / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue());
							break;
						case BMID:
							double dSumBMid = 0;
							for (int p = 0; p < 86; p++) {
								dSumBMid = dSumBMid + Double.valueOf(freader.get(600 + p)).doubleValue();
							}

							tmp = dSumBMid / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue());
							break;
						case BHIGH:
							double dSumBHigh = 0;
							for (int p = 0; p < 85; p++) {
								dSumBHigh = dSumBHigh + Double.valueOf(freader.get(686 + p)).doubleValue();
							}

							tmp = dSumBHigh / (Double.valueOf(freader.get(1)).doubleValue() * Double.valueOf(freader.get(2)).doubleValue());
							break;
						}
						d[k] = tmp;
					}
				}

				bmld = new BasicMLData(d);
				double[] testd = bmld.getData();
				/*System.out.print(dc[k] + ": ");
				for (int u = 0; u < testd.length; u++) {
					System.out.print(testd[u] + ", ");
				}
				System.out.println();*/
				ds.add(bmld);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ds;
	}
}
