package com.syrus.util;

public class MathRef
{
	public static double getLinearStartPoint (double[] y)
	{
		double[] res = LSA (y, 0, y.length - 1);
		return res[1];
	}

	public static double[] linearize (double[] y, int begin, int end)
	{
		double res[] = new double[2];
		res[0] = (y[end] - y[begin]) / (double)(end - begin);
		res[1] = y[begin] - res[0] * (double)begin;
		return res;
	}

	public static double[] LSA(double[] y, int begin, int end)
	{
		double alfa=0d;
		double beta=0d;
		double gamma=0d;
		double dzeta=0d;

		if(begin < 0)
			begin=0;
		if(end < 1)
			end=1;
		if(end > y.length -1)
			end = y.length -1;

		for(int i = begin; i<=end; i++)
		{
			beta = beta - y[i]*((double)i);
			alfa = alfa + ((double)i)*((double)i);
			gamma = gamma + ((double)i);
			dzeta = dzeta - y[i];
		}
		double n = (double)(end - begin + 1);
		double res[] = new double[2];
		res[0] = (n*beta/gamma - dzeta)/(gamma - n*alfa/gamma);
		res[1] = -(alfa*res[0] + beta)/gamma;
		return res;
	}

	public static double ORL (double[] y, int begin, int end)
	{
		return ORL (y[begin], y[end]);
	}

	public static double ORL (double y1, double y2)
	{
		if (y1 == y2)
			return 0;
		double S = 0.001;
		double loss = y1 - y2;
		return (-10*Math.log(S/2d *(1d - Math.exp(-2d*0.23*Math.abs(loss))))/Math.log(10));
	}

	// вычислить сигму для отражения
	// параметры: длина волны в нм, длительность импульса в нс
	public static double calcSigma (int wavelength, int pulsewidth)
	{
		double sigma;
		switch (wavelength)
		{
			case 1310: sigma = 49d; break;
			case 1550: sigma = 52d; break;
			case 1625: sigma = 52.8d; break;
			default: sigma = 51d;
		}
		if (pulsewidth == 0)
			pulsewidth = 1000;
		return sigma + Math.log(1000d/(double)pulsewidth)/Math.log(10d);
	}

	// вычислить отражение
	// параметры вышеприведенная сигма, амплитуда отражения в дБ
	public static double calcReflectance (double sigma, double peak)
	{
		return (-sigma + 10d*Math.log(Math.pow(10d, peak/5d) - 1)/Math.log(10));
	}

	public static double round_4 (double d)
	{
		return ((double)Math.round(d * 10000d)) / 10000d;
	}

	public static double round_3 (double d)
	{
		return ((double)Math.round(d * 1000d)) / 1000d;
	}

	public static double round_1 (double d)
	{
		return ((double)Math.round(d * 10d)) / 10d;
	}

	public static double round_2 (double d)
	{
		return ((double)Math.round(d * 100d)) / 100d;
	}

	public static double round (double d, int digits)
	{
		double pow = Math.pow(10d, (double)digits);
		return ((double)Math.round(d * pow)) / pow;
	}
}