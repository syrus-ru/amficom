/*
 * $Id: CoreAnalysisManager.java,v 1.2 2004/12/17 18:16:20 arseniy Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

/**
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2004/12/17 18:16:20 $
 * @module
 */

import java.util.Map;

import com.syrus.io.BellcoreStructure;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;

public class CoreAnalysisManager
{
	private CoreAnalysisManager() {
	}

	private static native double[] gauss(double[] y, double center,
			double amplitude, double sigma);

	/**
	 * @param waveletType ����� - ����� ��������
	 * @param y ������� ��������������
	 * @param d_x ��� ������������� � ������
	 * @param formFactor ?
	 * @param min_level ���. ������� �������
	 * @param max_level_noise ����. ������� ����?
	 * @param min_level_to_find_end ���. ������� �����
	 * @param min_weld ���. ������
	 * @param min_connector ���. ���������
	 * @param out_meanAttenuation ������ ��� �������� ��������
	 * �������� ���������, ������ �� ����������
	 * @param reflectiveSize ���. ������ �����. ������� (� ������?)
	 * @param nonReflectiveSize ���. ������ �������. ������� (� ������?)
	 * @return ������ �������
	 */
	private static native ReflectogramEvent[] analyse2(int waveletType,
			double[] y, //the refl. itself
			double d_x, //dx
			double formFactor, // 1. ����-������
			double min_level, // 2. ��� ������� �������
			double max_level_noise, // 3. ���� ������� ����
			double min_level_to_find_end,// 4. ���. ������� �����
			double min_weld, // 5. ����������� ������
			double min_connector, // 6. ����������� ���������
			double[] out_meanAttenuation, // ��� �������� �������� �������� ���������
			int reflectiveSize, int nonReflectiveSize);
	
	/**
	 * ����� ��������� ������� ���� �� �������������� �� ������ 3 sigma,
	 * ����������� ������� ����, ����������� �� �������� ����������� �������.
	 * ��� ������������ �� ������ ����������� ������������� ��������
	 * fabs(2*data[n] - data[n-8] - data[n+8]) ��� ����� n-8,n,n+8, ���
	 * ��� ������������ data[], � data = 10^(y/5) - ��������
	 * (�� ���������������) �������� �������. ����� ����������� �������
	 * ������� ���� � �������� ����������� ������� y[]. ��� 8 ������
	 * �� ������ ��������� NetTest(tm) ��� ���� ������������������ �������
	 * ������� BC.
	 * 
	 * ����������� sigma ������������� ��� ������������� � ���������� 
	 * ������������� � ����������� ������� �� ���� ����.
	 * 
	 * � ������ ��������� "� �������������� ������� ���� �������
	 * �������� ������ ��������", �������� ��� ����� ������ �� �������,
	 * � ������� ��� ����� ����� ������ ��������.
	 *      
	 * @param y ������� ��������������
	 * @return 3*sigma ���������� ����, ����������
	 * � �������� ������������������� ��������
	 */
	private static native double nCalcNoise3s(double[] y);

	static {
		try {
			System.loadLibrary("dadara");
		} catch (java.lang.UnsatisfiedLinkError ex) {
			ex.printStackTrace();
		}
	}

	public static double[] calcGaussian(double[] y, int max_index) {
		double[] gauss = new double[y.length];
		int width = 0;

		double max_value = y[max_index];

		max_value *= .2;
		for (int i = 0; i < y.length; i++)
			if (y[i] > max_value)
				width++;
		max_value /= .2;

		double[] d = gauss(y, max_index, max_value, width);
		double center = d[0];
		max_value = d[1];
		double sigma_squared = d[2] * d[2];

		for (int i = 0; i < gauss.length; i++)
			gauss[i] = max_value
					* Math.exp(-(i - center) * (i - center) / sigma_squared);

		return gauss;
	}

	public static double[] calcThresholdCurve(double[] y, int max_index) {
		double[] threshold = new double[y.length];
		double max = 0;

		for (int i = 0; i < threshold.length; i++) {
			threshold[i] = 0;
			for (int j = 0; j < y.length; j++)
				if (j <= max_index - i || j >= max_index + i)
					threshold[i] += y[j];

			if (max < threshold[i])
				max = threshold[i];
		}

		for (int i = 0; i < threshold.length; i++)
			threshold[i] /= max;

		return threshold;
	}

	/**
	 * ������ ������. �������� �� UI ���������, ��������� � ����������
	 * �������� ������ IA, fit, calcMutualParameters � ������������ ���. �������.
	 * 
	 * @param strategy ������� �������������. �����������������.
	 * @param bs ��������������
	 * @param pars ������������������� ����� ���������� ��� IA
	 * @param bellcoreTraces ��� ���� �������� �/� ��� ����������� �������� ���������
	 * @return ������ �������
	 */

	// strategy == 0: min fitting
	public static ReflectogramEvent[] makeAnalysis(
			int strategy, BellcoreStructure bs,
			double[] pars, Map bellcoreTraces)
	{
	    // ������� ������
	    double y[] = bs.getTraceData();
	    double delta_x = bs.getResolution(); // �����

		int reflSize = ReflectogramMath.getReflectiveEventSize(y, 0.5);
		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
				y,
				1000,
				bs.getIOR(),
				delta_x);
		if (nReflSize > 3 * reflSize / 5)
			nReflSize = 3 * reflSize / 5;

		// ������������ �������
		double[] meanAttenuation = { 0 }; // storage for meanAttenuation -- unused: XXX
		ReflectogramEvent[] ep = analyse2((int) pars[7], y, delta_x, pars[5],
				pars[0], pars[4], pars[3], pars[1], pars[2], meanAttenuation,
				reflSize, nReflSize);

		// ���������� ������� ���� ��� ���������
		double noiseLevel = calcNoise3s(y);

		// ��������� ���������� ��������� � ���������
		// (� ������������ ���������� ������ ��� ������� ������ � ���������)
		fitTrace(y, delta_x, ep, strategy, meanAttenuation[0], noiseLevel);

		// ����������� ���������� ������ � ��������� (�� ������� ��������)
		ReflectogramEvent.calcMutualParameters(ep, y);

		// ������ ��� � ��������� ������
		//for (int i = 0; i < ep.length; i++)
		//	ep[i].setDefaultThreshold(y);
		for (int i = 0; i < ep.length; i++)
			ep[i].setDefaultThreshold(bs, bellcoreTraces);

		return ep;
	}

	private static ReflectogramEvent[] fitTrace(double[] y, double delta_x,
			ReflectogramEvent[] events, int strategy, double meanAttenuation, double noiseLevel) {
		long t0 = System.currentTimeMillis();

		System.out.println("fitTrace: strategy = " + strategy);

		ReflectogramEvent.FittingParameters fPars =
		    new ReflectogramEvent.FittingParameters(
				y,
				2, // error mode = 2: XXX
				0.01 + 0.001, // XXX: 0.01: user-set; 0.001: rg precision 
				noiseLevel);

		for (int i = 0; i < events.length; i++)
		    events[i].setFittingParameters(fPars);

		for (int i = 0; i < events.length; i++)
			events[i].doFit();

		long t1 = System.currentTimeMillis();

		System.out.println("delta time: fit: " + (t1 - t0) + " ms");

		return events;
	}
	
	// ���������� ������� ���� � �/�
	public static double calcNoise3s(double[] y)
	{
		return nCalcNoise3s(y);
	}
}
