package io.artie.ai.cnn;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;



public class Convolution extends Layer{
	private int id;
	
	private double[] filter = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private List<Double> patternedOutputs = new ArrayList<Double>();
	private List<Connection> relatedConnections = new ArrayList<Connection>();
	


	Convolution(int id, double[] Filter, ArrayList<Connection> connections, ArrayList<Double> patout) {
		super(id, 100);
		this.filter = Filter;
		this.setRelatedConnections(connections);
		this.patternedOutputs = patout;
	}

	public void setPO(double sum) {
		patternedOutputs.add(sum);
	}
	
	public List<Double> getAllPO() {
		return this.patternedOutputs;
	}
	
	public double getPO(int index) {
		return this.patternedOutputs.get(index);
	}
	
	public double getFilter(int index) {
		return this.filter[index];
	}

	public List<Connection> getRelatedConnections() {
		return relatedConnections;
	}

	public void setRelatedConnections(List<Connection> relatedConnections) {
		this.relatedConnections = relatedConnections;
	}
}
