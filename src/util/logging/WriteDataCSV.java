package util.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.csvreader.CsvWriter;

/**
 * 
 *
 * @author Mike
 */
public class WriteDataCSV {
	
	private CsvWriter fwriter;
	
	private String dataFileName;
	private String filePath;
	
	private final static String HOMEDIRECTORY = new File("").getAbsolutePath();
	
	public WriteDataCSV() {
		initWriter();
	}
	
	public WriteDataCSV(String dataFileName, String filePath) {
		this.dataFileName = dataFileName;
		this.filePath = filePath;
		
		checkDir(HOMEDIRECTORY + filePath);
		
		initWriter();
	}

	private void checkDir(String d) {
		File dir = new File(d);
		if (!dir.isDirectory()) {
			dir.mkdir();
		}
	}

	private void initWriter() {
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(new File(HOMEDIRECTORY + filePath + dataFileName)));
			fwriter = new CsvWriter(bw, ';');
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeHeader(String[] header) {
		if(fwriter != null) {
			try {
				fwriter.writeRecord(header);
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public void writeRecord(String[] record) {
		try {
			fwriter.writeRecord(record);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		fwriter.close();
	}
}
