package com.syrus.AMFICOM.analysis.dadara;

public class MathRef
{
	private MathRef() {
		// no instantiation possible
	}

	public static double[] correctReflectogramm(double []data)
	{
		int begin = 300;
		if(begin > data.length / 2)
			begin = data.length / 2;

		double min = data[begin];

		for(int i = begin; i < data.length; i++)
			if(data[i] < min)
				min = data[i];

		for(int i = 0; i < data.length; i++)
			data[i] = data[i] - min;

		for(int i = 0; i <= begin; i++)
			if(data[i] < 0.)
				data[i] = 0.;

		if(data[0] > 0.001)
			data[0] = 0.;

		if(data[1] < 0.001)
			data[1] = data[2] / 2.;

		return data;
	}

	/*
	public static double[] getDerivative(double[] y)
	{
		double[] deriv = new double[y.length];
		for(int i = 0; i < y.length - 1; i++)
			deriv[i] = y[i+1] - y[i];
		deriv[y.length-1] = 0.;
		return deriv;
	}
	*/

	public static double getLinearStartPoint (double[] y)
	{
		double[] res = calcLSA (y, 0, y.length - 1);
		return res[1];
	}

	public static double[] linearize2point (double[] y, int begin, int end)
	{
		double res[] = new double[2];
		res[0] = (y[end] - y[begin]) / (end - begin);
		res[1] = y[begin] - res[0] * begin;
		return res;
	}

	/*
	public static int findInflectionPoint(double[] y, int start, int end)
	{
		for (int i = start + 1; i < end - 1; i++)
			if (sign2(y[i] - y[i-1]) != sign2(y[i+1] - y[i]))
				return i;
		return end;
	}

	public static int findFirstAbsMinimumPoint(double[] y, int start, int end)
	{
		for (int i = start + 1; i < end - 1; i++)
			if (isLocalAbsoluteMinPoint(y, i))
				return i;
		return end;
	}

	public static boolean isLocalAbsoluteMinPoint(double[] y, int point)
	{
		try
		{
			if (Math.abs(y[point-1]) > Math.abs(y[point]) && Math.abs(y[point+1]) > Math.abs(y[point]))
				return true;
		}
		catch (Exception ex)
		{
		}
		return false;
	}

	public static boolean isInflectionPoint(double[] y, int point)
	{
		try
		{
			if (sign2(y[point] - y[point-1]) != sign2(y[point+1] - y[point]))
				return true;
		}
		catch (Exception ex)
		{
		}
		return false;
	}

	public static int findConstantPoint(double[] y, int start, int end)
	{
		for (int i = start + 1; i < end - 1; i++)
			if (Math.abs(y[i] - y[i-1]) < 0.001
				&& Math.abs(y[i+1] - y[i]) < 0.001)
			return i;
		return end;
	}

	public static boolean isConstantNonZeroPoint(double[] y, int point)
	{
		try
		{
			if (Math.abs(y[point]) > 0
					&& Math.abs(y[point] - y[point-1]) < 0.001
					&& Math.abs(y[point+1] - y[point]) < 0.001)
				return true;
		}
		catch (Exception ex)
		{
		}
		return false;
	}

	public static int[] findLocalAbsMinimumPoints(double[] y, int start, int end)
	{
		double[] tmp = new double[end - start];
		int[] res = new int[end - start];
		for (int i = start; i < end; i++)
			tmp[i - start] = Math.abs(y[i]);

		int counter = 0;
		boolean b = false;
		int st = 0;
		for (int i = 1; i < tmp.length; i++)
		{
			if (tmp[i] - tmp[i-1] < 0.0001)
			{
				b = true;
				st = i;
			}
			else if (tmp[i] - tmp[i-1] > 0.0001 && b)
			{
				for (int j = st; j < i; j++)
					res[counter++] = j;
				b = false;
			}
			if (tmp[i] < 0.0001)
			{
				b = false;
			}
		}
		int[] _res = new int[counter];
		for (int i = 0; i < counter; i++)
			_res[i] = res[i] + start;
		return _res;
	}
	*/

