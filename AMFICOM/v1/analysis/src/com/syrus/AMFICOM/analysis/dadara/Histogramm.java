package com.syrus.AMFICOM.analysis.dadara;

public class Histogramm
{
	private double upLimit;
	private double downLimit;
	private int nBins;
	private double[] histo;

	public Histogramm(double downLimit, double upLimit, int nBins)
	{
		if (downLimit > upLimit)
		{
			double tmp = downLimit;
			downLimit = upLimit;
			upLimit = tmp;
		}
		this.upLimit = upLimit;
		this.downLimit = downLimit;
		this.nBins	= nBins;
	}

	public double[] init(double[] data, int start, int end)
	{
		histo = new double[nBins];
		double derivDelta = (upLimit - downLimit) / (double)nBins;

		int n;
		for (int i = Math.max(0, start); i <= Math.min (end, data.length-1); i++)
		{
			n = (int)Math.round((data[i] - downLimit) / derivDelta);
			if (n >= 0 && n < nBins)
				histo[n]++;
		}
		return histo;
	}

	public int getMaximumIndex()
	{
		int center = 0;
		double max = histo[0];
		for (int i = 1; i < histo.length; i++)
			if (histo[i] > max)
			{
				max = histo[i];
				center = i;
			}
			return center;
	}

	public double getMaximumValue()
	{
		int max = getMaximumIndex();
		return downLimit + (double)(upLimit - downLimit) / (double)nBins * max;
	}

	public double[] getHistogramm()
	{
		return histo;
	}
}
