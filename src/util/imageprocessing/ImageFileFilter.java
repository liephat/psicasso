package util.imageprocessing;

import java.io.File;
import java.io.FileFilter;

public class ImageFileFilter implements FileFilter{
	
	public boolean accept(File pathname){
		return pathname.getName().endsWith(".jpg") || pathname.getName().endsWith(".jpeg") || pathname.getName().endsWith(".png") || 
			pathname.getName().endsWith(".bmp") || pathname.getName().endsWith(".JPG");		
	}
	
}
