/*
 * $Id: CoreAnalysisManager.java,v 1.18 2005/03/03 15:58:30 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis;

/**
 * @author $Author: saa $
 * @version $Revision: 1.18 $, $Date: 2005/03/03 15:58:30 $
 * @module
 */

import java.util.Map;

import com.syrus.io.BellcoreStructure;
import com.syrus.AMFICOM.analysis.dadara.ModelFunction;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceManager;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.ReflectogramMath;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventImpl;

public class CoreAnalysisManager
{
	protected CoreAnalysisManager()
	{ // empty
	}

	private static native double[] gauss(double[] y, double center,
			double amplitude, double sigma);
	
	private static native double nMedian(double[] y, int pos);

	/**
	 * @param y ��������������, ����������� � �������������
	 * @param dX ��� ������������� � ������ �� DX (�� ������������?)
	 * @param minLevel ������� ���������������� ��� ����������� ������ �������
	 * @param minWeld ����������� �����. �������
	 * @param minConnector ����������� �����. �������
	 * @param minEnd ���. ������� ����� (���� �� ������������?)
	 * @param reflSize ���. ������ �����. ������� (� ������) - ���������
	 * @param nReflSize ���. ������ �������. ������� (� ������)
	 * @param noiseDB ������� ���� �� 1 �����, � ���. ��; ����� ���� null - ����� ����� ������ �������������
	 * @return ������ �������
	 */
	private static native SimpleReflectogramEventImpl[] analyse3(
			double[] y,
			double dX, 
			double minLevel,
			double minWeld,
			double minConnector, 
			double minEnd,
			int reflSize,
			int nReflSize,
			double[] noiseDB);

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

	private static native double[] nCalcNoiseArray(double[] y);

	/**
	 * ������ ������ ���� �� ������ ��������������.
	 * �������� ����� ������������ ��� �� ������������ �������
	 * ���������������� IA ������� (ev). �� ���������, ��� �����
	 * ����������� ���� ����� ������ ���������� IA.
	 * @param y  ������� ������ �������������� � ��.
	 * @param ev (�����������) ��������������� ������ IA. ���� �� null,
	 *   �� ����� ��������������, � ����� �� ��������������.
	 *   ���� null, �� �� ������������.
	 * @return ������������� �������� ���� (��) �� ������ 3 �����  
	 */
	private static double[] calcNoiseArray(double[] y, SimpleReflectogramEvent[] ev)
	{
		double[] ret = nCalcNoiseArray(y);
		for (int i = 0; i < ret.length; i++)
			ret[i] *= 3.0;
		return ret;
	}

	static {
		try {
			System.loadLibrary("dadara");
		} catch (java.lang.UnsatisfiedLinkError ex) {
			ex.printStackTrace();
		}
	}

	public static double[] calcGaussian(double[] y, int maxIndex) {
		double[] gauss = new double[y.length];
		int width = 0;

		double maxValue = y[maxIndex];

		maxValue *= .2;
		for (int i = 0; i < y.length; i++)
			if (y[i] > maxValue)
				width++;
		maxValue /= .2;

		double[] d = gauss(y, maxIndex, maxValue, width);
		double center = d[0];
		maxValue = d[1];
		double sigmaSquared = d[2] * d[2];

		for (int i = 0; i < gauss.length; i++)
			gauss[i] = maxValue
					* Math.exp(-(i - center) * (i - center) / sigmaSquared);

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
	public static ModelTraceManager makeAnalysis(
			int strategy, BellcoreStructure bs,
			double[] pars, Map bellcoreTraces)
	{
		long t0 = System.currentTimeMillis();
	    // ������� ������
	    double y[] = bs.getTraceData();
	    double deltaX = bs.getResolution(); // �����

		int reflSize = ReflectogramMath.getReflectiveEventSize(y, 0.5);
		int nReflSize = ReflectogramMath.getNonReflectiveEventSize(
				y,
				1000, // @todo: ����� ������������ �������� �����
				bs.getIOR(),
				deltaX);
		if (nReflSize > 3 * reflSize / 5) // @todo: remove?
			nReflSize = 3 * reflSize / 5;

		reflSize *= 5; // XXX -- ?

		System.out.println("reflSize="+reflSize+"; nReflSize="+nReflSize);

		long t1 = System.currentTimeMillis();
		// ���������� ������� ���� ��� ���������
		// @todo: �� ��� �������, �� ��� ��������� �� ����� ��. ���� ��
		// ���� ����� �/�, �������, ��� ��������� ������,
		// � ����� ������� ��� ���� �� ����� ����� �� ����� �������
		double[] noiseArray = calcNoiseArray(y, null);

		long t2 = System.currentTimeMillis();
		// ������������ �������
		SimpleReflectogramEventImpl[] se = analyse3(y, deltaX,
				pars[0], pars[1], pars[2], pars[3],
				reflSize, nReflSize, noiseArray);

		int traceLength = se.length > 0
			? se[se.length - 1].getEnd() + 1
			: 0;

		long t3 = System.currentTimeMillis();
		ModelFunction mf = ModelFunction.CreateFitedAsBreakL(y, 0, traceLength, noiseArray);

		long t4 = System.currentTimeMillis();

		// ������ ��� � ��������� ������ -- FIXME - �������
//		for (int i = 0; i < ep.length; i++)
//			ep[i].setDefaultThreshold(bs, bellcoreTraces);

		long t5 = System.currentTimeMillis();
		System.out.println("makeAnalysis: getData: " + (t1-t0) + "; noiseArray:" + (t2-t1) + "; IA: " + (t3-t2) + "; fit: " + (t4-t3) + "; postProcess: " + (t5-t4));

		return new ModelTraceManager(se, mf, deltaX);
	}

	// ���������� ������� ���� � �/� -- ������ �����
	public static double calcNoise3s(double[] y)
	{
		return nCalcNoise3s(y);
	}

	public static double getMedian(double[] y, int pos)
	{
		return nMedian(y, pos);
	}
	public static double getMedian(double[] y)
	{
		return nMedian(y, y.length / 2);
	}

	/**
	 * �������� ��������� ����������. ����� ��������� �������� �������� ���������
	 * L-��������������� �� �����, � ����� ���� �����. ���. � ��������� ���������
	 * ����������, ����� ����� ���� �������� ����� �� �����.
	 * @param mf ��������� ������
	 * @param evBegin ��������� ������ �������
	 * @param evEnd ��������� ����� �������
	 * @return int[3] { ������ ������� (���������� ������), ��������, ��������� ������� (���������� �����) } 
	 */
	public static int[] getConnectorMinMaxMin(ModelFunction mf, int evBegin, int evEnd)
	{
		int x0 = evBegin;
		int N = evEnd - evBegin + 1;
		double[] arr = mf.funFillArray(x0, 1.0, N);
		int iMax = ReflectogramMath.getArrayMaxIndex(arr, 0, N - 1);
		int iLMin = ReflectogramMath.getArrayMinIndex(arr, 0, iMax);
		int iRMin = ReflectogramMath.getArrayMinIndex(arr, iMax, N - 1);
		return new int[] {evBegin + iLMin, evBegin + iMax, evBegin + iRMin};
	}
}
