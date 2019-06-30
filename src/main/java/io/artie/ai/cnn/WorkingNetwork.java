package io.artie.ai.cnn;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

public class WorkingNetwork {

	private static final int TRAINING_ROUNDS = 5000;
	private List<Double> targets = new ArrayList<>();
	private List<List<Double>> trainingData = new ArrayList<List<Double>>();
	private List<Double> pairedTargets = new ArrayList<>();
	private Scanner scnr = new Scanner(System.in);
	private int[] hiddenLayerSize = { 75, 25 };
	private int outputLayerSize = 10;

	public static void main(String[] args) throws IOException {
		WorkingNetwork nn = new WorkingNetwork();
		nn.train();

		System.out.println("-----Ready to Predict-----");
		while (true) {
			if (nn.scnr.next().charAt(0) == 'q') {
				break;
			}
			nn.predict();
		}

		nn.scnr.close();
	}

	// this using arrayList for different sized hidden layers, flexible
	/*
	 * public WorkingNetwork(int layers) { this.numLayers = layers; for (int i = 0;
	 * i < numLayers; i++) { vals.add(new ArrayList<Double>()); } for (int i = 0; i
	 * < numLayers - 1; i++) { weights.add(new ArrayList<Double>()); deltas.add(new
	 * ArrayList<Double>()); }
	 * 
	 * }
	 * 
	 * /* public void train() throws IOException { this.parseCsv();
	 * setHiddenLayerSizes();
	 * 
	 * for (int i = 0; i < TRAINING_ROUNDS; i++) { setInputLayer(i); setTargets(i);
	 * forwardPropagate(); backPropagate(); //
	 * System.out.println(nn.vals.get(nn.numLayers - 1).get(0) + "new predicted //
	 * output"); // System.out.println(nn.vals.get(nn.numLayers - 1).get(1) + "new
	 * predicted // output"); } }
	 */

	public void train1() throws IOException {
		for (int i = 0; i < TRAINING_ROUNDS; i++) {
			System.out.println("Training Round: " + i + " started.");
			System.out.println("Training Round: on data ->" + trainingData.get(0));

			setInputLayer(trainingData.get(0));
			System.out.println("Training Round: on target data ->" + pairedTargets.get(0));
			setTargets(pairedTargets.get(0));
			forwardPass();
			backwardPass();
//			resetDeltaInTheSystem();
			System.out.println("Training Round: " + i + " completed.");
		}
	}

	public void predict1() {
		// Random rand = new Random();
		// int curIndex = rand.nextInt(300);
		System.out.println("Prediction: on data ->" + trainingData.get(11));
		setInputLayer(trainingData.get(11));
		System.out.println("Prediction: on target ->" + pairedTargets.get(11));
		setTargets(pairedTargets.get(11));
		forwardPass();
		predictOutcome(pairedTargets.get(11));
	}

	public void train() throws IOException {
		for (int i = 0; i < TRAINING_ROUNDS; i++) {
			System.out.println("Training Round: " + i + " started.");
			System.out.println("Training Round: on data ->" + trainingData.get(i));

			setInputLayer(trainingData.get(i));
			System.out.println("Training Round: on target data ->" + pairedTargets.get(i));
			setTargets(pairedTargets.get(i));
			forwardPass();
			backwardPass();
//			resetDeltaInTheSystem();
			System.out.println("Training Round: " + i + " completed.");
		}
	}

	public void predict() {
		Random rand = new Random();
		int curIndex = rand.nextInt(TRAINING_ROUNDS);
		System.out.println("Prediction: on data ->" + trainingData.get(curIndex));
		setInputLayer(trainingData.get(curIndex));
		System.out.println("Prediction: on target ->" + pairedTargets.get(curIndex));
		setTargets(pairedTargets.get(curIndex));
		forwardPass();
		predictOutcome(pairedTargets.get(curIndex));
	}

