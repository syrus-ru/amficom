package com.syrus.AMFICOM.analysis.dadara;

public class Histogramm {
	private double upLimit;
	private double downLimit;
	private int nBins;
	private double[] histo;

	public Histogramm(double downLimit, double upLimit, int nBins) {
		if (downLimit > upLimit) {
			double tmp = downLimit;
			downLimit = upLimit;
			upLimit = tmp;
		}
		this.upLimit = upLimit;
		this.downLimit = downLimit;
		this.nBins = nBins;
	}

	public double[] init(double[] data, int start, int end) {
		this.histo = new double[this.nBins];
		double derivDelta = (this.upLimit - this.downLimit) / this.nBins;

		int n;
		for (int i = Math.max(0, start); i <= Math.min(end, data.length - 1); i++) {
			n = (int) Math.round((data[i] - this.downLimit) / derivDelta);
			if (n >= 0 && n < this.nBins) {
				this.histo[n]++;
			}
		}
		return this.histo;
	}

	public int getMaximumIndex() {
		int center = 0;
		double max = this.histo[0];
		for (int i = 1; i < this.histo.length; i++) {
			if (this.histo[i] > max) {
				max = this.histo[i];
				center = i;
			}
		}
		return center;
	}

	public double getMaximumValue() {
		int max = getMaximumIndex();
		return this.downLimit + (this.upLimit - this.downLimit) / this.nBins * max;
	}

	public double[] getHistogramm() {
		return this.histo;
	}
}
