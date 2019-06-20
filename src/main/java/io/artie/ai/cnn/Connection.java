package net;

import java.util.Random;

public class Connection {
	private double weightVal;
	private double deltaVal;
	
	private int layerIndex;
	private int connectionIndexInLayer;
	
	private Random rand = new Random();
	
	

	
	public Connection (double weightV, double deltaV, int layerNum, int connectionNum) {
		this.setRandomWeightVal();
		this.setDeltaVal(deltaV);
		this.setLayerIndex(layerNum);
		this.setConnectionIndex(connectionNum);
	}
	

	public void setRandomWeightVal() {
		this.weightVal = rand.nextDouble();
	}
	
	
	public int getConnectionIndex() {
		return connectionIndexInLayer;
	}

	public void setConnectionIndex(int connectionIndexInLayer) {
		this.connectionIndexInLayer = connectionIndexInLayer;
	}

	public int getLayerIndex() {
		return layerIndex;
	}

	public void setLayerIndex(int layerIndex) {
		this.layerIndex = layerIndex;
	}

	public double getDeltaVal() {
		return deltaVal;
	}

	public void setDeltaVal(double deltaVal) {
		this.deltaVal = deltaVal;
	}

	public double getWeightVal() {
		return weightVal;
	}

	public void setWeightVal(double weightVal) {
		this.weightVal = weightVal;
	}



	
}
