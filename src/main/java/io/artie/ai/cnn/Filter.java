package io.artie.ai.cnn;

import java.util.*;

public class Filter extends Node {
	private List<List<Double>> kernal;
	private int size;

	public Filter(int iD, int size, Layer containingLayer, Double[] filterVals) {
		super(0.0, iD, NODE_TYPE.FILTER, containingLayer);
		this.setSize(size);
		int sizeCounter = 0;
		for (int s = 0; s < size; s++) {
			List<Double> tempRow = new ArrayList<Double>();
			for (int i = sizeCounter; i < sizeCounter + size; i++) {
				tempRow.add(filterVals[i]);
				sizeCounter++;
			}
			kernal.add(tempRow);
		}
	}

	public List<Double> getKernalVals() {
		
		List<Double> returnVals = new ArrayList<Double>();
		
		for (List<Double> row : kernal) {
			for (Double val : row) {
				returnVals.add(val);
			}
		}
		
		return returnVals;
	}

	public void createConvolutionalConnections(List<Image> imagesInPreviousLayer) {
		if (this.getType() == NODE_TYPE.FILTER) {
			for (Image image : imagesInPreviousLayer) {

			}
		}
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
