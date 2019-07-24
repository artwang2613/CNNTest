package io.artie.ai.cnn;

import java.util.*;

public class Image {
	private int iD;
	private double targetValue;
	private List<List<Double>> image;
	private int rowLength;
	private int numPixels;

	public Image(int iD, double targetVal, double[] pixels, int conversionFactor) {
		this.setiD(iD);
		this.setTargetValue(targetVal);
		this.setRowLength(conversionFactor);

		int counter = 0;
		List<Double> row = new ArrayList<Double>();

		int imageMaxSize = (int) Math.pow(conversionFactor, 2);
		setNumPixels(imageMaxSize);

		// TODO handle underflow of data to make it a full square
		for (int index = 0; index < pixels.length; index++) {
			if (index > imageMaxSize) {
				if (counter++ < conversionFactor) {
					row.add(pixels[index]);
				} else {
					image.add(row);
					row = new ArrayList<Double>();
					counter = 0;
				}
			} else {
				break;
			}
		}
	}
	
	public Image(int iD, double targetVal, List<List<Double>> pixels, int conversionFactor) {
		this.setiD(iD);
		this.setTargetValue(targetVal);
		this.setRowLength(conversionFactor);

		this.image = pixels;

		// TODO handle underflow of data to make it a full square
	}

	public List<Double> getSubImage(int rowIndex, int colIndex, int size) { // row and col will correspond to the
																			// top left square of returned
																			// subimage
		List<Double> returnImage = new ArrayList<Double>();

		for (int rowNum = 0; rowNum < size; rowNum++) {
			for (int colNum = 0; colNum < size; colNum++) {
				returnImage.add(image.get(rowIndex + rowNum).get(colIndex + colNum));
			}
		}

		return returnImage;
	}

	public int getiD() {
		return iD;
	}

	public void setiD(int iD) {
		this.iD = iD;
	}

	public double getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(double targetValue) {
		this.targetValue = targetValue;
	}

	public int getRowLength() {
		return rowLength;
	}

	public void setRowLength(int rowLength) {
		this.rowLength = rowLength;
	}

	public int getNumPixels() {
		return numPixels;
	}

	public void setNumPixels(int numPixels) {
		this.numPixels = numPixels;
	}
}
