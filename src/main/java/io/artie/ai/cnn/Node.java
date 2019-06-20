package net;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private double value;
	
	private int layerIndex;
	private int nodeID;
	
	
	private List<Connection> linkedConnections = new ArrayList<Connection>();
	
	public Node (double val, int layerNum, int iD) {
		this.setValue(val);
		this.setLayerIndex(layerNum);
		this.setNodeID(iD);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getLayerIndex() {
		return layerIndex;
	}

	public void setLayerIndex(int layerIndex) {
		this.layerIndex = layerIndex;
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
	
}
