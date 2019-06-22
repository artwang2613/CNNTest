package io.artie.ai.cnn;

import java.util.ArrayList;
import java.util.List;

public class Layer {

	private List<Node> nodes = new ArrayList<Node>();
	private Layer parent;
	private Layer child;

	private int index;
	private int length;

	public Layer(int index, int size) {
		this.setLength(size);
		this.index = index;

		for (int i = 0; i < size; i++) {
			nodes.add(new Node(0.0, this.index, i));
		}
	}

	public void addRelations(Layer parent) {
		this.parent = parent;
		parent.child = this;

		// construct the connections between the layers
		for (Node n : nodes) {
			n.createConnections(parent.getNodes());
		}
	}

	public void addChildLayer(Layer child) {
		this.child = child;
	}

	public void setInput(List<Double> inputs) {
		for (int i = 0; i < inputs.size(); i++) {
			nodes.get(i).setValue(inputs.get(i));
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

	public void forwardPropagate() {
		// we only do this for hidden layers ie checking if they have parents
		if (parent != null) {
			for (Node n : nodes) {
				n.propagate();
			}
		}
		if (child != null) {
			child.forwardPropagate();
		}
	}

	public void backPropagate(List<Double> targets) {
		if (targets != null) {
			for (Node n : nodes) {
				double nodeID = n.getNodeID();
				for (Connection c : n.getLinkedConnections()) {
					c.setDeltaVal(c.getInputNode().getValue() * n.nodeSigmoidDeriv()
							* n.outputErrorDeriv(targets.get(n.getNodeID())));
					c.correctWeights();
					c.setDeltaVal(0.0);
				}
			}
		} else {
			List<Node> childNodes = child.getNodes();
			List<Connection> childConnections = new ArrayList<Connection>();
			for (Node node : nodes) {
				for (Connection currentConnection : node.getLinkedConnections()) {

					for (Node childNode : childNodes) {
						for (Connection c1 : childNode.getLinkedConnections()) {
							if (c1.getInputNode() == node) {
								childConnections.add(c1);
								break;
							}
						}
					}

					for (Connection childConnection : childConnections) {
						double deltaVal = currentConnection.getDeltaVal() + node.nodeSigmoidDeriv()
								* currentConnection.getInputNode().getValue() * childConnection.getDeltaVal();
						currentConnection.setDeltaVal(deltaVal);
					}

					currentConnection.correctWeights();
					currentConnection.setDeltaVal(0.0);
				}
				childConnections.clear();

			}
		}
		if (parent.parent != null) {
			parent.backPropagate(null);
		}
	}

}
