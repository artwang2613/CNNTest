package io.artie.ai.cnn;

import java.util.Random;

public class Connection {
	private double weightVal;
	private double deltaVal;
	private double eta = 50.0;
	private Node inputNode;
	private Node receivingNode;

	private Random rand = new Random();

	public Connection(double weightV, double deltaV, Node inputNode, Node receivingNode) {
		this.setRandomWeightVal();
		this.setDeltaVal(deltaV);
		this.setInputNode(inputNode);
		this.setReceivingNode(receivingNode);
	}

	private void setReceivingNode(Node receivingNode) {
		this.receivingNode = receivingNode;
	}

	public Node getReceivingNode() {
		return this.receivingNode;
	}

	public void setRandomWeightVal() {
		this.weightVal = rand.nextDouble();
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

	public Node getInputNode() {
		return inputNode;
	}

	public void setInputNode(Node inputNode) {
		this.inputNode = inputNode;
	}

	public void correctWeights() {
		weightVal -= eta * deltaVal;
	}

	public String toString() {
		return "Connection: delta->" + this.deltaVal + ", weight->" + this.weightVal + " input node->"
				+ this.inputNode.toString() + " receiving node->" + this.receivingNode.toString();
	}
}
