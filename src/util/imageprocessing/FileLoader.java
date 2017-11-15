package util.imageprocessing;

import java.io.File;

public class FileLoader {
	
	private File[] files;	
	private File directory;
		
	public FileLoader(String directory){		
		loadFilesFromDirectory(directory);		
	}
		
	public void loadFilesFromDirectory(String directory){				
		this.directory = new File(directory);		
		files = this.directory.listFiles(new ImageFileFilter());				
		System.out.println("Load files from directory " + directory);
		
	}
	
	public File[] getFiles() {		
		return files;		
	}
		
}
