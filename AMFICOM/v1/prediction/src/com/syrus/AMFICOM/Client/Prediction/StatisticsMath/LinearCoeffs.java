package com.syrus.AMFICOM.Client.Prediction.StatisticsMath;

public class LinearCoeffs
{
	public double c = 0.;
	public double k = 0.;
	public double dispersia = 0.;
	public double absDispersia = 0.;
	public double chi2 = 0.;

	public LinearCoeffs(double c, double k)
	{
		this.c = c;
		this.k = k;
	}

	public LinearCoeffs(double c, double k, double dispersia, double absDispersia)
	{
		this(c, k);
		this.dispersia = dispersia;
		this.absDispersia = absDispersia;
	}

	public LinearCoeffs(double c, double k, double dispersia, double absDispersia, double chi2)
	{
		this(c, k, dispersia, absDispersia);
		this.chi2 = chi2;
	}

	public double f(double arg)
	{
		return c+k*arg;
	}
}

