package io.artie.ai.cnn;

import java.util.ArrayList;
import java.util.List;

public class Layer {
	private List<Node> nodes = new ArrayList<Node>();
	private Layer parent = null;
	private Layer child = null;

	private double totalError = 0.0;

	private int index;

	public Layer(int index, int size) {
		this.index = index;

		for (int i = 0; i < size; i++) {
			nodes.add(new Node(0.0, i, this));
		}
	}

	public void addBiasNode() {
		// create the bias node at the end of every layer except the output layer,
		// taking no input with a value of 1
		nodes.add(new Node(1.0, nodes.size(), Node.NODE_TYPE.BIAS, this));

	}

	public void addRelations(Layer parent) {
		this.parent = parent;
		parent.child = this;

		// construct the connections between the layers
		for (Node node : nodes) {
			node.createConnections(parent.getNodes());
		}
	}

	public void forwardPropagate() {
		// we only do this for hidden layers ie checking if they have parents
		if (parent != null) {
			//System.out.println("Forward propagation: starting on layer ->" + this.index);
			for (Node node : nodes) {
				node.propagate();
			}
		} else {
			//System.out.println("Forward propagation: skiping this layer ->" + this.index + ": this is the INPUT layer");
		}
		if (child != null) {
			child.forwardPropagate();
		} else {

		}
	}

	public void correctWeightsInLayer() {
		for(Node node : nodes) {
			for(Connection upperConnection : node.getUpConnections()) {
				upperConnection.correctWeights();
				//System.out.println("Correcting " + upperConnection.getDeltaVal());
			}
		}
	}
	
	public void backPropagate(List<Double> targets) {
		//System.out.println("Back propagation: starting on layer ->" + this.index);
		if (targets != null) {
			calculateTotalError(targets);
			System.out.println("Total error of run: " + getTotalError());
			//System.out.println("Back propagation: on layer ->" + this.index + ": this is the OUTPUT layer.");
			for (Node node : nodes) {
				//System.out.println("Back propagation: running on " + node.toString());
				// double nodeID = n.getNodeID();
				for (Connection c : node.getUpConnections()) {
					// delta = -(target - out) * out(1 - out)
					double delta = node.outputErrorDeriv(targets.get(node.getNodeID())) * node.nodeSigmoidDeriv() * c.getInputNode().getValue();
					//System.out.println("" + node.outputErrorDeriv(targets.get(node.getNodeID())) + " * " + node.nodeSigmoidDeriv() + " * " + c.getInputNode().getValue()); 

					//		(node.getValue() - targets.get(node.getNodeID())) * node.getValue() * (1 - node.getValue()) ;
					c.setDeltaVal(delta);
					//System.out.println("Delta for: " + c.toString());
					// System.out.println("Previous Weight Value: " + c.getWeightVal() + " w/
					// expected change of: " + c.getDeltaVal());
					//c.correctWeights();

					// System.out.println("New Weight Value: " + c.getWeightVal());

				}
			}
		} else {
			//System.out.println("Back propagation: on layer ->" + this.index + ": this is a middle hidden layer.");
			for (Node node : nodes) {
				//System.out.println("Back propagation: running on " + node.toString());
				for (Connection upConnection : node.getUpConnections()) {
					List<Connection> downConnections = node.getDownConnections();

					// sum of (delta_down * weight_down) * value (1 - value})
					double deltaVal = 0.0;
					for (Connection downConnection : downConnections) {
						//System.out.println("BP : " + downConnection.toString());
						deltaVal += downConnection.getDeltaVal() * downConnection.getWeightVal(); // TODO: need to see if we need to use the old weight
				
//								" up connection->" + upConnection.toString() +	 
//								", down connection->" + downConnection.toString());
					}
					//System.out.println(deltaVal + " * " + node.nodeSigmoidDeriv() + " * " + upConnection.getInputNode().getValue()); 
					deltaVal = deltaVal * node.nodeSigmoidDeriv() * upConnection.getInputNode().getValue();

					upConnection.setDeltaVal(deltaVal);
				//	System.out.println("Delta for: " + upConnection.toString());

					// System.out.println("Previous Weight Value: " + upConnection.getWeightVal() +
					// " w/ expected change of: " + upConnection.getDeltaVal());
					//upConnection.correctWeights();
					// System.out.println("New Weight Value: " + upConnection.getWeightVal());
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

	public void calculateTotalError(List<Double> targets) {
		double sum = 0;

		for (Node outputNode : nodes) {
			sum += Math.pow((targets.get(outputNode.getNodeID()) - outputNode.getValue()), 2);
		}

		sum = sum / 2.0;

		setTotalError(sum);
	}

	public double getTotalError() {
		return totalError;
	}

	public void setTotalError(double totalError) {
		this.totalError = totalError;
	}

	public Layer getChild() {
		return child;
	}
	
	public Layer getParent() {
		return parent;
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
	
}
