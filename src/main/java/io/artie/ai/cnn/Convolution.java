package io.artie.ai.cnn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Convolution extends Layer {

	private List<Image> outputImages = new ArrayList<Image>();
	private List<Filter> filters = new ArrayList<Filter>();
	private int poolingSize = 0;

	Convolution(int id, List<Double[]> filterVals, int poolingSize) {
		super(id, 100);
		this.setType(LAYER_TYPE.CONVOLUTIONAL);
		for (Double[] kernal : filterVals) {
			filters.add(new Filter(filterVals.indexOf(kernal), 2, this, kernal));
		}
		this.poolingSize = poolingSize;
	}

	public Image getOutputImage(int index) {
		return outputImages.get(index);
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
				for (int rowIndex = 0; rowIndex < image.getRowLength() - 1; rowIndex++) {
					for (int colIndex = 0; colIndex < image.getRowLength() - 1; colIndex++) {

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
			this.addOutputImage(new Image(filter.getNodeID(), 0.0, tempActivationMap, conversionFactor));
		}
	}

	public List<Image> getOutputImages() {
		return this.outputImages;
	}

	public void setOutputImages(List<Image> images) {
		this.outputImages = images;
	}

	public void setOutputImage(Image image, int index) {
		this.outputImages.set(index, image);
	}

	public void addOutputImage(Image image) {
		this.outputImages.add(image);
	}

	public void pool() {

		List<Double> poolSorter = null;
		List<Double> pooledImage = null;
		double[] convertedPooledImage = null;

		for (Image image : this.getImages()) {

			pooledImage = new ArrayList<Double>();

			for (int rowIndex = 0; rowIndex < image.getRowLength() / poolingSize; rowIndex++) {
				for (int colIndex = 0; colIndex < image.getRowLength() / poolingSize; colIndex++) {

					poolSorter = new ArrayList<Double>();

					for (int poolingRowIndex = 0; poolingRowIndex < poolingSize; poolingRowIndex++) {
						for (int poolingColIndex = 0; poolingColIndex < poolingSize; poolingColIndex++) {
							poolSorter.add(image.getIndexValue(rowIndex * poolingSize + poolingRowIndex,
									colIndex * poolingSize + poolingColIndex));
						}
					}
					Collections.sort(poolSorter);
					Collections.reverse(poolSorter);
					pooledImage.add(poolSorter.get(0));
				}
			}

			convertedPooledImage = new double[pooledImage.size()];

			for (Double d : pooledImage) {
				convertedPooledImage[pooledImage.indexOf(d)] = d.doubleValue();
			}
			int conversionFactor = this.getOutputImages().get(0).getRowLength() / poolingSize;

			this.setOutputImage(new Image(image.getiD(), convertedPooledImage, conversionFactor), image.getiD());

		}

	}

	public List<Double> getImagesAsLinear() {

		List<Double> returnedLinearImage = new ArrayList<Double>();

		for (Image outputImage : outputImages) {
			returnedLinearImage.addAll(outputImage.getImageAsNNInput());
		}

		return returnedLinearImage;
	}
}
