package io.artie.ai.cnn;

import java.util.ArrayList;
import java.util.List;

public class Layer {

	private List<Node> nodes = new ArrayList<Node>();
	private Layer parent;
	
	private int index;
	private int length;

	public Layer(int index, int size) {
		this.setLength(size);
		this.index = index;

		for (int i = 0; i < size; i++) {
			nodes.add(new Node(0.0, this.index, i));
		}
	}

	public void addParentLayer(Layer parent) {
		this.parent = parent;
		
		// construct the connections between the layers
		for(Node n : nodes) {
			n.createConnections(parent.getNodes());
		}
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodeList) {
		this.nodes = nodeList;
	}

	public int getLayerID() {
		return index;
	}

	public void setLayerID(int layerID) {
		this.index = layerID;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
