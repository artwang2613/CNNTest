package io.artie.ai.cnn;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private Layer containingLayer;
	private double value;
	private int nodeID;
	private List<Connection> upConnections = new ArrayList<Connection>();
	private List<Connection> downConnections = new ArrayList<Connection>();

	public Node(double val, int iD, Layer containingLayer) {
		this.setValue(val);
		this.setNodeID(iD);
		this.setContainingLayer(containingLayer);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getNodeID() {
		return nodeID;
	}

	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	public List<Connection> getUpConnections() {
		return upConnections;
	}

	public List<Connection> getDownConnections() {
		return downConnections;
	}
	
	public void createConnections(List<Node> nodesInPreviousLayer) {
		for (Node nodeInPreviousLayer : nodesInPreviousLayer) {
			// up connection
			Connection con = new Connection(1.0, 0.0, nodeInPreviousLayer, this);
			upConnections.add(con);
			
			// down connection
			nodeInPreviousLayer.addDownConnection(con);
		}
	}
	
	public void addDownConnection(Connection con) {
		this.downConnections.add(con);
	}

	public void propagate() {
		System.out.println("Forward propagation: on " + this.toString());
		double sum = 0.0;
		for(Connection c : upConnections) {
			sum += c.getWeightVal() * c.getInputNode().getValue();
		}
		double oldValue = value;
		value = 1.0 / (1.0 + Math.pow(Math.E, -1.0 * sum / 100.0));
		System.out.println("Forward propagation: on " + this.toString() + ": changing value from " + oldValue + " to " + value);
	}
	
	public double nodeSigmoidDeriv() {
		return value * (1.0 - value);
	}
	
	public double outputErrorDeriv(double target) {
		return -(target - value);
	}

	public Layer getContainingLayer() {
		return containingLayer;
	}

	public void setContainingLayer(Layer containingLayer) {
		this.containingLayer = containingLayer;
	}
	
	public String toString() {
		return "Node: " + this.nodeID + " with value -> " + this.value + " in layer " + containingLayer.getLayerID();
	}
}
