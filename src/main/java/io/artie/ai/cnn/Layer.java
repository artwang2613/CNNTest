package io.artie.ai.cnn;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	private List<Node> nodes = new ArrayList<Node>();
	private Layer parent = null;
	private Layer child = null;

	private int index;

	public Layer(int index, int size) {
		this.index = index;

		for (int i = 0; i < size; i++) {
			nodes.add(new Node(0.0, i, this));
		}
	}

	public void addRelations(Layer parent) {
		this.parent = parent;
		parent.child = this;

		// construct the connections between the layers
		for (Node node : nodes) {
			node.createConnections(parent.getNodes());
		}
	}

	public void setInput(List<Double> inputs) {
		for (int i = 0; i < inputs.size(); i++) {
			nodes.get(i).setValue(inputs.get(i));
		}
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public int getLayerID() {
		return index;
	}

	public void setLayerID(int layerID) {
		this.index = layerID;
	}

	public void forwardPropagate() {
		// we only do this for hidden layers ie checking if they have parents
		if (parent != null) {
			System.out.println("Forward propagation: starting on layer ->" + this.index);
			for (Node node : nodes) {
				node.propagate();
			}
		} else {
			System.out.println("Forward propagation: skiping this layer ->" + this.index + ": this is the INPUT layer");
		}
		if (child != null) {
			child.forwardPropagate();
		}
	}

	public void backPropagate(List<Double> targets) {
		System.out.println("Back propagation: starting on layer ->" + this.index);
		if (targets != null) {
			System.out.println("Back propagation: on layer ->" + this.index + ": this is the OUTPUT layer.");
			for (Node n : nodes) {
				System.out.println("Back propagation: running on " + n.toString());
				// double nodeID = n.getNodeID();
				for (Connection c : n.getUpConnections()) {
					c.setDeltaVal(c.getInputNode().getValue() * n.nodeSigmoidDeriv()
							* n.outputErrorDeriv(targets.get(n.getNodeID())));
					c.correctWeights();
					c.setDeltaVal(0.0);
				}
			}
		} else {
			System.out.println("Back propagation: on layer ->" + this.index + ": this is a middle hidden layer.");
			for (Node node : nodes) {
				System.out.println("Back propagation: running on " + node.toString());
				for (Connection upConnection : node.getUpConnections()) {
					List<Connection> downConnections = node.getDownConnections();
					for (Connection downConnection : downConnections) {
						double deltaVal = upConnection.getDeltaVal() + node.nodeSigmoidDeriv()
								* upConnection.getInputNode().getValue() * downConnection.getDeltaVal();
						upConnection.setDeltaVal(deltaVal);
					}

					upConnection.correctWeights();
					upConnection.setDeltaVal(0.0);
				}
			}
		}
		if (parent.parent != null) {
			System.out.println("Back propagation: to the next layer.");
			parent.backPropagate(null);
		} else {
			System.out.println("Back propagation: on layer ->" + this.index
					+ ": stop propagation becasue we have reached the first hidden layer.");
		}
	}

	public double calculateAbsErr(List<Double>targets) {
		double sum = 0;

		for(Node outputNode : nodes) {
			sum += Math.pow(targets.get(outputNode.getNodeID()) - outputNode.getValue(), 2);
		}
		
		sum /= nodes.size();
		
		return sum;
	}
	
	

}
