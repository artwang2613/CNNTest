package net;




import java.util.ArrayList;
import java.util.List;

public class Layer {

	private List<Node> nodeList = new ArrayList<Node>();
	private List<Connection> connectionList = new ArrayList<Connection>();

	private int layerID;
	private int length;
	
	public Layer (int iD, int size) {
		this.setLength(size);
		this.layerID = iD;
		
		
		for(int i = 0; i < size; i++) {
			nodeList.add(new Node(0.0, this.layerID, i));
		}
		

	}
	
	

	public List<Node> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<Node> nodeList) {
		this.nodeList = nodeList;
	}

	public List<Connection> getConnectionList() {
		return connectionList;
	}

	public void setConnectionList(List<Connection> connectionList) {
		this.connectionList = connectionList;
	}

	public int getLayerID() {
		return layerID;
	}

	public void setLayerID(int layerID) {
		this.layerID = layerID;
	}



	public int getLength() {
		return length;
	}



	public void setLength(int length) {
		this.length = length;
	}
	
	
	
	
}
