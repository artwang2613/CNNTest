package io.artie.ai.cnn;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private double value;

	private Layer parentLayer;
	private int nodeID;

	private List<Connection> linkedConnections = new ArrayList<Connection>();

	public Node(double val, int layerNum, int iD) {
		this.setValue(val);
		this.setNodeID(iD);
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

	public List<Connection> getLinkedConnections() {
		return linkedConnections;
	}

	public void setLinkedConnections(List<Connection> linkedConnections) {
		this.linkedConnections = linkedConnections;
	}

	public Layer getParentLayer() {
		return parentLayer;
	}

	public void setParentLayer(Layer parentLayer) {
		this.parentLayer = parentLayer;
	}

	public void createConnections(List<Node> nodesInPreviousLayer) {

		for (Node n : nodesInPreviousLayer) {
			linkedConnections.add(new Connection(0.0, 0.0, n));
		}

	}
	
	public void propagate() {
		double sum = 0.0;
		for(Connection c : linkedConnections) {
			sum += c.getWeightVal() * c.getInputNode().getValue();
		}
		value = 1.0 / (1.0 + Math.pow(Math.E, -sum / 100.0));
	}
	
	
	
	public double nodeSigmoidDeriv() {

		return value * (1 - value);
	}
	
	public double outputErrorDeriv(double target) {
		return value - target; // -(target - val)
	}

}
