package io.artie.ai.cnn;

import java.util.*;

public class Filter {
	private int iD;
	private List<List<Double>> kernal;

	public Filter(int iD, Double[]... rows) {
		this.iD = iD;
		for (Double[] row : rows) {
			List<Double> curRow = Arrays.asList(row);
			kernal.add(curRow);
		}
	}

	public double getKernalVal(int row, int col) {

		return kernal.get(row).get(col);
	}
}
