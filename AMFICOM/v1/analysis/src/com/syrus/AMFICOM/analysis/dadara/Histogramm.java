package com.syrus.AMFICOM.analysis.dadara;

public class Histogramm
{
	private double up_limit;
	private double down_limit;
	private int nBins;
	private double[] histo;

	public Histogramm(double down_limit, double up_limit, int nBins)
	{
		if (down_limit > up_limit)
		{
			double tmp = down_limit;
			down_limit = up_limit;
			up_limit = tmp;
		}
		this.up_limit = up_limit;
		this.down_limit = down_limit;
		this.nBins	= nBins;
	}

	public double[] init(double[] data, int start, int end)
	{
		histo = new double[nBins];
		double deriv_delta = (up_limit - down_limit) / (double)nBins;

		int N;
		for (int i = Math.max(0, start); i <= Math.min (end, data.length-1); i++)
		{
			N = (int)Math.round((data[i] - down_limit) / deriv_delta);
			if (N >= 0 && N < nBins)
				histo[N]++;
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
		return down_limit + (double)(up_limit - down_limit) / (double)nBins * max;
	}

	public double[] getHistogramm()
	{
		return histo;
	}
}