	public static double[] calcLSA(double[] y, int begin, int end)
	{
		return calcLSA(y, begin, end, 0);
	}

	public static double[] calcLSA(double[] y, int begin, int end, int shift)
	{
		double alfa=0d;
		double beta=0d;
		double gamma=0d;
		double dzeta=0d;
		double d=0;

		if(begin < 0)
			begin=0;
		if(end < 1)
			end=1;
		if(end > y.length -1)
			end = y.length -1;

		for(int i = begin; i<=end; i++)
		{
			d = i - shift;
			beta = beta - y[i] * d;
			alfa = alfa + d * d;
			gamma = gamma + d;
			dzeta = dzeta - y[i];
		}
		double n = end - begin + 1;
		double res[] = new double[2];
		res[0] = (n*beta/gamma - dzeta)/(gamma - n*alfa/gamma);
		res[1] = -(alfa*res[0] + beta)/gamma;
		return res;
	}

	public static double calcORL (double[] y, int begin, int end)
	{
		return calcORL (y[begin], y[end]);
	}

	public static double calcORL (double y1, double y2)
	{
		if (y1 == y2)
			return 0;
		double s = 0.001;
		double loss = y1 - y2;
		double ret = (-10*Math.log(s/2d *(1d - Math.exp(-2d*0.23*Math.abs(loss))))/Math.log(10));
		return ret; // @todo: replace values < -99 with '-inf' (?)
	}

	// ��������� ����� ��� ���������
	// ���������: ����� ����� � ��, ������������ �������� � ��
	public static double calcSigma (int wavelength, int pulsewidth)
	{
		// FIXIT: verify
		//System.out.println("calcSigma: wavelength = " + wavelength + " pulse = " + pulsewidth); // FIXIT - remove
		
		// XXX: ��������������� ������, �������������� ����������� �������
		double sigma0;
		double vG = 3e8 / 1.47 * 1e-9; // �/��
		switch (wavelength)
		{
			//case 1310: sigma = 49d; break;
			//case 1550: sigma = 52d; break;
			//case 1625: sigma = 52.8d; break;
			//default: sigma = 51d;
			case 1310: sigma0 = 42d; break;
			case 1550: sigma0 = 45d; break;
			case 1625: sigma0 = 46d; break; // XXX: 1625 nm: sigma0 = ?
			default:
				sigma0 = 46d;
				System.out.println("calcSigma: warning: unknown wavelength " + wavelength);
		}
		if (pulsewidth == 0)
			pulsewidth = 1000; // XXX: default pulsewidth
		//return sigma + Math.log(1000d/(double)pulsewidth) / Math.log(10d);
		return sigma0 + Math.log(vG / pulsewidth) / Math.log(10d);
	}

	// ��������� ���������
	// ��������� ��������������� �����, ��������� ��������� � ��
	public static double calcReflectance (double sigma, double peak)
	{
		//System.out.println("calcReflectance: sigma "+sigma+" peak "+peak);
		if (peak < 0)
			peak = 0; // XXX
		return (-sigma + 10d*Math.log(Math.pow(10d, peak/5d) - 1)/Math.log(10));
	}

	public static double round_4 (double d)
	{
		return Math.round(d * 10000.0) / 10000.0;
	}

	public static double round_3 (double d)
	{
		return Math.round(d * 1000.0) / 1000.0;
	}

	public static double round_2 (double d)
	{
		return Math.round(d * 100.0) / 100.0;
	}

	public static double round_1 (double d)
	{
		return Math.round(d * 10.0) / 10.0;
	}

	/*
	public static double round (double d, int digits)
	{
		double pow = Math.pow(10d, (double)digits);
		return ((double)Math.round(d * pow)) / pow;
	}
	*/

	/*
	public static int sign(double d)
	{
		if (d > 0)
			return 1;
		if (d < 0)
			return -1;
		return 0;
	}
	*/

	public static int sign2(double d)
	{
		if (d < 0)
			return -1;
		return 1;
	}
}
