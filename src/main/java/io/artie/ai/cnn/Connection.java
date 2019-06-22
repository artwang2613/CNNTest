package io.artie.ai.cnn;

import java.util.Random;

public class Connection {
	private double weightVal;
	private double deltaVal;
	
	private Node inputNode;
	
	private Random rand = new Random();
	
	

	
	public Connection (double weightV, double deltaV, Node inputNode) {
		this.setRandomWeightVal();
		this.setDeltaVal(deltaV);
		this.setInputNode(inputNode);
		/*this.setLayerIndex(layerNum);
		this.setConnectionIndex(connectionNum);*/
	}
	

	public void setRandomWeightVal() {
		this.weightVal = rand.nextDouble();
	}
	
	
	/*public int getConnectionIndex() {
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
	}*/

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


	public Node getInputNode() {
		return inputNode;
	}


	public void setInputNode(Node inputNode) {
		this.inputNode = inputNode;
	}

	public void correctWeights() {
		weightVal += -10 * deltaVal;
	}


	
}
