package io.artie.ai.cnn;

import java.util.*;

public class Image {
	private int iD;
	private double targetValue;
	private List<List<Double>> image;
	private int rowLength;

	public Image(int iD, double targetVal, double[] trainingData, int conversionFactor) {
		this.iD = iD;
		this.targetValue = targetVal;
		this.rowLength = conversionFactor;

		int counter = 0;
		List<Double> row = new ArrayList<Double>();

		int imageMaxSize = (int) Math.pow(conversionFactor, 2);

		// TODO handle underflow of data to make it a full square
		for (int index = 0; index < trainingData.length; index++) {
			if (index > imageMaxSize) {
				if (counter++ < conversionFactor) {
					row.add(trainingData[index]);
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

	public List<List<Double>> getSubImage(int rowIndex, int colIndex, int size) { // row and col will correspond to the
																					// top left square of returned
																					// subimage
		List<List<Double>> returnImage = new ArrayList<List<Double>>();

		List<Double> row = new ArrayList<Double>();
		for (int rowNum = 0; rowNum < size; rowNum++) {
			for (int colNum = 0; colNum < size; colNum++) {
				row.add(image.get(rowIndex + rowNum).get(colIndex + colNum));
			}
			returnImage.add(row);
			row = new ArrayList<Double>();
		}

		return returnImage;

	}
}
