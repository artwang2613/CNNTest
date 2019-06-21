package io.artie.ai.cnn;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

public class WorkingNetwork {

	private static final int TRAINING_ROUNDS = 1000;
	private List<List<Double>> weights = new ArrayList<List<Double>>();
	private List<List<Double>> vals = new ArrayList<List<Double>>();
	private List<List<Double>> deltas = new ArrayList<List<Double>>();

	private List<Double> targets = new ArrayList<>();
	private List<List<Double>> trainingData = new ArrayList<List<Double>>();
	private List<Double> pairedTargets = new ArrayList<>();

	private int numLayers;
	private Scanner scnr = new Scanner(System.in);
	private int[] neuronsInHiddenLayers = new int[] { 20, 15, 10 };

	public static void main(String[] args) throws IOException {
		int numberOfLayers = 3;
		WorkingNetwork nn = new WorkingNetwork(numberOfLayers);
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
	public WorkingNetwork(int layers) {
		this.numLayers = layers;
		for (int i = 0; i < numLayers; i++) {
			vals.add(new ArrayList<Double>());
		}
		for (int i = 0; i < numLayers - 1; i++) {
			weights.add(new ArrayList<Double>());
			deltas.add(new ArrayList<Double>());
		}

	}

	public void train() throws IOException {
		this.parseCsv();
		setHiddenLayerSizes();

		for (int i = 0; i < TRAINING_ROUNDS; i++) {
			setInputLayer(i);
			setTargets(i);
			forwardPropagate();
			backPropagate();
			// System.out.println(nn.vals.get(nn.numLayers - 1).get(0) + "new predicted
			// output");
			// System.out.println(nn.vals.get(nn.numLayers - 1).get(1) + "new predicted
			// output");
		}
	}

	public void predict() {
		Random rand = new Random();
		int curIndex = rand.nextInt(300);
		setInputLayer(curIndex);
		setTargets(curIndex);
		forwardPropagate();
		predictOutcome();
	}

	private List<List<Double>> parseCsv() throws IOException {
//		csvFile = "C:/Users/wFanga/eclipse-workspace1/SimpleNN/MNIST/mnist_train.csv";
		String trainingDataFilePath = "static/mnist_train.csv";
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(trainingDataFilePath);
//		InputStream is = new java.util.zip.ZipInputStream(zFile);
		Reader reader = new java.io.InputStreamReader(is);
		CsvListReader cs = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE);

		int count;

		try {
			count = 0;
			boolean isLabel;
			while (count < 1000) {
				trainingData.add(new ArrayList<Double>());
				isLabel = true;
				List<String> row = cs.read();
				for (String s : row) {
					if (isLabel) {
						trainingData.get(count).add(Double.valueOf(s)); //
						// System.out.print(Double.valueOf(s));
					} else {
						if (Double.valueOf(s) != 0) {
							trainingData.get(count).add(1.0); //
							// System.out.print(1.0);
						} else {
							trainingData.get(count).add(0.0); //
							// System.out.print(0.0);
						}
					}
					isLabel = false;
				}
				// System.out.println("");
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (List<Double> d : trainingData) {
			pairedTargets.add(d.get(0));
			d.remove(0);
		}
		cs.close();
		return trainingData;
	}

	private void setHiddenLayerSizes() {
		Random rand = new Random(5);

		for (int i = 0; i < this.neuronsInHiddenLayers.length; i++) { // setting hidden val sizes
			// System.out.println("Input size");
			// int size = scnr.nextInt();
			vals.get(i + 1).clear();
			for (int j = 0; j < neuronsInHiddenLayers[i]; j++) {
				vals.get(i + 1).add(0.0);

			}
			for (int k = 0; k < vals.get(i).size() * vals.get(i + 1).size(); k++) {
				weights.get(i).add(rand.nextDouble());
				deltas.get(i).add(0.0);
			}
			// System.out.println(vals.get(i - 1).size() * vals.get(i).size());

		}
	}

	private void setInputLayer(int rounds) {
		vals.get(0).clear();

		for (int i = 0; i < trainingData.get(rounds).size(); i++) {
			vals.get(0).add(trainingData.get(rounds).get(i));
			// System.out.println(vals.get(0).get(i));
		}
	}

	private void setTargets(int rounds) {
		targets.clear();

		for (int i = 0; i < 10; i++) {
			// double target = scnr.nextDouble();
			if (i == pairedTargets.get(rounds)) {
				targets.add(1.0);
			} else {
				targets.add(0.0);
			}
			// System.out.println(targets.get(i));
		}
	}

	private void forwardPropagate() {
		for (int i = 1; i < numLayers; i++) {
			double sum = 0.0;
			List<Double> currentLayer = vals.get(i);
			List<Double> previousLayer = vals.get(i - 1);
			List<Double> currentWeights = weights.get(i - 1);

			// System.out.println(" ");
			for (int currentLayerIndex = 0; currentLayerIndex < currentLayer.size(); currentLayerIndex++) {
				for (int previousLayerIndex = 0; previousLayerIndex < previousLayer.size(); previousLayerIndex++) {
					sum += currentWeights.get(currentLayerIndex * previousLayer.size() + previousLayerIndex)
							* previousLayer.get(previousLayerIndex);
				}
				// System.out.println(sum + " this is the sum for" + i);
				currentLayer.set(currentLayerIndex, 1.0 / (1.0 + Math.pow(Math.E, -sum / 100.0)));
				// System.out.println(" ");
				// System.out.println(vals.get(i).get(j) + " = neuron(" + i + ")(" + j + ")");
				sum = 0.0;
			}
		}
	}

	private void backPropagate() {
		for (int i = numLayers - 1; i > 0; i--) {
			if (i == numLayers - 1) {
				for (int j = 0; j < vals.get(i - 1).size(); j++) {
					for (int k = 0; k < vals.get(i).size(); k++) {
						deltas.get(i - 1).set(j + k * vals.get(i - 1).size(),
								deltas.get(i - 1).get(j + k * vals.get(i - 1).size())
										+ outputErrorDeriv(vals.get(numLayers - 1).get(k), targets.get(k))
												* sigmoidDeriv(vals.get(i - 1).get(j)) * vals.get(i - 1).get(j));
						weights.get(i - 1).set(j + k * vals.get(i - 1).size(),
								weights.get(i - 1).get(j + k * vals.get(i - 1).size())
										- 0.5 * deltas.get(i - 1).get(j + k * vals.get(i - 1).size()));
						// System.out.println("New w" + i + "" + "(" + (j + k * vals.get(i - 1).size())
						// + ")" + " is: "
						// + weights.get(i - 1).get(j + k * vals.get(i - 1).size()));
					}
				}
			} else {
				for (int j = 0; j < vals.get(i - 1).size(); j++) {
					for (int k = 0; k < vals.get(i).size(); k++) {
						deltas.get(i - 1).set(j + k * vals.get(i - 1).size(),
								deltas.get(i - 1).get(j + k * vals.get(i - 1).size())
										* sigmoidDeriv(vals.get(i - 1).get(j)) * vals.get(i - 1).get(j));
						weights.get(i - 1).set(j + k * vals.get(i - 1).size(),
								weights.get(i - 1).get(j + k * vals.get(i - 1).size())
										- 0.5 * deltas.get(i - 1).get(j + k * vals.get(i - 1).size()));
						// System.out.println("New w" + i + "" + "(" + (j + k * vals.get(i - 1).size())
						// + ")" + " is: "
						// + weights.get(i - 1).get(j + k * vals.get(i - 1).size()));
					}
				}
			}
		}
	}

	private double calculateAbsErr() {
		double sum = 0;

		for (int i = 0; i < vals.get(numLayers - 1).size(); i++) {
			sum += 0.5 * Math.pow(targets.get(i) - vals.get(numLayers - 1).get(i), 2);
		}
		return sum;
	}

	private double outputErrorDeriv(double val, double target) {
		return val - target; // -(target - val)
	}

	private double sigmoidDeriv(double output) {

		return output * (1 - output);
	}

	private void predictOutcome() {

		List<Double> listResults = new ArrayList<Double>();
		for (int i = 0; i < targets.size(); i++) {
			listResults.add(0.5 * Math.pow(targets.get(i) - vals.get(numLayers - 1).get(i), 2));
		}
		// System.out.println(listResults);
		double min = Collections.max(listResults);

		for (int i = 0; i < listResults.size(); i++) {
			if (min == listResults.get(i)) {
				System.out.println(i);
			}
		}

		listResults.clear();

	}

}