	private void parseCsv() throws IOException {
//		csvFile = "C:/Users/wFanga/eclipse-workspace1/SimpleNN/MNIST/mnist_train.csv";
		String trainingDataFilePath = "static/mnist_train.csv";
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(trainingDataFilePath);
//		InputStream is = new java.util.zip.ZipInputStream(zFile);
		Reader reader = new java.io.InputStreamReader(is);
		CsvListReader cs = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE);

		int count;
		try {
			count = 0;
//			boolean isLabel;
			while (count < TRAINING_ROUNDS) {
//				isLabel = true;
				List<String> row = cs.read();
				List<Double> lineData = new ArrayList<Double>();
				pairedTargets.add(Double.valueOf(row.get(0)));
				
				row.remove(0);
				for (String s : row) {
						lineData.add(Double.valueOf(s));
//						if (Double.valueOf(s) != 0) {
//							lineData.add(1.0); //
//							// System.out.print(1.0);
//						} else {
//							lineData.add(0.0); //
//							// System.out.print(0.0);
//						}
				}
				trainingData.add(lineData);
				// System.out.println("");
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cs.close();
		}
	}

	private void setInputLayer(List<Double> data) {
		inputLayer.setInput(data);
	}

	private void setTargets(Double targetNum) {
		targets.clear();

		for (int i = 0; i < 10; i++) {
			if (i == targetNum) {
				targets.add(1.0);
			} else {
				targets.add(0.0);
			}
		}
	}

//	private void resetDeltaInTheSystem() {
//		for (Layer layer : layers) {
//			for (Node node : layer.getNodes()) {
//				for (Connection connection : node.getUpConnections()) {
//					connection.setDeltaVal(0.0);
//				}
//			}
//		}
//	}
	
	
	private void correctAllWeights() {
		for (Layer layer : layers) {
			layer.correctWeightsInLayer();
		}
	}

	/*
	 * private double calculateAbsErr() { double sum = 0;
	 * 
	 * for (int i = 0; i < vals.get(numLayers - 1).size(); i++) { sum += 0.5 *
	 * Math.pow(targets.get(i) - vals.get(numLayers - 1).get(i), 2); } return sum; }
	 * 
	 * private double outputErrorDeriv(double val, double target) { return val -
	 * target; // -(target - val) }
	 * 
	 * private double sigmoidDeriv(double output) {
	 * 
	 * return output * (1 - output); }
	 */
	private void predictOutcome(Double correctTarget) {
		double maxOutputVal = 0;
		Node nodeWithTheMaxValue = null;// outputLayer.getNodes().get(0);
		for (Node node : outputLayer.getNodes()) {
			if (maxOutputVal < node.getValue()) {
				maxOutputVal = node.getValue();
				nodeWithTheMaxValue = node;
			}
		}

		System.out.println("The predicted answer is: " + nodeWithTheMaxValue.getNodeID());
		System.out.println("As compared to the theoretical value of: " + correctTarget);

	}

	// ____________________________________________________________
	// This is new code for a more OOP style
	private List<Layer> layers = new ArrayList<Layer>();
	private Layer inputLayer = null;
	private Layer outputLayer = null;

	public WorkingNetwork() throws IOException {
		Layer previousLayer = null;
		this.parseCsv();
		int index = 0;
		
		// input layer initialized separately
		previousLayer = inputLayer = new Layer(index++, trainingData.get(0).size());
		layers.add(inputLayer);
		
		// hidden layers
		for (int size : hiddenLayerSize) {
			Layer currentLayer = new Layer(index++, size);
			//	currentLayer.addBiasNode();

			currentLayer.addRelations(previousLayer);
			layers.add(currentLayer);
			previousLayer = currentLayer;
		}
		
		// output layer
		outputLayer = new Layer(index, this.outputLayerSize);
		outputLayer.addRelations(previousLayer);
		layers.add(outputLayer);
		
	}

	public void forwardPass() {
		inputLayer.forwardPropagate();
	}

	public void backwardPass() {
		outputLayer.backPropagate(targets);
		correctAllWeights();
	}
}