package io.artie.ai.cnn;

import java.util.ArrayList;
import java.util.List;

public class Convolution extends Layer {

	private List<Image> featureMaps = new ArrayList<Image>();
	private List<Filter> filters = new ArrayList<Filter>();

	Convolution(int id, List<Double[]> filterVals) {
		super(id, 100);
		this.setType(LAYER_TYPE.CONVOLUTIONAL);
		for (Double[] kernal : filterVals) {
			filters.add(new Filter(filterVals.indexOf(kernal), 2, this, kernal));
		}
	}

	public Image getFeatureMap(int index) {
		return featureMaps.get(index);
	}

	public Node getFilter(int index) {
		return filters.get(index);
	}

	public void convolve() {
		Layer parentLayer = this.getParent();
		List<Double> subImage = null;
		List<Double> filterVals = null;
		double kernalSum = 0.0;
		double channelSum = 0.0;

		for (Filter filter : filters) {

			List<Double> activationMap = new ArrayList<Double>();
			List<ArrayList<Double>> mapHolder = new ArrayList<ArrayList<Double>>();
			List<Double> finishedActivationMap = new ArrayList<Double>();

			for (Image image : parentLayer.getImages()) {
				for (int rowIndex = 0; rowIndex < image.getRowLength(); rowIndex++) {
					for (int colIndex = 0; colIndex < image.getRowLength(); colIndex++) {

						subImage = image.getSubImage(rowIndex, colIndex, image.getRowLength());
						filterVals = filter.getKernalVals();
						for (int i = 0; i < subImage.size(); i++) {
							kernalSum += subImage.get(i) * filterVals.get(i);
						}
						activationMap.add(kernalSum);
						kernalSum = 0.0;
					}

				}
				mapHolder.add((ArrayList<Double>) activationMap);
			}

			for (int i = 0; i < mapHolder.get(0).size(); i++) {
				for (ArrayList<Double> featureMap : mapHolder) {
					channelSum += featureMap.get(i).doubleValue();
				}
				finishedActivationMap.add(channelSum);
			}

			double[] tempActivationMap = new double[finishedActivationMap.size()];
			for (Double d : finishedActivationMap) {
				tempActivationMap[finishedActivationMap.indexOf(d)] = d.doubleValue();
			}
			int conversionFactor = (int) parentLayer.getImages().get(0).getRowLength() - filter.getSize() + 1;

			this.addImage(new Image(filter.getNodeID(), 0.0, tempActivationMap, conversionFactor));
		}
	}
}
