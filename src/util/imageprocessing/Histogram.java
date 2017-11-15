package util.imageprocessing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Histogram {
	
	private BufferedImage image;
	private BufferedImage imageHistogram;	
	
	private int[] imgData;
	
	private static final int RED = 0;
	private static final int GREEN = 1;
	private static final int BLUE = 2;

	private int[][] rgbData = new int[3][];

	private int[] red = new int[256];
	private int[] green = new int[256];
	private int[] blue = new int[256];
		
	private double processingTime;
	
	private int pixelSum;
	

	public Histogram(File fileImage){				
		loadImage(fileImage);		
		buildHistogram();		
		calculatePixelSum();			
	}
	
	public void loadImage(File fileImage){
		try {
			image = ImageIO.read(fileImage);			
		} catch (IOException e) {			
			e.printStackTrace();			
		}		
	}
	
	public void buildHistogram(){
		
		// Start process time measurement
		double start = System.currentTimeMillis();
		
		int height = image.getHeight();
		int width = image.getWidth();
     
     	Raster raster = image.getRaster();
     	for(int i=0; i < width ; i++) {
     		for(int j=0; j < height ; j++) {          
     			red[raster.getSample(i,j, 0) ] ++;
                green[ raster.getSample(i,j, 1) ] ++;
                blue[ raster.getSample(i,j, 2) ] ++;             
     		}
     	}
		
		rgbData[RED] = red;
		rgbData[GREEN] = green;
		rgbData[BLUE] = blue;
		
		// End process time measurement and convert into milliseconds
		processingTime = (System.currentTimeMillis() - start) / 1000;
	}
	
	public double[] calculateRelativeRgbValues(){
		double[] relativeRgbValues = new double[3*256];				
		for (int i = 0; i < rgbData.length; i++) {			
			for (int j = 0; j < rgbData[i].length; j++){				
				relativeRgbValues[(i*256) + j] = (double) rgbData[i][j] / (double) pixelSum;												
			}			
		}
		
		return relativeRgbValues;		
	}
	
	/** 
	 * This method calculates test-values for a neural net (ONLY FOR TEST PURPOSES!)
	 * 
	 * @return testValues
	 */
	
	public double[] calculateTestValues1() {		
		double[] testValues = new double[3];						
		for (int i = 0; i < rgbData.length; i++){			
			int tmpValue = 0;			
			for (int j = 0; j < rgbData[i].length; j++){
				tmpValue = tmpValue + (rgbData[i][j] * (j + 1));
			}
			testValues[i] = ((double) tmpValue / (double) pixelSum) / 256;
		}
		return testValues;
	}
	
	public double[][] calculateTestValues(int divisor){		
		double[][] testValues = new double[3][256/divisor];						
		for (int i = 0; i < rgbData.length; i++){			
			int counter = 0;
			int tmpValue = 0;			
			for (int j = 0; j < rgbData[i].length; j++){
				tmpValue = tmpValue + rgbData[i][j];
				counter++;
				if (counter == divisor){
					int stelle = ((j+1)/divisor) - 1;
					testValues[i][stelle] = (double) tmpValue / (double) pixelSum;
										
					tmpValue = 0;
					counter = 0;					
				}																
			}	
		}
		return testValues;
	}
	
	public void calculatePixelSum(){
		pixelSum = image.getWidth() * image.getHeight();
	}
	
	public void createHistogramGraphic(File filePath){
				
		imageHistogram = new BufferedImage((256*3), 1000, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = imageHistogram.createGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
		int pointer = 0;
		int rectWidth;
		
		for(int i = 0; i < rgbData.length; i++){
			
			for(int j = 0; j < rgbData[i].length; j++){
				
			int height = (int) ((double) rgbData[i][j] / 10);
			
			int offset = 0;	
				if (i == RED){
					graphics.setColor(new Color(j, 0, 0));	
					offset=0;
				} else if (i == GREEN){
					offset= 256;
					graphics.setColor(new Color(0, j, 0));				
				} else if (i == BLUE){
					offset = 512;
					graphics.setColor(new Color(0, 0, j));
				}
				graphics.drawRect(offset+j, 1000-height, 1, height);	
			}
		}
				
		try {
			ImageIO.write(imageHistogram, "png", filePath);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		System.out.println("Histogram ready.");
	}
	
	public void testOutputHistogram(){
				
		for(int i = 0; i < red.length; i++){
			System.out.print(i + " ");
			System.out.print("R: " + red[i] + ", ");
			System.out.print("G: " + green[i] + ", ");
			System.out.println("B: " + blue[i]);			
		}
			
	}
	
	public int[][] getRgbData() {
		return rgbData;		
	}
	
	public double getProcessingTime(){
		return processingTime;
	}
	
	public int getPixelSum(){ 
		return pixelSum;		
	}
	
}
