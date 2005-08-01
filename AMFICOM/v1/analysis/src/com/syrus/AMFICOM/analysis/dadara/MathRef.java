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

	public static double getLinearStartPoint (double[] y)
	{
		double[] res = calcLSA (y, 0, y.length - 1);
		return res[1];
	}

	public static double[] calcLSA(double[] y, int begin, int end)
	{
		return calcLSA(y, begin, end, 0);
	}

	private static double[] calcLSA(double[] y, int begin, int end, int shift)
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

	private static final double MINF_DESIGNATION = -99; // XXX: ��������� ����� -99 ���������� ��� -99 ��

	// �������� ����� MINF_DESIGNATION ���������� �� MINF_DESIGNATION 
	public static double calcORL (double y1, double y2)
	{
		if (y1 == y2)
			return 0;
		double s = 0.001;
		double loss = y1 - y2;
		double ret = (-10*Math.log(s/2d *(1d - Math.exp(-2d*0.23*Math.abs(loss))))/Math.log(10));
		if (ret < MINF_DESIGNATION)
			return MINF_DESIGNATION;
		else
			return ret;
	}

	// ��������� ����� ��� ���������
	// ���������: ����� ����� � ��, ������������ �������� � ��
	public static double calcSigma (int wavelength, int pulsewidth)
	{
		// ����������� ����� ��� �������� 1 ���
		double sigma0;
		switch (wavelength)
		{
		// ������ ������
		//case 1310: sigma0 = 49; break;
		//case 1550: sigma0 = 52; break;
		//case 1625: sigma0 = 53; break; // XXX: 1625 nm: sigma0 = ?

		// �������� ��������� �� NetTest:
		case 1310: sigma0 = 48.4; break;
		case 1550: sigma0 = 51.7; break;
		case 1625: sigma0 = 50.0; break;
		default:
			sigma0 = 51d;
			System.out.println("calcSigma: warning: unknown wavelength " + wavelength);
		}
		// ���� ����� �������� �� ������, ����� 1 ���
		if (pulsewidth == 0)
			pulsewidth = 1000; // XXX: default pulsewidth
		// ���������� ����� ��� ������ ��������
		// � ������� ����������� ����� ��������, ���������� � ���
		return sigma0 - 10.0 * Math.log10(pulsewidth / 1000.0);
	}

	/**
	 * ��������� ����������� ���������.
	 * <p> �������� ����� MINF_DESIGNATION ���������� �� MINF_DESIGNATION </p> 
	 * @param sigma ������������ ����� {@link #calcSigma(int, int)}
	 * @param peak ��������� �������������� ��������, �� � ��������� 5
	 * @return ����������� ���������, �� � ��������� 10
	 */
	public static double calcReflectance(double sigma, double peak)
	{
		if (peak <= 0)
			return MINF_DESIGNATION;
		double ret = -sigma
				+ 10.0 * Math.log10(Math.pow(10.0, peak/5.0) - 1);
//		System.err.println("calcReflectance: sigma " + sigma
//				+ ", peak " + peak
//				+ ", ret " + ret);
		if (ret < MINF_DESIGNATION)
			return MINF_DESIGNATION;
		return ret;
	}

	/**
	 * ���������� ��������� �������������� ��������
	 * �� ������������ ���������.
	 * <p> ������������ � ������ "�������������" </p>
	 * @param sigma ������������ ����� {@link #calcSigma(int, int)}
	 * @param reflectance ��������� (�� � ��������� 10)
	 * @return ��������� �������������� �������� (�� � ��������� 5)
	 */
	public static double calcPeakByReflectance(double sigma,
			double reflectance) {
		return 5.0 * Math.log10(
				Math.pow(10.0, (reflectance + sigma) / 10.0 )
				+ 1);
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

	/**
	 * ��������� ����� � ��������� ������ �� ���������� ����� ��������
	 * ����.
	 * @param v ����������� �����
	 * @param digits ��������� ����� �������� ���� (1 � �����)
	 * @return ����������� ��������
	 */
	public static double floatRound(double v, int digits)
	{
		if (v == 0)
			return 0;
		if (v < 0)
			return -floatRound(-v, digits);
		int p = 0;
		double ddd = Math.pow(10.0, digits - 1);
		while (v >= ddd * 10)
		{
			v /= 10;
			p--;
		}
		while (v < ddd)
		{
			v *= 10;
			p++;
		}
		return Math.round(v) / Math.pow(10.0, p);
	}

	/*
	public static double round (double d, int digits)
	{
		double pow = Math.pow(10d, (double)digits);
		return ((double)Math.round(d * pow)) / pow;
	}
	*/
}
